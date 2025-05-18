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
    if truncate_string(file_name):
        package_name = truncate_string(file_name)
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
        response.raise_for_status()  # Raise exception for 4xx/5xx responses
        package_info = response.json()
        if package_info and 'packages' in package_info:
            return package_info['packages'][0]
        else:
            logging.warning(f"Информация о пакете {package_name} не найдена.")
    except requests.RequestException as e:
        logging.error(f"Ошибка запроса к API для {package_name}: {e}")
        return None  # Возвращаем None в случае ошибки

def parse_logs_from_directory(directory, es_client, index_name, redis_client):
    with ThreadPoolExecutor(max_workers=10) as executor:
        futures = []
        for root, dirs, files in os.walk(directory):
            for file in files:
                if redis_client.sismember("processed_files", file):
                    logging.debug(f"Файл {file} уже обработан. Пропускаем.")
                    continue

                file_path = os.path.join(root, file)
                futures.append(executor.submit(process_file, file_path, file, es_client, index_name, redis_client))

        for future in futures:
            future.result()

import re
from datetime import datetime

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

    # Если информация о пакете не найдена, используем заглушку или оставляем пустые значения
    if package_info:
        summary = package_info.get("summary", "Неизвестно")
        description = package_info.get("description", "Описание отсутствует")
        group = package_info.get("group", "Неизвестно")
        depends = package_info.get("depends", {}).get("require", [])
    else:
        summary = "Информация о пакете не найдена"
        description = "Описание отсутствует"
        group = "Неизвестно"
        depends = []

    # Извлекаем первую и последнюю даты из лога
    first_date, last_date = extract_dates_from_log(log_content)

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
        "first_log_date": first_date,  # Первая дата
        "last_log_date": last_date,    # Последняя дата
    }

    try:
        es_client.index(index=index_name, document=document)
        logging.debug(f"Отправлен лог из файла: {file_path}")
    except Exception as e:
        logging.error(f"Ошибка при отправке документа в Elasticsearch: {e}")

    # Сохраняем название файла в Redis, чтобы избежать повторной обработки
    redis_client.sadd("processed_files", file)

    # Сохраняем уникальные пакеты и ошибки в Redis, даже если не удалось получить информацию о пакете
    redis_client.sadd("unique_packages", package)  # Сохраняем уникальные пакеты
    for error in errors:
        redis_client.sadd("unique_errors", error['short_name'])  # Сохраняем короткие имена ошибок

# Функция для планирования задания
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
