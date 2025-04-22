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
        // Собираем данные формы в объект, соответствующий FilterDto
        const filterData = {
            title: document.getElementById('titleFilter').value || null,
            type: document.getElementById('typeFilter').value || null,
            locationDto: {
                address: document.getElementById('addressFilter').value || null,
                city: document.getElementById('cityFilter').value || null,
                state: document.getElementById('stateFilter').value || null,
                country: document.getElementById('countryFilter').value || null
            },
            startPricePerNight: document.getElementById('startPriceFilter').value ?
                parseFloat(document.getElementById('startPriceFilter').value) : null,
            endPricePerNight: document.getElementById('endPriceFilter').value ?
                parseFloat(document.getElementById('endPriceFilter').value) : null,
            free: document.getElementById('availableFilter').checked || null
        };

        // Удаляем null значения из locationDto, если все поля пустые
        if (Object.values(filterData.locationDto).every(val => val === null)) {
            filterData.locationDto = null;
        }

        // Отправляем POST-запрос с фильтрами
        const response = await fetch('http://localhost:8085/properties/recommendations', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(filterData)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

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

    properties.forEach(property => {
        const card = document.createElement('div');
        card.className = 'col-md-6 col-lg-4';

        card.innerHTML = `
            <div class="card h-100 shadow-sm property-card" style="cursor: pointer;">
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
                    </div>
                </div>
            </div>
        `;

        // Card click shows details
        card.querySelector('.card').addEventListener('click', () => {
            showPropertyDetails(property);
        });

        // Button click handles booking
        const bookButton = card.querySelector('.book-btn');
        if (property.free) {
            bookButton.addEventListener('click', (e) => {
                e.stopPropagation();
                alert(`You have booked: ${property.title}`);
            });
        }

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

document.addEventListener('DOMContentLoaded', () => {
    // Загружаем все свойства при загрузке страницы
    fetchProperties();

    // Обработчик формы фильтров
    const filterForm = document.getElementById('filterForm');
    filterForm.addEventListener('submit', (e) => {
        e.preventDefault();
        applyFilters();
    });

    // Обработчик сброса фильтров
    filterForm.addEventListener('reset', () => {
        // После сброса загружаем все свойства снова
        fetchProperties();
    });
});
