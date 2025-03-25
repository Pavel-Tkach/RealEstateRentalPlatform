# Используем официальный Python образ как базовый
FROM python:3.9-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл с зависимостями в контейнер
COPY requirements.txt .

# Устанавливаем зависимости
RUN pip install --no-cache-dir -r requirements.txt

# Копируем весь проект в контейнер
COPY . .

# Открываем порт 8000 для доступа
EXPOSE 8000

# Используем Gunicorn для запуска приложения
CMD ["gunicorn", "-b", "0.0.0.0:8000", "recomendation-app:app"]
