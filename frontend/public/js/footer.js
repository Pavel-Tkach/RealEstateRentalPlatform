document.addEventListener("DOMContentLoaded", () => {
    const year = new Date().getFullYear();
    document.querySelector(".custom-footer p").innerHTML = `&copy; ${year} RealEstateRentalPlatform. All rights reserved.`;
});
