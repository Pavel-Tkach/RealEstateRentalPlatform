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
            document.getElementById('profileContent').innerHTML = `
            <h5>Welcome, ${user.firstname || ''} ${user.lastname || ''}</h5>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>User ID:</strong> ${user.id}</p>
        `;
        })
        .catch(err => {
            console.error('Error loading profile:', err);
            document.getElementById('profileContent').innerHTML = `
            <div class="alert alert-danger">Unable to load profile information.</div>
        `;
        });
});
