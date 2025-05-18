import os
import logging
import json
import pandas as pd
from elasticsearch import Elasticsearch
from datetime import datetime
from elasticsearch.exceptions import AuthenticationException
import redis
import requests
import re
from apscheduler.schedulers.background import BackgroundScheduler
from concurrent.futures import ThreadPoolExecutor

logging.basicConfig(level=logging.DEBUG)

import subprocess
import os

# Выполнение запроса через rsync
def sync_logs():
    try:
        # Выполнение команды синхронизации
        subprocess.run(
            ['rsync', '-avp', '--delete-after', 'git.altlinux.org::beehive-logs/Sisyphus-x86_64/latest/error', '.'],
            check=True)
        print("Синхронизация завершена успешно.")
    except subprocess.CalledProcessError as e:
        print(f"Ошибка при синхронизации: {e}")


# Запуск нескольких Jupyter Notebook для записи логов в JSON
def run_notebooks(notebook_files):
    for notebook in notebook_files:
        try:
            # Запуск каждого файла .ipynb с помощью Jupyter
            print(f"Запуск {notebook}...")
            subprocess.run(['jupyter', 'nbconvert', '--to', 'notebook', '--execute', notebook], check=True)
            print(f"{notebook} выполнен успешно.")
        except subprocess.CalledProcessError as e:
            print(f"Ошибка при запуске {notebook}: {e}")

def read_json_errors(json_path):
    """Чтение JSON файла с ошибками для кластеров с проверкой наличия файла"""
    if not os.path.exists(json_path):
        logging.error(f"Файл {json_path} не найден.")
        return {}  # Возвращаем пустой словарь, если файл не найден

    try:
        with open(json_path, 'r', encoding='utf-8') as file:
            return json.load(file)
    except Exception as e:
        logging.error(f"Ошибка при чтении JSON файла: {e}")
        return {}


def read_logs_csv(csv_path, chunksize=1000):
    """Чтение CSV с логами с чанками для экономии памяти"""
    return pd.read_csv(csv_path, chunksize=chunksize)


def read_chunk_21(chunk_path):
    """Чтение файла chunk_21"""
    return pd.read_csv(chunk_path)


def truncate_string(input_string):
    return re.sub(r'-\d.*', '', input_string)


def fetch_package_info(file_name):
    """Обработка запросов к API параллельно для получения информации о пакете"""
    print(file_name)
    print(truncate_string(file_name))
    package_name = truncate_string(file_name) if truncate_string(file_name) else file_name

    url = 'https://rdb.altlinux.org/api/package/package_info'
    params = {
        'name': package_name,
        'arch': 'x86_64',
        'source': 'false',
        'full': 'true'
    }
    headers = {
        'accept': 'application/json'
    }

    try:
        response = requests.get(url, params=params, headers=headers)
        response.raise_for_status()
        package_info = response.json()
        if package_info and 'packages' in package_info:
            return package_info['packages'][0]
    except requests.RequestException as e:
        logging.error(f"Ошибка запроса к API: {e}")
    return None

def extract_dates_from_log(log_content):
    """Извлекает первую и последнюю дату из содержимого лога в нужном формате."""
    # Регулярное выражение для извлечения даты и времени в формате 'May 16 02:15:35'
    date_pattern = r'\w{3} \d{1,2} \d{2}:\d{2}:\d{2}'
    dates = re.findall(date_pattern, log_content)

    if dates:
        # Первая дата из лога
        first_date_str = dates[0]
        # Последняя дата из лога
        last_date_str = dates[-1]

        # Преобразуем строки в datetime объекты
        current_year = datetime.now().year  # Для корректного парсинга в текущий год
        first_date = datetime.strptime(f"{first_date_str} {current_year}", '%b %d %H:%M:%S %Y')
        last_date = datetime.strptime(f"{last_date_str} {current_year}", '%b %d %H:%M:%S %Y')

        # Преобразуем datetime в ISO формат
        first_date_iso = first_date.isoformat()
        last_date_iso = last_date.isoformat()

        return first_date_iso, last_date_iso
    return None, None

def process_log_entry(log_entry, chunk_21, json_errors, es_client, index_name, redis_client):
    """Обработка одного лог-записи"""
    file_name = log_entry['filename']
    log_content = log_entry['log_text']
    log_id = log_entry['id']
    timestamp = datetime.now().isoformat()

    # Получаем все строки из chunk_21, где log_id совпадает
    errors_for_log = chunk_21[chunk_21['log_id'] == log_id]

    if errors_for_log.empty:
        logging.warning(f"Нет ошибок для лога с id {log_id}. Пропускаем.")
        return

    errors = []

    # Перебираем все ошибки для этого log_id
    for _, error_row in errors_for_log.iterrows():
        full_error = error_row['error_text']  # Сообщение ошибки из chunk_21
        cluster_id = error_row['cluster']  # Идентификатор кластера из chunk_21

        # Ищем описание ошибки в json_errors по id_cluster
        summary = "Неизвестно"

        # Перебираем json_errors (который является списком объектов)
        for key, error_obj in json_errors.items():
            if key == str(
                    cluster_id):  # Compare the key (string representation of the cluster_id) to the current cluster_id
                summary = error_obj['deepseek_name']
                break

        # Добавляем ошибку
        errors.append({
            "short_name": summary,
            "full_error": full_error
        })

    # Формируем данные пакета из названия файла
    package = truncate_string(file_name)
    package_info = fetch_package_info(file_name)

    if package_info:
        summary = package_info.get("summary", "Неизвестно")
        description = package_info.get("description", "Описание отсутствует")
        group = package_info.get("group", "Неизвестно")
        depends = package_info.get("depends", {}).get("require", [])
        first_date, last_date = extract_dates_from_log(log_content)

        document = {
            "log": log_content,
            "timestamp": timestamp,
            "programming_language": "C++",
            "errors": errors,
            "package_field": package,
            "package_summary": summary,
            "package_description": description,
            "package_group": group,
            "package_dependencies": depends,
            "first_log_date": first_date,  # Первая дата
            "last_log_date": last_date,    # Последняя дата
        }

        # Отправляем лог в Elasticsearch
        es_client.index(index=index_name, document=document)
        logging.debug(f"Отправлен лог из файла: {file_name}")

        redis_client.sadd("unique_packages", package)

        # Сохраняем уникальные ошибки и статистику по ним
        for error in errors:
            error_key = f"error:{error['short_name']}"

            # Добавляем ошибку в множество уникальных ошибок
            redis_client.sadd("unique_errors", error['short_name'])

            # Увеличиваем количество вхождений ошибки
            redis_client.hincrby(error_key, "count", 1)

            # Добавляем пакет в список пакетов для этой ошибки
            redis_client.sadd(f"{error_key}:packages", package)

        redis_client.sadd("processed_files", file_name)


def parse_logs_from_directory(csv_path, chunk_21_path, json_errors, es_client, index_name, redis_client):
    """Парсинг логов из CSV и обработка ошибок из chunk_21"""
    chunk_21 = read_chunk_21(chunk_21_path)

    with ThreadPoolExecutor(max_workers=5) as executor:  # Уменьшено количество потоков для экономии памяти
        futures = []
        for chunk in read_logs_csv(csv_path):
            logging.debug(f"Обрабатываем следующий чанк логов: {len(chunk)} записей.")
            for _, log_entry in chunk.iterrows():
                futures.append(
                    executor.submit(process_log_entry, log_entry, chunk_21, json_errors, es_client, index_name,
                                    redis_client))

        for future in futures:
            future.result()


def schedule_log_parsing():
    # Синхронизация логов (вы можете добавить другие источники, если нужно)
    sync_logs()

    # Список файлов Jupyter Notebook, которые нужно запустить
    notebooks = []  # Замените на свои файлы

    # Запуск Jupyter Notebook файлов
    run_notebooks(notebooks)
    # Используем директорию скрипта для определения пути к файлам
    script_dir = os.path.dirname(os.path.abspath(__file__))
    csv_path = os.path.join(script_dir, 'logs_with_labels_with_id.csv')
    chunk_21_path = os.path.join(script_dir, 'longformer_hdbscan_clusters.csv')
    json_errors = read_json_errors(os.path.join(script_dir, 'cluster_names_v2.json'))
    print(json_errors)

    es_client = Elasticsearch(
        ["http://localhost:9200"],
        basic_auth=("elastic", "elastic_password"),
        verify_certs=False
    )

    try:
        if es_client.ping():
            print("Подключение к Elasticsearch установлено.")
        else:
            print("Не удалось подключиться к Elasticsearch.")
    except AuthenticationException:
        print("Ошибка аутентификации. Проверьте имя пользователя и пароль.")
    except Exception as e:
        print(f"Ошибка при подключении к Elasticsearch: {e}")

    redis_client = redis.StrictRedis(host='localhost', port=6379, db=0, decode_responses=True)

    parse_logs_from_directory(csv_path, chunk_21_path, json_errors, es_client, "logs_index", redis_client)

    print("Логи успешно отправлены в Elasticsearch.")


scheduler = BackgroundScheduler()

scheduler.add_job(schedule_log_parsing, 'cron', minute='*')

schedule_log_parsing()

scheduler.start()

try:
    while True:
        pass
except (KeyboardInterrupt, SystemExit):
    scheduler.shutdown()
