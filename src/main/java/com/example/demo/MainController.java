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
    @Autowired
    private UserRepository userRepository;

    // Página inicial
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    // Formulários para envio de dados
    @GetMapping("/forms")
    public String showFormsPage() {
        return "forms";
    }

    // tenant review submission
    @PostMapping("/submitTenant")
    public String submitTenant(@RequestParam String name,
                               @RequestParam String address,
                               @RequestParam int hygiene,
                               @RequestParam int safety,
                               @RequestParam int conservation,
                               @RequestParam int crowding, // Moved before price
                               @RequestParam int price,
                               @RequestParam int landlord,
                               @RequestParam String comments) {
        try {
            System.out.println("Submitting tenant review for property: " + address);

            // Create a new property and set all tenant-provided details
            Property property = new Property();
            property.setTenantId("tenant-" + UUID.randomUUID());
            property.setPropertyName(name);
            property.setLocation(address);
            property.setHygiene(hygiene);
            property.setSafety(safety);
            property.setConservation(conservation);
            property.setCrowding(crowding); // Map crowding
            property.setPrice(price);
            property.setLandlordRating(landlord);
            property.setComments(comments);

            // Save property to the database
            propertyRepository.save(property);
            System.out.println("Tenant review saved successfully for address: " + address);

            return "redirect:/tenant";
        } catch (Exception e) {
            System.err.println("Error while submitting tenant review: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    // landlord comments submission
    @PostMapping("/submitLandlord")
    public String submitLandlord(@RequestParam String address,
                                 @RequestParam String comment,
                                 Model model) {
        try {
            System.out.println("Submitting landlord comment for address: " + address);

            Property property = propertyRepository.findByLocationIgnoreCase(address);

            if (property == null) {
                System.out.println("No property found for address: " + address);
                model.addAttribute("error", "No property found for the provided address.");
                return "landlord";
            }

            property.setLandlordId("landlord-" + UUID.randomUUID());
            property.setLandlordComment(comment);

            propertyRepository.save(property);
            System.out.println("Landlord comment saved successfully for property: " + property.getPropertyName());

            model.addAttribute("property", property);
            model.addAttribute("success", "Comment added successfully.");
            return "landlord";
        } catch (Exception e) {
            System.err.println("Error while submitting landlord comment: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while submitting the comment.");
            return "landlord";
        }
    }

    // tenant search
    @GetMapping("/tenantSearch")
    public String tenantSearch(@RequestParam String address, Model model) {
        try {
            System.out.println("Tenant searching for address: " + address);

            Property property = propertyRepository.findByLocationIgnoreCase(address);

            if (property != null) {
                System.out.println("Property found for tenant: " + property.getPropertyName());
                model.addAttribute("property", property);
                model.addAttribute("location", property.getLocation());
            } else {
                System.out.println("No property found for address: " + address);
                model.addAttribute("error", "No property found for the provided address.");
            }

            return "tenant";
        } catch (Exception e) {
            System.err.println("Error while searching property for tenant: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while searching for the property.");
            return "tenant";
        }
    }

    // landlord search
    @GetMapping("/landlordSearch")
    public String landlordSearch(@RequestParam String address, Model model) {
        try {
            System.out.println("Landlord searching for address: " + address);

            List<Property> properties = propertyRepository.findByLocationContainingIgnoreCase(address);

            if (!properties.isEmpty()) {
                System.out.println("Property found for landlord: " + properties.get(0).getPropertyName());
                model.addAttribute("property", properties.get(0));
                model.addAttribute("location", properties.get(0).getLocation());
            } else {
                System.out.println("No properties found for address: " + address);
                model.addAttribute("error", "No property found for the provided address.");
            }

            return "landlord";
        } catch (Exception e) {
            System.err.println("Error while searching property for landlord: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while searching for the property.");
            return "landlord";
        }
    }

    // list all properties
    @GetMapping("/properties")
    public String showProperties(Model model) {
        try {
            List<Property> properties = propertyRepository.findAll();
            System.out.println("Total properties found: " + properties.size());
            model.addAttribute("properties", properties);
            return "properties";
        } catch (Exception e) {
            System.err.println("Error while retrieving all properties: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while retrieving properties.");
            return "error";
        }
    }

    // landlord page
    @GetMapping("/landlord")
    public String showLandlordPage() {
        return "landlord";
    }

    // tenant page
    @GetMapping("/tenant")
    public String showTenantPage() {
        return "tenant";
    }

    // User Registration
    @PostMapping("/registerUser")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam String role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);
        return "redirect:/index"; // Redirect to the login section
    }

    // User Login
    @PostMapping("/loginUser")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            if (user.getRole().equals("Tenant")) {
                return "redirect:/tenant";
            } else if (user.getRole().equals("Landlord")) {
                return "redirect:/landlord";
            }
        }
        model.addAttribute("error", "Invalid email or password");
        return "index";
    }
}
