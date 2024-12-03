// Wait for the DOM (Document Object Model) to fully load before running the script
window.addEventListener('DOMContentLoaded', event => {

    // This function handles shrinking the navbar when scrolling
    const navbarShrink = function () {
        const navbarCollapsible = document.body.querySelector('#mainNav'); // Get the navbar element
        if (!navbarCollapsible) return; // Exit if the navbar doesn't exist

        // Add or remove the 'navbar-shrink' class based on the scroll position
        if (window.scrollY === 0) {
            navbarCollapsible.classList.remove('navbar-shrink'); // Remove class if at the top
        } else {
            navbarCollapsible.classList.add('navbar-shrink'); // Add class when scrolling
        }
    };

    // Run the navbar shrinking logic on page load and whenever the user scrolls
    navbarShrink();
    document.addEventListener('scroll', navbarShrink);

    // Enable Bootstrap's ScrollSpy feature for highlighting navbar links based on scroll position
    const mainNav = document.body.querySelector('#mainNav');
    if (mainNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#mainNav', // Link the navbar
            rootMargin: '0px 0px -40%', // Adjust the scroll offset
        });
    }

    // Toggle the navbar menu for smaller screens (like mobile devices)
    const navbarToggler = document.body.querySelector('.navbar-toggler'); // Menu button
    const responsiveNavItems = [].slice.call(
        document.querySelectorAll('#navbarResponsive .nav-link') // All nav links
    );

    responsiveNavItems.forEach(responsiveNavItem => {
        // Close the menu when a nav link is clicked (only if it's open)
        responsiveNavItem.addEventListener('click', () => {
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click(); // Simulate a click to close the menu
            }
        });
    });

    // Handle switching between the Register and Login sections
    const registerSection = document.getElementById("register");
    const showRegisterButton = document.getElementById("show-register");
    const loginSection = document.getElementById("login");

    if (registerSection && showRegisterButton) {
        registerSection.style.display = "none"; // Hide the register section by default

        showRegisterButton.addEventListener("click", e => {
            e.preventDefault(); // Prevent the default link behavior
            registerSection.style.display = "block"; // Show the register section
            if (loginSection) loginSection.style.display = "none"; // Hide the login section
        });
    }

    // Add event listeners to the role selection buttons (Tenant or Landlord)
    document.querySelectorAll('.role-btn').forEach(button => {
        button.addEventListener('click', function () {
            // Set the value of the hidden input to the selected role
            document.getElementById('role').value = this.getAttribute('data-role');

            // Hide the buttons after one is clicked
            document.querySelectorAll('.role-btn').forEach(btn => btn.style.display = 'none');

            // Log the selected role (useful for debugging)
            console.log("Selected role:", this.getAttribute('data-role'));
        });
    });

    // Ensure the property details, map, and Street View are always visible
    const propertyDetails = document.getElementById('property-details');
    const propertyMap = document.getElementById('property-map');
    const streetView = document.getElementById('street-view');

    if (propertyDetails) propertyDetails.style.display = "block";
    if (propertyMap) propertyMap.style.display = "block";
    if (streetView) streetView.style.display = "block";

    // Search button functionality for finding properties
    const searchButton = document.getElementById('search-button');
    if (searchButton) {
        searchButton.addEventListener("click", e => {
            const addressInput = document.getElementById('search-property');
            if (addressInput && addressInput.value.trim()) {
                console.log("Submitting address:", addressInput.value);

                // Ensure the map and details sections are visible
                if (propertyDetails) propertyDetails.style.display = "block";
                if (propertyMap) propertyMap.style.display = "block";
                if (streetView) streetView.style.display = "block";

                // Submit the form
                document.getElementById('search-form').submit();
            } else {
                e.preventDefault(); // Stop form submission if input is empty
                alert("Please enter a valid address."); // Alert the user
            }
        });
    }

    // Initialize Google Maps and Autocomplete when the page loads
    initGoogleMaps(); // Set up Google Maps and Street View
    initAutocomplete(); // Add autocomplete functionality to input fields
});

// Initialize Google Maps and Street View with the property's address
function initGoogleMaps() {
    const mapElement = document.getElementById("property-map");
    const streetViewElement = document.getElementById("street-view");
    const propertyLocationElement = document.getElementById("property-location");

    // Check if all required elements are present
    if (!mapElement || !streetViewElement || !propertyLocationElement) {
        console.error("Missing required elements for Google Maps or Street View.");
        return;
    }

    const address = propertyLocationElement.textContent.trim(); // Get the address
    console.log("Initializing Google Maps with address:", address);

    if (address) {
        updateGoogleMaps(address); // Pass the address to the map function
    } else {
        console.error("No address available in Property Details.");
    }
}

// Update Google Maps and Street View with a given address
function updateGoogleMaps(address) {
    console.log("Updating Google Maps with address:", address);

    const mapElement = document.getElementById("property-map");
    const streetViewElement = document.getElementById("street-view");

    const geocoder = new google.maps.Geocoder();
    geocoder.geocode({ address: address }, (results, status) => {
        if (status === "OK" && results[0]) {
            const location = results[0].geometry.location; // Get the location
            console.log("Location found:", location);

            // Initialize Google Maps
            const map = new google.maps.Map(mapElement, {
                center: location,
                zoom: 15,
            });

            // Add a marker on the map
            new google.maps.Marker({
                position: location,
                map: map,
                title: address,
            });

            // Initialize Street View
            const panorama = new google.maps.StreetViewPanorama(streetViewElement, {
                position: location,
                pov: { heading: 34, pitch: 10 },
                zoom: 1,
            });

            map.setStreetView(panorama);
        } else {
            console.error("Failed to find location:", status);
            if (status === "ZERO_RESULTS") alert(`Address not found: ${address}`);
        }
    });
}

// Add Google Places Autocomplete to input fields
function initAutocomplete() {
    const addressFields = document.querySelectorAll('#search-property, #address');

    addressFields.forEach(field => {
        const autocomplete = new google.maps.places.Autocomplete(field);

        autocomplete.addListener('place_changed', function () {
            const place = autocomplete.getPlace();
            if (!place.geometry) {
                alert("No details available for the selected address.");
                return;
            }
            console.log("Selected place:", place);
        });
    });
}
