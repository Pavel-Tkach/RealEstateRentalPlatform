FROM python:3.9

WORKDIR /app

COPY requirements.txt recomendation-app.py ./

RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 8000

CMD ["uvicorn", "recomendation-app:app", "--host", "0.0.0.0", "--port", "8000"]
