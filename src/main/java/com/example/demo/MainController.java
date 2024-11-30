package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private PropertyRepository propertyRepository;

    /**
     * Serve the index page.
     * This page acts as the main entry point for the application.
     * @return "index.html" located in the templates directory.
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * Serve the forms page where users can submit property-related data.
     * This is useful for tenants submitting property reviews or landlords adding comments.
     * @return "forms.html".
     */
    @GetMapping("/forms")
    public String showFormsPage() {
        return "forms";
    }

    /**
     * Handle property submission from tenants.
     * Stores tenant-provided data in the database.
     * @param name Tenant's name.
     * @param address Property address.
     * @param hygiene Hygiene rating.
     * @param safety Safety rating.
     * @param conservation Conservation rating.
     * @param price Price rating.
     * @param landlord Landlord rating.
     * @param comments Tenant's comments about the property.
     * @return Redirects to the tenant's main page after submission.
     */
    @PostMapping("/submitTenant")
    public String submitTenant(@RequestParam String name,
                               @RequestParam String address,
                               @RequestParam int hygiene,
                               @RequestParam int safety,
                               @RequestParam int conservation,
                               @RequestParam int price,
                               @RequestParam int landlord,
                               @RequestParam String comments) {
        try {
            Property property = new Property();
            property.setTenantId("tenant-" + UUID.randomUUID()); // Generate a unique tenant ID.
            property.setPropertyName(name);
            property.setLocation(address);
            property.setHygiene(hygiene);
            property.setSafety(safety);
            property.setCrowding(conservation);
            property.setLandlordRating(landlord);
            property.setComments(comments); // Add tenant's comments.

            propertyRepository.save(property); // Save to the database.

            return "redirect:/tenant"; // Redirect to the tenant's page.
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Redirect to an error page if an exception occurs.
        }
    }

    /**
     * Handle property comments from landlords.
     * Updates a property with landlord-specific comments.
     * @param address The address of the property to update.
     * @param comment The landlord's comment about the property.
     * @param model The model used to pass data to the view.
     * @return Renders the landlord page with the updated data.
     */
    @PostMapping("/submitLandlord")
    public String submitLandlord(@RequestParam String address,
                                 @RequestParam String comment,
                                 Model model) {
        try {
            Property property = propertyRepository.findByLocation(address);

            if (property == null) {
                model.addAttribute("error", "No property found for the provided address.");
                return "landlord";
            }

            property.setLandlordId("landlord-" + UUID.randomUUID()); // Generate a unique landlord ID.
            property.setLandlordComment(comment); // Add landlord's comment.

            propertyRepository.save(property); // Save to the database.

            model.addAttribute("property", property);
            model.addAttribute("success", "Comment added successfully.");
            return "landlord"; // Return to the landlord's page with success message.
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while submitting the comment.");
            return "landlord"; // Return to the landlord page with an error message.
        }
    }

    /**
     * Handle property search for tenants.
     * Retrieves property details based on the address entered by the tenant.
     * @param address The address to search for.
     * @param model The model used to pass data to the view.
     * @return Renders the tenant page with the search results.
     */
    @GetMapping("/tenantSearch")
    public String tenantSearch(@RequestParam String address, Model model) {
        try {
            Property property = propertyRepository.findByLocation(address);

            if (property != null) {
                model.addAttribute("property", property);
            } else {
                model.addAttribute("error", "No property found for the provided address.");
            }

            return "tenant"; // Return to the tenant's page with results or an error message.
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while searching for the property.");
            return "tenant";
        }
    }

    /**
     * Handle property search for landlords.
     * Retrieves property details based on the address entered by the landlord.
     * @param address The address to search for.
     * @param model The model used to pass data to the view.
     * @return Renders the landlord page with the search results.
     */
    @GetMapping("/landlordSearch")
    public String landlordSearch(@RequestParam String address, Model model) {
        try {
            Property property = propertyRepository.findByLocation(address);

            if (property != null) {
                model.addAttribute("property", property);
            } else {
                model.addAttribute("error", "No property found for the provided address.");
            }

            return "landlord"; // Return to the landlord's page with results or an error message.
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while searching for the property.");
            return "landlord";
        }
    }

    /**
     * Fetch and display all properties.
     * Retrieves all property entries from the database.
     * @param model The model used to pass data to the view.
     * @return Renders the "properties.html" page with all properties listed.
     */
    @GetMapping("/properties")
    public String showProperties(Model model) {
        List<Property> properties = propertyRepository.findAll();
        model.addAttribute("properties", properties);
        return "properties";
    }

    /**
     * Serve the landlord page.
     * @return Renders "landlord.html".
     */
    @GetMapping("/landlord")
    public String showLandlordPage() {
        return "landlord";
    }

    /**
     * Serve the tenant page.
     * @return Renders "tenant.html".
     */
    @GetMapping("/tenant")
    public String showTenantPage() {
        return "tenant";
    }

}
