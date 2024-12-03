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

    // Submissão de informações de inquilinos
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
            property.setTenantId("tenant-" + UUID.randomUUID());
            property.setPropertyName(name);
            property.setLocation(address);
            property.setHygiene(hygiene);
            property.setSafety(safety);
            property.setCrowding(conservation);
            property.setLandlordRating(landlord);
            property.setComments(comments);

            propertyRepository.save(property);
            return "redirect:/tenant";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    // Submissão de comentários de proprietários
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

            property.setLandlordId("landlord-" + UUID.randomUUID());
            property.setLandlordComment(comment);

            propertyRepository.save(property);

            model.addAttribute("property", property);
            model.addAttribute("success", "Comment added successfully.");
            return "landlord";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while submitting the comment.");
            return "landlord";
        }
    }

    // Buscar informações de uma propriedade pelo inquilino
    @GetMapping("/tenantSearch")
    public String tenantSearch(@RequestParam String address, Model model) {
        try {
            Property property = propertyRepository.findByLocation(address);

            if (property != null) {
                model.addAttribute("property", property);
                model.addAttribute("location", property.getLocation());
            } else {
                model.addAttribute("error", "No property found for the provided address.");
            }

            return "tenant";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while searching for the property.");
            return "tenant";
        }
    }

    // Buscar informações de uma propriedade pelo proprietário
    @GetMapping("/landlordSearch")
    public String landlordSearch(@RequestParam String address, Model model) {
        try {
            Property property = propertyRepository.findByLocation(address);

            if (property != null) {
                model.addAttribute("property", property);
                model.addAttribute("location", property.getLocation());
            } else {
                model.addAttribute("error", "No property found for the provided address.");
            }

            return "landlord";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while searching for the property.");
            return "landlord";
        }
    }

    // Listar todas as propriedades
    @GetMapping("/properties")
    public String showProperties(Model model) {
        List<Property> properties = propertyRepository.findAll();
        model.addAttribute("properties", properties);
        return "properties";
    }

    // Página inicial do proprietário
    @GetMapping("/landlord")
    public String showLandlordPage() {
        return "landlord";
    }

    // Página inicial do inquilino
    @GetMapping("/tenant")
    public String showTenantPage() {
        return "tenant";
    }
}
