import os
import logging
from elasticsearch import Elasticsearch
from datetime import datetime
from elasticsearch.exceptions import AuthenticationException
import redis
import requests
import re
from apscheduler.schedulers.background import BackgroundScheduler
from concurrent.futures import ThreadPoolExecutor

logging.basicConfig(level=logging.DEBUG)

def read_log_from_file(file_path):
    """Чтение лог-файлов с использованием построчного чтения для экономии памяти."""
    try:
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as file:
            return file.read()  # Читаем весь файл целиком
    except UnicodeDecodeError:
        with open(file_path, 'r', encoding='latin1', errors='ignore') as file:
            return file.read()  # Читаем весь файл целиком

def truncate_string(input_string):
    return re.sub(r'-\d.*', '', input_string)

def fetch_package_info(file_name):
    """Обработка запросов к API параллельно, используя ThreadPoolExecutor."""
    print(file_name)
    print(truncate_string(file_name))
    if  truncate_string(file_name):
        package_name =  truncate_string(file_name)
    else:
        package_name = file_name

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


def parse_logs_from_directory(directory, es_client, index_name, redis_client):
    # Используем пул потоков для параллельной обработки файлов
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for root, dirs, files in os.walk(directory):
            for file in files:
                # if redis_client.sismember("processed_files", file):
                #     logging.debug(f"Файл {file} уже обработан. Пропускаем.")
                #     continue

                file_path = os.path.join(root, file)
                futures.append(executor.submit(process_file, file_path, file, es_client, index_name, redis_client))

        for future in futures:
            future.result()


def process_file(file_path, file, es_client, index_name, redis_client):
    log_content = read_log_from_file(file_path)
    timestamp = datetime.now().isoformat()
    language = "C++"

    errors = [
        {"short_name": "composer_error", "full_error": "\n".join(log_content.splitlines()[:3])},
        {"short_name": "rpm_error", "full_error": "\n".join(log_content.splitlines()[:3])},
        {"short_name": "command_nonzero", "full_error": "\n".join(log_content.splitlines()[:3])}
    ]

    package = truncate_string(file)
    package_info = fetch_package_info(file)
    if package_info:
        summary = package_info.get("summary", "Неизвестно")
        description = package_info.get("description", "Описание отсутствует")
        group = package_info.get("group", "Неизвестно")
        depends = package_info.get("depends", {}).get("require", [])

        document = {
            "log": log_content,
            "timestamp": timestamp,
            "programming_language": language,
            "errors": errors,
            "package_field": package,
            "package_summary": summary,
            "package_description": description,
            "package_group": group,
            "package_dependencies": depends,
        }

        es_client.index(index=index_name, document=document)
        logging.debug(f"Отправлен лог из файла: {file_path}")

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

        redis_client.sadd("processed_files", file)


def schedule_log_parsing():
    directory_path = os.path.expanduser('~/Desktop/latest/error')

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

    parse_logs_from_directory(directory_path, es_client, "logs_index", redis_client)

    print("Логи успешно отправлены в Elasticsearch.")


scheduler = BackgroundScheduler()

scheduler.add_job(schedule_log_parsing, 'cron', hour=0, minute=1, second=0)

schedule_log_parsing()

scheduler.start()

try:
    while True:
        pass
except (KeyboardInterrupt, SystemExit):
    scheduler.shutdown()
