/*!
 * Start Bootstrap - Grayscale v7.0.6
 * Licensed under MIT
 */

// Ensure all functionalities work dynamically on page load
window.addEventListener('DOMContentLoaded', event => {
    // Function to shrink the navbar when scrolling
    const navbarShrink = function () {
        const navbarCollapsible = document.body.querySelector('#mainNav'); // Get the navbar element
        if (!navbarCollapsible) return; // Exit if navbar does not exist

        // Add or remove the 'navbar-shrink' class based on scroll position
        if (window.scrollY === 0) {
            navbarCollapsible.classList.remove('navbar-shrink'); // Remove shrink class if at the top
        } else {
            navbarCollapsible.classList.add('navbar-shrink'); // Add shrink class when scrolling
        }
    };

    // Shrink the navbar immediately on page load
    navbarShrink();

    // Dynamically shrink the navbar as the user scrolls
    document.addEventListener('scroll', navbarShrink);

    // Activate Bootstrap's ScrollSpy plugin
    const mainNav = document.body.querySelector('#mainNav'); // Reference to the navbar
    if (mainNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#mainNav', // Target the navbar
            rootMargin: '0px 0px -40%', // Adjust scroll tracking
        });
    }

    // Close the responsive navbar menu when a link inside it is clicked
    const navbarToggler = document.body.querySelector('.navbar-toggler'); // Toggler button
    const responsiveNavItems = [].slice.call(
        document.querySelectorAll('#navbarResponsive .nav-link') // All navbar links
    );

    responsiveNavItems.forEach(function (responsiveNavItem) {
        responsiveNavItem.addEventListener('click', () => {
            // Check if the toggler is visible (menu is open)
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click(); // Close the menu
            }
        });
    });

    // Toggle the "Register" section functionality
    const registerSection = document.getElementById("register"); // Register section
    const showRegisterButton = document.getElementById("show-register"); // Register button in navbar
    const loginSection = document.getElementById("login"); // Login section (always visible)

    if (registerSection && showRegisterButton) {
        // Ensure the Register section starts hidden
        registerSection.style.display = "none";

        // Show the Register section when the button is clicked
        showRegisterButton.addEventListener("click", (e) => {
            e.preventDefault(); // Prevent default link behavior
            registerSection.style.display = "block"; // Show Register section
            if (loginSection) loginSection.style.display = "none"; // Hide Login section
        });
    }

    // Hide Property Details, Google Maps, and Street View until a property is found
    const propertyDetails = document.getElementById('property-details'); // Property Details section
    const propertyMap = document.getElementById('property-map'); // Google Maps section
    const streetView = document.getElementById('street-view'); // Street View section
    const searchButton = document.getElementById('search-button'); // Search button in the form

    // Ensure these sections are hidden initially
    if (propertyDetails) propertyDetails.style.display = "none";
    if (propertyMap) propertyMap.style.display = "none";
    if (streetView) streetView.style.display = "none";

    // Show these sections when a property is found
    if (searchButton) {
        searchButton.addEventListener("click", (e) => {
            e.preventDefault(); // Prevent form submission
            const propertyLocation = document.getElementById("property-location"); // Check for property data

            // Simulate property fetch and show the section if data exists
            if (propertyLocation && propertyLocation.getAttribute("data-location")) {
                if (propertyDetails) propertyDetails.style.display = "block"; // Show Property Details
                if (propertyMap) propertyMap.style.display = "block"; // Show Google Maps
                if (streetView) streetView.style.display = "block"; // Show Street View
            } else {
                alert("No property found for the entered address.");
            }
        });
    }
});

// Function to initialize Google Maps and Street View
function initGoogleMaps() {
    const mapElement = document.getElementById("property-map"); // Map container
    const streetViewElement = document.getElementById("street-view"); // Street View container
    const propertyLocationElement = document.getElementById("property-location"); // Location data

    if (!mapElement || !streetViewElement || !propertyLocationElement) {
        console.error("Required elements for Google Maps are missing.");
        return;
    }

    const address = propertyLocationElement.getAttribute("data-location"); // Get the address
    if (!address) {
        console.error("No address found in data-location attribute.");
        return;
    }

    const geocoder = new google.maps.Geocoder();
    geocoder.geocode({ address: address }, (results, status) => {
        if (status === "OK" && results[0]) {
            const location = results[0].geometry.location; // Get location coordinates

            // Initialize Google Maps
            const map = new google.maps.Map(mapElement, {
                center: location,
                zoom: 15,
            });

            // Add a marker to the map
            new google.maps.Marker({
                position: location,
                map: map,
                title: address, // Marker title
            });

            // Initialize Google Street View
            const panorama = new google.maps.StreetViewPanorama(streetViewElement, {
                position: location,
                pov: { heading: 34, pitch: 10 }, // Point of view
                zoom: 1,
            });
            map.setStreetView(panorama); // Link Street View with the map
        } else {
            console.error("Geocode failed due to:", status);
        }
    });
}

// Function to initialize Google Places Autocomplete
function initAutocomplete() {
    const propertySearchInput = document.getElementById('search-property'); // Search input field
    const mapElement = document.getElementById('property-map'); // Map container
    const streetViewElement = document.getElementById('street-view'); // Street View container

    if (!propertySearchInput) {
        console.error("Search input field not found.");
        return;
    }

    const propertyAutocomplete = new google.maps.places.Autocomplete(propertySearchInput);

    propertyAutocomplete.addListener('place_changed', function () {
        const place = propertyAutocomplete.getPlace(); // Get selected place details
        if (!place.geometry) {
            alert("No details available for the selected address.");
            return;
        }

        const { location } = place.geometry;

        // Initialize or update Google Maps
        const map = new google.maps.Map(mapElement, {
            center: location,
            zoom: 16,
        });

        // Add a marker to the map
        new google.maps.Marker({
            position: location,
            map: map,
            title: place.formatted_address,
        });

        // Initialize or update Google Street View
        new google.maps.StreetViewPanorama(streetViewElement, {
            position: location,
            pov: { heading: 34, pitch: 10 },
            zoom: 1,
        });
    });
}

// Initialize Google Maps and Autocomplete on page load
window.addEventListener("load", () => {
    initGoogleMaps(); // Load Google Maps
    initAutocomplete(); // Load Autocomplete
});
