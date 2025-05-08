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
            content: document.getElementById('bookingsContent')
        },
        tabPayments: {
            button: document.getElementById('tabPayments'),
            content: document.getElementById('paymentsContent')
        },
        tabCards: {
            button: document.getElementById('tabCards'),
            content: document.getElementById('cardsContent')
        }
    };

    Object.values(tabs).forEach(tab => {
        tab.button.addEventListener('click', () => {
            // Убрать active и скрыть контент
            Object.values(tabs).forEach(t => {
                t.button.classList.remove('active');
                t.content.classList.add('d-none');
            });

            // Установить активную вкладку и показать её контент
            tab.button.classList.add('active');
            tab.content.classList.remove('d-none');
        });
    });
});
