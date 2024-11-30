// Google Maps initialization for the form and search pages
function initMap() {
    const dublin = { lat: 53.3498, lng: -6.2603 };
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 12,
        center: dublin,
        disableDefaultUI: true // Hide default controls for a cleaner background
    });

    // Marker for selected location
    const marker = new google.maps.Marker({ map: map });
    const addressInput = document.getElementById("address-input");
    if (addressInput) {
        const autocomplete = new google.maps.places.Autocomplete(addressInput);
        autocomplete.bindTo('bounds', map);

        autocomplete.addListener('place_changed', () => {
            const place = autocomplete.getPlace();
            if (!place.geometry) {
                alert("No details available for the selected place.");
                return;
            }

            // Center map and set marker
            if (place.geometry.viewport) {
                map.fitBounds(place.geometry.viewport);
            } else {
                map.setCenter(place.geometry.location);
                map.setZoom(15);
            }
            marker.setPosition(place.geometry.location);
            marker.setVisible(true);
        });
    }
}

// Handling form submission for property review
const reviewForm = document.getElementById('review-form');
if (reviewForm) {
    reviewForm.addEventListener('submit', function (e) {
        e.preventDefault();
        alert('Your review has been submitted.');
        reviewForm.reset();
    });
}

// Generate a unique identifier (UUID-like)
function generateID() {
    return 'xxxx-xxxx-4xxx-yxxx-xxxx-xxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0;
        const v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

// Handling property search form submission
const searchForm = document.getElementById('search-form');
if (searchForm) {
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const searchID = generateID();
        const searchLocation = document.getElementById('search-location').value;
        const searchEircode = document.getElementById('search-eircode').value;
        const minRating = document.getElementById('min-rating').value;

        const results = [
            { name: 'Property 1', location: 'Dublin', eircode: 'D07 FY97', rating: 5 },
            { name: 'Property 2', location: 'Cork', eircode: 'T12 XY56', rating: 4 },
            { name: 'Property 3', location: 'Dublin', eircode: 'D07 FY97', rating: 3 }
        ];

        const filteredResults = results.filter(property =>
            (property.location.includes(searchLocation) || property.eircode === searchEircode) &&
            (property.rating >= minRating)
        );

        const resultsList = document.getElementById('results-list');
        resultsList.innerHTML = '';

        filteredResults.forEach(property => {
            const listItem = `<li>${property.name} - Location: ${property.location} - Eircode: ${property.eircode} - Rating: ${property.rating} (Search ID: ${searchID})</li>`;
            resultsList.innerHTML += listItem;
        });

        if (filteredResults.length === 0) {
            resultsList.innerHTML = `<li>No properties found (Search ID: ${searchID})</li>`;
        }
    });
}

// Dynamically load property list for property page
window.onload = function () {
    const propertyList = document.getElementById('property-list');
    if (propertyList) {
        const properties = [
            { name: 'Property 1', location: 'Dublin', eircode: 'D07 FY97', hygiene: 5, crowding: 3, safety: 4, landlordRating: 5, comments: 'Great property!' },
            { name: 'Property 2', location: 'Cork', eircode: 'T12 XY56', hygiene: 4, crowding: 2, safety: 5, landlordRating: 4, comments: 'Nice location.' },
            { name: 'Property 3', location: 'Galway', eircode: 'H91 XY34', hygiene: 3, crowding: 4, safety: 4, landlordRating: 3, comments: 'Could be better.' }
        ];

        properties.forEach(property => {
            const row = `<tr>
                <td>${property.name}</td>
                <td>${property.location}</td>
                <td>${property.eircode}</td>
                <td>${property.hygiene}</td>
                <td>${property.crowding}</td>
                <td>${property.safety}</td>
                <td>${property.landlordRating}</td>
                <td>${property.comments}</td>
            </tr>`;
            propertyList.innerHTML += row;
        });
    }
};
