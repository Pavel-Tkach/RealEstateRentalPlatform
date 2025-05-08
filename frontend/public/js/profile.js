document.addEventListener('DOMContentLoaded', () => {
    const email = localStorage.getItem('email');
    const token = localStorage.getItem('token');

    if (!email || !token) {
        console.warn('Missing email or token, redirecting to login');
        keycloak.login();
        return;
    }

    fetch(`http://localhost:8085/users/search?email=${encodeURIComponent(email)}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(res => {
            if (!res.ok) throw new Error("Failed to fetch user profile");
            return res.json();
        })
        .then(user => {
            const userInfo = document.getElementById('userInfo');
            userInfo.textContent = `${user.firstname || ''} ${user.lastname || ''}`;
        })
        .catch(err => {
            console.error('Error loading user info:', err);
        });

    const tabs = {
        tabBookings: {
            button: document.getElementById('tabBookings'),
            content: document.getElementById('bookingsContent'),
            endpoint: 'http://localhost:8085/bookings',
            render: renderBookings
        },
        tabPayments: {
            button: document.getElementById('tabPayments'),
            content: document.getElementById('paymentsContent'),
            endpoint: 'http://localhost:8085/payments',
            render: renderPayments
        },
        tabCards: {
            button: document.getElementById('tabCards'),
            content: document.getElementById('cardsContent'),
            endpoint: 'http://localhost:8085/bankCards',
            render: renderCards
        }
    };

    // Переключение вкладок и загрузка данных
    Object.values(tabs).forEach(tab => {
        tab.button.addEventListener('click', () => {
            // Активная вкладка
            Object.values(tabs).forEach(t => {
                t.button.classList.remove('active');
                t.content.classList.add('d-none');
            });

            tab.button.classList.add('active');
            tab.content.classList.remove('d-none');

            // Загрузка данных
            fetch(tab.endpoint, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(res => {
                    if (!res.ok) throw new Error(`Failed to fetch from ${tab.endpoint}`);
                    return res.json();
                })
                .then(data => {
                    tab.render(tab.content, data);
                })
                .catch(err => {
                    console.error(`Error loading data from ${tab.endpoint}:`, err);
                    tab.content.innerHTML = `<div class="alert alert-danger">Failed to load data.</div>`;
                });
        });
    });

    function renderBookings(container, data) {
        if (!data.length) {
            container.innerHTML = `<div class="card p-4 shadow-sm"><h5>No bookings found.</h5></div>`;
            return;
        }

        container.innerHTML = data.map(booking => `
            <div class="card p-3 mb-3 shadow-sm">
                <p><strong>Property ID:</strong> ${booking.propertyId}</p>
                <p><strong>Start Date:</strong> ${new Date(booking.startDate).toLocaleDateString()}</p>
                <p><strong>End Date:</strong> ${new Date(booking.endDate).toLocaleDateString()}</p>
                <p><strong>Total Price:</strong> ${booking.totalPrice} USD</p>
                <p><strong>Status:</strong> ${booking.status}</p>
            </div>
        `).join('');
    }

    function renderPayments(container, data) {
        if (!data.length) {
            container.innerHTML = `<div class="card p-4 shadow-sm"><h5>No payments found.</h5></div>`;
            return;
        }

        container.innerHTML = data.map(payment => `
            <div class="card p-3 mb-3 shadow-sm">
                <p><strong>Amount:</strong> ${payment.amount} USD</p>
                <p><strong>Status:</strong> ${payment.status}</p>
                <p><strong>Payment Date:</strong> ${new Date(payment.paymentDate).toLocaleString()}</p>
            </div>
        `).join('');
    }

    function renderCards(container, data) {
        if (!data.length) {
            container.innerHTML = `<div class="card p-4 shadow-sm"><h5>No cards found.</h5></div>`;
            return;
        }

        container.innerHTML = data.map(card => `
            <div class="card p-3 mb-3 shadow-sm">
                <p><strong>Card Number:</strong> ${card.number}</p>
                <p><strong>Expiry Date:</strong> ${new Date(card.expiryDate).toLocaleDateString()}</p>
                <p><strong>Card Type:</strong> ${card.cardType}</p>
                <p><strong>Balance:</strong> ${card.balance} USD</p>
                <p><strong>Priority:</strong> ${card.priority ? 'Yes' : 'No'}</p>
            </div>
        `).join('');
    }

    // Инициировать первую вкладку при загрузке
    tabs.tabBookings.button.click();
});
