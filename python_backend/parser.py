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


# Запуск Jupyter Notebook для записи логов в JSON
def run_notebook():
    try:
        # Запуск файла .ipynb с помощью Jupyter
        subprocess.run(['jupyter', 'nbconvert', '--to', 'notebook', '--execute', 'notebook.ipynb'],
                       check=True)
        print("Jupyter Notebook выполнен успешно.")
    except subprocess.CalledProcessError as e:
        print(f"Ошибка при запуске Jupyter Notebook: {e}")


if __name__ == "__main__":
    # Синхронизация логов
    sync_logs()

    # Запуск Jupyter Notebook для обработки логов
    run_notebook()
