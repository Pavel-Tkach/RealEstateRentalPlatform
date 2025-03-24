import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
import numpy as np
import pandas as pd
from typing import List, Optional
from pydantic import BaseModel
from fastapi import FastAPI
import uvicorn
from sklearn.preprocessing import StandardScaler, LabelEncoder, MinMaxScaler
from sklearn.model_selection import train_test_split

# Определение DTO
class LocationDto(BaseModel):
    address: str
    city: str
    state: str
    country: str

class PropertyDto(BaseModel):
    id: Optional[str] = None
    title: str
    description: str
    type: str
    locationDto: LocationDto
    pricePerNight: float
    ownerId: Optional[str] = None
    free: Optional[bool] = True

class FilterDto(BaseModel):
    title: Optional[str] = None
    type: Optional[str] = None
    locationDto: Optional[LocationDto] = None
    pricePerNight: Optional[float] = None
    free: Optional[bool] = None
    propertiesDtos: Optional[List[PropertyDto]] = None

# Генерация случайного датасета недвижимости
np.random.seed(42)
data = pd.DataFrame({
    'pricePerNight': np.random.uniform(30, 500, 1000),
    'free': np.random.choice([True, False], 1000),
    'type': np.random.choice(['APARTMENT', 'HOUSE', 'OFFICE'], 1000),
})

# Кодирование типа недвижимости
label_encoder = LabelEncoder()
data['type'] = label_encoder.fit_transform(data['type'])

# Разделение на признаки и метки
X = data.drop(columns=['pricePerNight'])
y = data['pricePerNight']

# Нормализация данных
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# Используем MinMaxScaler для y, чтобы избежать отрицательных значений
y_scaler = MinMaxScaler()
y_scaled = y_scaler.fit_transform(y.values.reshape(-1, 1))

# Разделение на тренировочную и тестовую выборки
X_train, X_test, y_train, y_test = train_test_split(X_scaled, y_scaled, test_size=0.2, random_state=42)

# Создание модели нейронной сети
model = keras.Sequential([
    layers.Dense(64, activation='relu', input_shape=(X_train.shape[1],)),
    layers.Dense(32, activation='relu'),
    layers.Dense(1)  # Один выходной нейрон для предсказания цены за ночь
])

# Компиляция модели
model.compile(optimizer='adam', loss='mse', metrics=['mae'])

# Обучение модели
model.fit(X_train, y_train, validation_data=(X_test, y_test), epochs=50, batch_size=32)

# FastAPI для обработки запросов
app = FastAPI()

@app.post("/recommendations")
def get_recommendations(filter_dto: FilterDto):
    if not filter_dto.propertiesDtos:
        return filter_dto  # Если нет недвижимости, возвращаем запрос без изменений

    properties_df = pd.DataFrame([
        {**p.model_dump(), 'locationDto': p.locationDto.model_dump()} for p in filter_dto.propertiesDtos
    ])

    # Обработка неизвестных типов
    known_types = set(label_encoder.classes_)
    properties_df['type'] = properties_df['type'].apply(lambda x: x if x in known_types else 'APARTMENT')
    properties_df['type'] = label_encoder.transform(properties_df['type'])

    if filter_dto.type:
        type_encoded = label_encoder.transform([filter_dto.type])[0]
        properties_df = properties_df[properties_df['type'] == type_encoded]
    if filter_dto.pricePerNight:
        properties_df = properties_df[properties_df['pricePerNight'] <= filter_dto.pricePerNight]
    if filter_dto.free is not None:
        properties_df = properties_df[properties_df['free'] == filter_dto.free]

    # Определение признаков в том же порядке, что и при обучении
    feature_columns = ['free', 'type']  # Задаем в нужном порядке, как в обучении
    for col in feature_columns:
        if col not in properties_df:
            properties_df[col] = 0  # Добавляем отсутствующие колонки

    properties_df = properties_df[feature_columns]  # Сортируем колонки в нужном порядке
    properties_scaled = scaler.transform(properties_df)

    # Предсказание и обратное преобразование цены
    properties_df['predicted_price'] = y_scaler.inverse_transform(model.predict(properties_scaled)).flatten()
    recommended_properties = properties_df.sort_values(by='predicted_price', ascending=True).to_dict(orient="records")

    return FilterDto(
        title=filter_dto.title,
        type=filter_dto.type,
        locationDto=filter_dto.locationDto,
        pricePerNight=filter_dto.pricePerNight,
        free=filter_dto.free,
        propertiesDtos=[PropertyDto(**prop) for prop in recommended_properties]
    )

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
