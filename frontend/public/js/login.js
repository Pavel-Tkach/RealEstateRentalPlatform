document.getElementById('loginForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const data = new URLSearchParams();
    data.append('client_id', 'frontend-client');  // Имя клиента в Keycloak
    data.append('grant_type', 'password');
    data.append('username', username);
    data.append('password', password);

    try {
        const response = await fetch('http://localhost:8083/realms/your-realm/protocol/openid-connect/token', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: data
        });

        if (!response.ok) {
            throw new Error('Ошибка авторизации');
        }

        const tokenData = await response.json();
        localStorage.setItem('access_token', tokenData.access_token);
        window.location.href = '/'; // перенаправляем на главную страницу
    } catch (error) {
        console.error('Ошибка входа:', error);
        document.getElementById('errorMsg').classList.remove('d-none');
    }
});
