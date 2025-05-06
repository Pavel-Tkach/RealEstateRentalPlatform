const keycloak = new Keycloak({
    url: 'http://localhost:10000',
    realm: 'RealEstateRentalPlatform',
    clientId: 'frontend-client'
});

keycloak.init({ onLoad: 'check-sso', pkceMethod: 'S256', flow: 'standard' }).then(authenticated => {
    if (authenticated) {
        console.log("Authenticated");
        localStorage.setItem('token', keycloak.token);
        localStorage.setItem('refreshToken', keycloak.refreshToken);

        keycloak.loadUserInfo().then(userInfo => {
            const { email } = userInfo;
            if (email) {
                localStorage.setItem('email', email);
            }
            renderUserAvatar(userInfo);
        }).catch(err => {
            console.warn('Failed to load user info', err);
            renderUserAvatar();
        });

        setInterval(() => {
            keycloak.updateToken(60).then((refreshed) => {
                if (refreshed) {
                    console.log("Token refreshed");
                    localStorage.setItem('token', keycloak.token);
                }
            }).catch(() => {
                console.error("Failed to refresh token");
                keycloak.logout();
            });
        }, 60000);

    } else {
        console.log("Not authenticated");
        renderLoginButton();
    }
}).catch(err => {
    console.error("Keycloak init error", err);
    renderLoginButton();
});

function renderLoginButton() {
    const authArea = document.getElementById('authArea');
    if (authArea) {
        authArea.innerHTML = `
            <a href="#" onclick="keycloak.login()" class="btn btn-outline-primary">Login</a>
        `;
    }
}

function renderUserAvatar(userInfo = {}) {
    const { preferred_username = 'User', email = '' } = userInfo;
    const authArea = document.getElementById('authArea');
    if (authArea) {
        authArea.innerHTML = `
            <div class="dropdown">
                <a href="#" class="d-flex align-items-center text-decoration-none dropdown-toggle" id="userMenu" data-bs-toggle="dropdown" aria-expanded="false">
                    <img src="https://www.gravatar.com/avatar/?d=mp&s=32" alt="avatar" class="rounded-circle me-2" />
                    <span>${preferred_username}</span>
                </a>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenu">
                    <li><span class="dropdown-item-text">${email}</span></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="profile.html">My Profile</a></li>
                    <li><a class="dropdown-item" href="#" onclick="keycloak.logout()">Logout</a></li>
                </ul>
            </div>
        `;
    }
}
