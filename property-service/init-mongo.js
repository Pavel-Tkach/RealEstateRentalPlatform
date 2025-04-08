db = db.getSiblingDB('property-db');

db.property.insertMany([
    {
        "title": "Modern Apartment in City Center",
        "description": "A beautiful apartment in the heart of the city.",
        "type": "APARTMENT",
        "location": {
            "address": "123 Main Street",
            "city": "New York",
            "state": "NY",
            "country": "USA"
        },
        "pricePerNight": 100.0,
        "ownerId": "ad42555a-69fc-4aff-b218-64fc3bb567f7",
        "free": true
    },
    {
        "title": "Spacious Family House",
        "description": "A cozy house perfect for families.",
        "type": "HOUSE",
        "location": {
            "address": "456 Elm Street",
            "city": "Los Angeles",
            "state": "CA",
            "country": "USA"
        },
        "pricePerNight": 200.0,
        "ownerId": "ad42555a-69fc-4aff-b218-64fc3bb567f7",
        "free": false
    },
    {
        "title": "Stylish Office Space",
        "description": "A modern office with all necessary amenities.",
        "type": "OFFICE",
        "location": {
            "address": "789 Business Blvd",
            "city": "San Francisco",
            "state": "CA",
            "country": "USA"
        },
        "pricePerNight": 150.0,
        "ownerId": "ad42555a-69fc-4aff-b218-64fc3bb567f7",
        "free": true
    },
    {
        "title": "Charming Apartment in Historic District",
        "description": "A lovely apartment with vintage charm.",
        "type": "APARTMENT",
        "location": {
            "address": "321 Heritage Lane",
            "city": "Chicago",
            "state": "IL",
            "country": "USA"
        },
        "pricePerNight": 120.0,
        "ownerId": "ad42555a-69fc-4aff-b218-64fc3bb567f7",
        "free": false
    },
    {
        "title": "Luxury House with Pool",
        "description": "An upscale house featuring a private pool.",
        "type": "HOUSE",
        "location": {
            "address": "654 Sunset Drive",
            "city": "Miami",
            "state": "FL",
            "country": "USA"
        },
        "pricePerNight": 350.0,
        "ownerId": "ad42555a-69fc-4aff-b218-64fc3bb567f7",
        "free": true
    },
    {
        "title": "Creative Office in Downtown",
        "description": "A vibrant office space perfect for startups.",
        "type": "OFFICE",
        "location": {
            "address": "890 Innovation Ave",
            "city": "Austin",
            "state": "TX",
            "country": "USA"
        },
        "pricePerNight": 180.0,
        "ownerId": "b7a3d5e2-3f1e-4a6f-bd56-ec6c3f4a1b67",
        "free": false
    },
    {
        "title": "Cozy Apartment with City Views",
        "description": "A charming apartment with stunning views.",
        "type": "APARTMENT",
        "location": {
            "address": "222 Skyline Blvd",
            "city": "Seattle",
            "state": "WA",
            "country": "USA"
        },
        "pricePerNight": 130.0,
        "ownerId": "c0a1b8f2-7e3c-4fe4-b459-8e5b3c9e7a8a",
        "free": true
    },
    {
        "title": "Beautiful Family House",
        "description": "A spacious house located in a quiet neighborhood.",
        "type": "HOUSE",
        "location": {
            "address": "111 Maple Street",
            "city": "Denver",
            "state": "CO",
            "country": "USA"
        },
        "pricePerNight": 220.0,
        "ownerId": "g4c7e8d1-3b9f-4c1e-bf2d-7d3e6b4a2f0f",
        "free": false
    }
]);