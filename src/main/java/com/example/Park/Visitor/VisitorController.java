package com.example.Park.Visitor;

import com.example.Park.User.User_master;
import com.example.Park.User.user_master_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class VisitorController {

    @Autowired
    private Visitor_repo visitorRepo;

    @Autowired
    private user_master_repo userRepo;

    @GetMapping("/register_visitor")
    @PreAuthorize("isAuthenticated()")
    public String showRegistrationForm(Model model, Principal principal) {
        User_master user = userRepo.findByEmail(principal.getName());
        model.addAttribute("visitor", new Visitor());
        model.addAttribute("loggedInName", user.getUser_name());  // ✅ use name, not email

        boolean hasVisitors = !visitorRepo.findByUserId(user.getUser_id()).isEmpty();
        model.addAttribute("hasVisitors", hasVisitors);

        return "register_visitor";
    }

    @PostMapping("/register_visitor")
    @PreAuthorize("isAuthenticated()")
    public String handleVisitorForm(@ModelAttribute Visitor visitor,
                                    @RequestParam("action") String action,
                                    Principal principal,
                                    Model model) {
        User_master user = userRepo.findByEmail(principal.getName());
        if (user == null) {
            model.addAttribute("error", "User not found. Please log in again.");
            return "login";
        }
        if ("add".equals(action)) {
            if (visitor.getName() != null && !visitor.getName().isBlank()) {
                visitor.setUserId(user.getUser_id());
                visitorRepo.save(visitor);
            }

            model.addAttribute("visitor", new Visitor());
            model.addAttribute("loggedInName", user.getUser_name());
            model.addAttribute("success", "Visitor added successfully!");

            boolean hasVisitors = !visitorRepo.findByUserId(user.getUser_id()).isEmpty();
            model.addAttribute("hasVisitors", hasVisitors);

            return "register_visitor";
        }
        return "redirect:/book";
    }
}