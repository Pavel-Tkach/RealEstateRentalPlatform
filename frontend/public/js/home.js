async function fetchProperties() {
    try {
        const response = await fetch('http://localhost:8085/properties');
        const properties = await response.json();
        displayProperties(properties);
    } catch (error) {
        console.error('Error fetching properties:', error);
        document.getElementById('propertyList').innerHTML = `
            <div class="col-12">
                <div class="alert alert-danger text-center">Failed to load data.</div>
            </div>
        `;
    }
}

async function applyFilters() {
    try {
        const filterData = {
            title: document.getElementById('titleFilter').value || null,
            type: document.getElementById('typeFilter').value || null,
            locationDto: {
                address: document.getElementById('addressFilter').value || null,
                city: document.getElementById('cityFilter').value || null,
                state: document.getElementById('stateFilter').value || null,
                country: document.getElementById('countryFilter').value || null
            },
            startPricePerNight: document.getElementById('startPriceFilter').value
                ? parseFloat(document.getElementById('startPriceFilter').value) : null,
            endPricePerNight: document.getElementById('endPriceFilter').value
                ? parseFloat(document.getElementById('endPriceFilter').value) : null,
            free: document.getElementById('availableFilter').checked || null
        };

        if (Object.values(filterData.locationDto).every(val => val === null)) {
            filterData.locationDto = null;
        }

        const response = await fetch('http://localhost:8085/properties/recommendations', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(filterData)
        });

        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const filteredProperties = await response.json();
        displayProperties(filteredProperties);
    } catch (error) {
        console.error('Error applying filters:', error);
        document.getElementById('propertyList').innerHTML = `
            <div class="col-12">
                <div class="alert alert-danger text-center">Failed to apply filters.</div>
            </div>
        `;
    }
}

function displayProperties(properties) {
    const container = document.getElementById('propertyList');
    container.innerHTML = '';

    if (!properties || properties.length === 0) {
        container.innerHTML = `
            <div class="col-12">
                <div class="alert alert-info text-center">No properties match your filters.</div>
            </div>
        `;
        return;
    }

    const imageUrls = [
        "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1570129477492-45c003edd2be?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1541746972996-4e0b0f43e02a?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1497366811353-6870744d04b2?auto=format&fit=crop&w=800&q=80",
        "https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=800&q=80",
    ];

    properties.forEach((property, index) => {
        const imageUrl = imageUrls[index % imageUrls.length];
        const card = document.createElement('div');
        card.className = 'col-md-6 col-lg-4';

        card.innerHTML = `
        <div class="card h-100 shadow-sm property-card position-relative" style="cursor: pointer;">
            <img src="${imageUrl}" class="card-img-top" alt="${property.title}">
            <div class="card-body d-flex flex-column justify-content-between">
                <div>
                    <h5 class="card-title">${property.title}</h5>
                    <p class="card-text">${property.description}</p>
                    <p class="card-text"><strong>Type:</strong> ${property.type}</p>
                    <p class="card-text"><strong>Price per night:</strong> ${property.pricePerNight}$</p>
                    <p class="card-text text-muted small">
                        ${property.locationDto.address}, ${property.locationDto.city},
                        ${property.locationDto.state}, ${property.locationDto.country}
                    </p>
                    <span class="badge bg-${property.free ? 'success' : 'secondary'}">
                        ${property.free ? 'Available' : 'Unavailable'}
                    </span>
                </div>
                <div class="mt-3">
                    <button class="btn btn-primary w-100 book-btn" ${property.free ? '' : 'disabled'}>
                        Book Now
                    </button>
                    <div class="d-flex justify-content-end mt-2">
                        <button class="btn btn-sm btn-danger delete-property-btn" data-id="${property.id}">
                            <i class="bi bi-trash"></i> Delete
                        </button>
                    </div>
                </div>
            </div>
        </div>
        `;

        // Card click opens details
        card.querySelector('.card').addEventListener('click', () => {
            showPropertyDetails(property);
        });

        // Book button click opens booking modal
        const bookButton = card.querySelector('.book-btn');
        if (property.free) {
            bookButton.addEventListener('click', (e) => {
                e.stopPropagation();

                const token = localStorage.getItem('token');
                if (!token) {
                    alert("You must be logged in to book a property.");
                    return;
                }

                // Открываем модальное окно бронирования
                const bookingModal = new bootstrap.Modal(document.getElementById('bookingModal'));

                // Заполняем скрытое поле id свойства
                document.getElementById('bookingPropertyId').value = property.id;

                // Сбрасываем даты
                document.getElementById('startDate').value = '';
                document.getElementById('endDate').value = '';

                bookingModal.show();
            });
        }

        const deleteBtn = card.querySelector('.delete-property-btn');
        deleteBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            const propertyId = deleteBtn.getAttribute('data-id');
            deleteProperty(propertyId);
        });

        container.appendChild(card);
    });
}

function showPropertyDetails(property) {
    const detailEl = document.getElementById('propertyDetailsContent');
    detailEl.innerHTML = `
        <h5>${property.title}</h5>
        <p>${property.description}</p>
        <ul class="list-group mb-3">
            <li class="list-group-item"><strong>Type:</strong> ${property.type}</li>
            <li class="list-group-item"><strong>Price per night:</strong> ${property.pricePerNight}$</li>
            <li class="list-group-item"><strong>Status:</strong> ${property.free ? 'Available' : 'Unavailable'}</li>
            <li class="list-group-item"><strong>Location:</strong><br>
                ${property.locationDto.address}<br>
                ${property.locationDto.city}, ${property.locationDto.state}, ${property.locationDto.country}
            </li>
        </ul>
    `;

    const offcanvas = new bootstrap.Offcanvas(document.getElementById('propertyDetails'));
    offcanvas.show();
}

async function deleteProperty(propertyId) {
    const token = localStorage.getItem('token');
    if (!token) {
        alert("You should login into system");
        return;
    }

    const confirmed = confirm("Are you sure you want to delete this property?");
    if (!confirmed) return;

    try {
        const response = await fetch(`http://localhost:8085/properties/${propertyId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) throw new Error('Failed to delete property');

        alert('Property deleted successfully.');
        fetchProperties();
    } catch (error) {
        console.error('Error deleting property:', error);
        alert('You are not owner of this property, so you cannot delete it.');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    fetchProperties();

    const filterForm = document.getElementById('filterForm');
    filterForm.addEventListener('submit', (e) => {
        e.preventDefault();
        applyFilters();
    });

    filterForm.addEventListener('reset', () => {
        fetchProperties();
    });

    const addBtn = document.getElementById('addPropertyBtn');
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('email');

    if (token && email) {
        addBtn.classList.remove('d-none');

        addBtn.addEventListener('click', () => {
            const canvas = new bootstrap.Offcanvas('#addPropertyCanvas');
            canvas.show();
        });

        document.getElementById('propertyForm').addEventListener('submit', async (e) => {
            e.preventDefault();

            const newProperty = {
                title: document.getElementById('propertyTitle').value,
                description: document.getElementById('propertyDescription').value,
                type: document.getElementById('propertyType').value,
                locationDto: {
                    address: document.getElementById('locationAddress').value,
                    city: document.getElementById('locationCity').value,
                    state: document.getElementById('locationState').value,
                    country: document.getElementById('locationCountry').value
                },
                pricePerNight: parseFloat(document.getElementById('pricePerNight').value),
                free: true,
                ownerId: null // будет установлен на бэке через токен
            };

            try {
                const response = await fetch('http://localhost:8085/properties', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify(newProperty)
                });

                if (!response.ok) throw new Error('Failed to save property');

                alert('Property created successfully!');
                document.getElementById('propertyForm').reset();
                bootstrap.Offcanvas.getInstance(document.getElementById('addPropertyCanvas')).hide();
                fetchProperties();
            } catch (err) {
                console.error(err);
                alert('Error creating property. Check your input and try again.');
            }
        });
    }

    // Обработчик отправки формы бронирования
    document.getElementById('bookingForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const token = localStorage.getItem('token');
        if (!token) {
            alert("You must be logged in to book a property.");
            return;
        }

        const propertyId = document.getElementById('bookingPropertyId').value;
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;

        if (!startDate || !endDate || new Date(endDate) <= new Date(startDate)) {
            alert('Please select valid start and end dates.');
            return;
        }

        const bookingDto = {
            propertyId: propertyId,
            startDate: new Date(startDate).toISOString(),
            endDate: new Date(endDate).toISOString(),
        };

        try {
            const response = await fetch('http://localhost:8085/bookings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(bookingDto)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to create booking');
            }

            alert('Booking successfully created!');
            bootstrap.Modal.getInstance(document.getElementById('bookingModal')).hide();

            fetchProperties();
        } catch (error) {
            alert('Error during booking: ' + error.message);
        }
    });
});
