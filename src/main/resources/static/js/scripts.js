// Function to initialize Google Places Autocomplete, Google Maps, and Street View
function initAutocomplete() {
    const propertySearchInput = document.getElementById('search-property');
    const mapElement = document.getElementById('property-map'); // Container for Google Maps
    const streetViewElement = document.getElementById('street-view'); // Container for Street View
    let map, panorama; // Declare map and panorama variables

    // Utility function to reset elements and show loading indicators
    const resetElements = () => {
        mapElement.classList.remove('show');
        streetViewElement.classList.remove('show');
        mapElement.innerHTML = '<p>Loading map...</p>';
        streetViewElement.innerHTML = '<p>Loading street view...</p>';
    };

    // Function to update Google Maps and Street View
    const updateGoogleMaps = (location, address) => {
        if (mapElement) {
            map = new google.maps.Map(mapElement, { center: location, zoom: 16 });
            new google.maps.Marker({ position: location, map, title: address });
            mapElement.classList.add('show'); // Show the map element
        }

        if (streetViewElement) {
            panorama = new google.maps.StreetViewPanorama(streetViewElement, {
                position: location,
                pov: { heading: 34, pitch: 10 },
                zoom: 1,
            });
            map.setStreetView(panorama); // Link the map with Street View
            streetViewElement.classList.add('show'); // Show the Street View element
        }
    };

    // Handle property selection from database-sourced elements
    const propertyDetails = document.querySelectorAll('[data-address]');
    propertyDetails.forEach(property => {
        property.addEventListener('click', event => {
            resetElements(); // Reset the elements to loading state
            const selectedAddress = event.target.getAttribute('data-address');
            if (!selectedAddress) return;

            const geocoder = new google.maps.Geocoder();
            geocoder.geocode({ address: selectedAddress }, (results, status) => {
                if (status === 'OK') {
                    const location = results[0].geometry.location;
                    updateGoogleMaps(location, selectedAddress); // Update Maps and Street View
                } else {
                    alert("Unable to fetch property details. Please try again.");
                }
            });
        });
    });

    // Handle autocomplete suggestions
    if (propertySearchInput) {
        const propertyAutocomplete = new google.maps.places.Autocomplete(propertySearchInput);
        propertyAutocomplete.addListener('place_changed', () => {
            resetElements(); // Reset the elements to loading state
            const place = propertyAutocomplete.getPlace();
            if (!place.geometry) {
                alert("No details available for the selected address.");
                return;
            }

            const location = place.geometry.location;
            updateGoogleMaps(location, place.formatted_address); // Update Maps and Street View
        });
    }
}

// Initialize the Google Places API on page load
window.addEventListener('load', initAutocomplete);
