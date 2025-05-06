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

        // Обновление токена перед его истечением
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
        }, 60000); // проверка каждую минуту

    } else {
        console.log("Not authenticated");
    }
}).catch(err => {
    console.error("Keycloak init error", err);
});
