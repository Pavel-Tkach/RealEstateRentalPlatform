from decimal import Decimal
from flask import Flask, request, jsonify
from typing import List, Optional

app = Flask(__name__)


class LocationDto:
    def __init__(self, address: str, city: str, state: str, country: str):
        self.address = address
        self.city = city
        self.state = state
        self.country = country

    def __repr__(self):
        return f"LocationDto({self.address}, {self.city}, {self.state}, {self.country})"


class PropertyDto:
    class PropertyType:
        APARTMENT = "APARTMENT"
        HOUSE = "HOUSE"
        OFFICE = "OFFICE"

    def __init__(self, id: str, title: str, description: str, type: str, locationDto: LocationDto, pricePerNight: Decimal, ownerId: str, free: bool):
        self.id = id
        self.title = title
        self.description = description
        self.type = type
        self.locationDto = locationDto
        self.pricePerNight = pricePerNight
        self.ownerId = ownerId
        self.free = free

    def __repr__(self):
        return f"PropertyDto({self.id}, {self.title}, {self.type}, {self.pricePerNight}, {self.free})"


class FilterDto:
    def __init__(self, title: Optional[str], type: Optional[str], locationDto: Optional[LocationDto],
                 startPricePerNight: Optional[Decimal], endPricePerNight: Optional[Decimal], free: Optional[bool],
                 propertiesDtos: List[PropertyDto]):
        self.title = title
        self.type = type
        self.locationDto = locationDto
        self.startPricePerNight = startPricePerNight
        self.endPricePerNight = endPricePerNight
        self.free = free
        self.propertiesDtos = propertiesDtos

    def __repr__(self):
        return f"FilterDto({self.title}, {self.type}, {self.locationDto}, {self.startPricePerNight}, {self.endPricePerNight}, {self.free})"


def filter_properties(filter_dto: FilterDto) -> List[PropertyDto]:
    filtered_properties = filter_dto.propertiesDtos

    if filter_dto.title:
        filtered_properties = [
            property for property in filtered_properties
            if filter_dto.title.lower() in property.title.lower()
        ]

    if filter_dto.type:
        filtered_properties = [
            property for property in filtered_properties
            if property.type == filter_dto.type
        ]

    if filter_dto.locationDto:
        filtered_properties = [
            property for property in filtered_properties
            if (filter_dto.locationDto.address and filter_dto.locationDto.address.lower() in property.locationDto.address.lower()) or
               (filter_dto.locationDto.city and filter_dto.locationDto.city.lower() in property.locationDto.city.lower()) or
               (filter_dto.locationDto.state and filter_dto.locationDto.state.lower() in property.locationDto.state.lower()) or
               (filter_dto.locationDto.country and filter_dto.locationDto.country.lower() in property.locationDto.country.lower())
        ]

    if filter_dto.startPricePerNight and filter_dto.endPricePerNight:
        filtered_properties = [
            property for property in filtered_properties
            if filter_dto.startPricePerNight <= property.pricePerNight <= filter_dto.endPricePerNight
        ]
    elif filter_dto.startPricePerNight:
        filtered_properties = [
            property for property in filtered_properties
            if property.pricePerNight >= filter_dto.startPricePerNight
        ]
    elif filter_dto.endPricePerNight:
        filtered_properties = [
            property for property in filtered_properties
            if property.pricePerNight <= filter_dto.endPricePerNight
        ]

    if filter_dto.free is not None:
        filtered_properties = [
            property for property in filtered_properties
            if property.free == filter_dto.free
        ]

    return filtered_properties


@app.route('/recommendations', methods=['POST'])
def filter_properties_endpoint():
    data = request.get_json()

    propertiesDtos = [
        PropertyDto(
            id=prop["id"],
            title=prop["title"],
            description=prop["description"],
            type=prop["type"],
            locationDto=LocationDto(**prop["locationDto"]) if prop.get("locationDto") else None,
            pricePerNight=Decimal(prop["pricePerNight"]),
            ownerId=prop["ownerId"],
            free=prop["free"]
        ) for prop in data.get("propertiesDtos", [])
    ]

    filter_dto = FilterDto(
        title=data.get("title"),
        type=data.get("type"),
        locationDto=LocationDto(**data.get("locationDto", {})) if data.get("locationDto") else None,
        startPricePerNight=Decimal(data["startPricePerNight"]) if data.get("startPricePerNight") else None,
        endPricePerNight=Decimal(data["endPricePerNight"]) if data.get("endPricePerNight") else None,
        free=data.get("free"),
        propertiesDtos=propertiesDtos
    )

    filtered_properties = filter_properties(filter_dto)

    return jsonify([{
        "id": prop.id,
        "title": prop.title,
        "description": prop.description,
        "type": prop.type,
        "locationDto": {
            "address": prop.locationDto.address,
            "city": prop.locationDto.city,
            "state": prop.locationDto.state,
            "country": prop.locationDto.country,
        },
        "pricePerNight": float(prop.pricePerNight),
        "free": prop.free
    } for prop in filtered_properties])


if __name__ == '__main__':
    app.run(debug=True, port=8000)
