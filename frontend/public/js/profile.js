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

    Object.values(tabs).forEach(tab => {
        tab.button.addEventListener('click', () => {
            // Скрываем все вкладки
            Object.values(tabs).forEach(t => {
                t.button.classList.remove('active');
                t.content.classList.add('d-none');
            });

            // Показываем текущую вкладку
            tab.button.classList.add('active');
            tab.content.classList.remove('d-none');

            // Загружаем данные
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
        <div class="card p-3 mb-3 shadow-sm" data-booking-id="${booking.id}">
            <p><strong>Property ID:</strong> ${booking.propertyId}</p>
            <p><strong>Start Date:</strong> ${new Date(booking.startDate).toLocaleDateString()}</p>
            <p><strong>End Date:</strong> ${new Date(booking.endDate).toLocaleDateString()}</p>
            <p><strong>Total Price:</strong> ${booking.totalPrice} USD</p>
            <p><strong>Status:</strong> ${booking.status}</p>
            ${booking.status === 'AWAIT_PAYMENT' ? `<button class="btn btn-primary pay-btn">Pay</button>` : ''}
        </div>
    `).join('');

        // Назначаем обработчики кнопкам оплаты
        container.querySelectorAll('.pay-btn').forEach(button => {
            button.addEventListener('click', function () {
                const bookingCard = this.closest('[data-booking-id]');
                const bookingId = bookingCard.getAttribute('data-booking-id');
                payForBooking(bookingId);
            });
        });
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
        // Получаем контейнер списка карт внутри cardsContent
        const listContainer = container.querySelector('#cardsList');

        if (!data.length) {
            listContainer.innerHTML = `<div class="card p-4 shadow-sm"><h5>No cards found.</h5></div>`;
            return;
        }

        listContainer.innerHTML = data.map(card => `
        <div class="card p-3 mb-3 shadow-sm position-relative" data-card-id="${card.id}">
            <p><strong>Card Number:</strong> ${card.number}</p>
            <p><strong>Expiry Date:</strong> ${new Date(card.expiryDate).toLocaleDateString()}</p>
            <p><strong>Card Type:</strong> ${card.cardType}</p>
            <p><strong>Balance:</strong> ${card.balance} USD</p>
            <p><strong>Priority:</strong> ${card.priority ? 'Yes' : 'No'}</p>
            <button class="btn btn-danger btn-sm position-absolute bottom-0 end-0 m-3 delete-card-btn">Delete</button>
        </div>
    `).join('');

        // Назначаем обработчики кнопкам удаления
        listContainer.querySelectorAll('.delete-card-btn').forEach(button => {
            button.addEventListener('click', function () {
                const cardElement = this.closest('[data-card-id]');
                const cardId = cardElement.getAttribute('data-card-id');
                if (confirm('Are you sure you want to delete this card?')) {
                    deleteCard(cardId, container);
                }
            });
        });
    }

    function payForBooking(bookingId) {
        fetch(`http://localhost:8085/bookings/${bookingId}/payments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({}) // Пустой DTO, как указано
        })
            .then(res => {
                if (!res.ok) throw new Error('Failed to process payment');
                alert('Payment successful!');
                // Обновим список бронирований
                return fetch('http://localhost:8085/bookings', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
            })
            .then(res => res.json())
            .then(data => {
                renderBookings(document.getElementById('bookingsContent'), data);
            })
            .catch(err => {
                console.error('Error during payment:', err);
                alert('Failed to process payment.');
            });
    }

    function deleteCard(cardId, container) {
        fetch(`http://localhost:8085/bankCards/${cardId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(res => {
                if (!res.ok) throw new Error('Failed to delete card');
                return fetch('http://localhost:8085/bankCards', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
            })
            .then(res => res.json())
            .then(data => {
                renderCards(container, data);
            })
            .catch(err => {
                console.error('Error deleting card:', err);
                alert('Failed to delete the card.');
            });
    }

    const addCardForm = document.getElementById('addCardForm');
    const addCardModal = new bootstrap.Modal(document.getElementById('addCardModal'));

    document.getElementById('addCardBtn').addEventListener('click', () => {
        addCardForm.reset();
        addCardModal.show();
    });

    addCardForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const newCard = {
            id: null,
            number: document.getElementById('cardNumber').value,
            expiryDate: new Date(document.getElementById('expiryDate').value).toISOString(),
            cardType: document.getElementById('cardType').value,
            cvc: document.getElementById('cvc').value,
            balance: parseFloat(document.getElementById('balance').value),
            priority: document.getElementById('priority').checked
        };

        fetch('http://localhost:8085/bankCards', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(newCard)
        })
            .then(res => {
                if (!res.ok) throw new Error('Failed to add card');
                return res.json();
            })
            .then(() => {
                addCardModal.hide();
                return fetch('http://localhost:8085/bankCards', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
            })
            .then(res => res.json())
            .then(data => {
                renderCards(document.getElementById('cardsContent'), data);
            })
            .catch(err => {
                console.error('Error adding card:', err);
                alert('Failed to add card');
            });
    });


    // Запускаем первую вкладку по умолчанию
    tabs.tabBookings.button.click();
});
