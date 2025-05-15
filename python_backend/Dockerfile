FROM python:3.13-slim

RUN apt-get update && apt-get install -y \
    libgomp1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

COPY ./symptom_classifier /app/symptom_classifier

COPY requirements.txt /app/requirements.txt

RUN --mount=type=cache,target=/root/.cache/pip pip install --no-cache-dir -r requirements.txt

ENV PYTHONPATH=/app
ENV BERT_MODEL_PATH=/app/symptom_classifier

CMD ["python", "app/main.py"]

EXPOSE 8086