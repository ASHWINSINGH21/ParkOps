package com.example.Park.controller;

import com.example.Park.User.User_master;
import com.example.Park.User.user_master_repo;
import com.example.Park.model.Feedback1;
import com.example.Park.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class ReviewController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private user_master_repo userRepo;

    @GetMapping("/feedback")
    public String showFeedback(Model model, Principal principal) {
        User_master user = userRepo.findByEmail(principal.getName());

        model.addAttribute("feedbacks", feedbackRepository.findAll());
        model.addAttribute("currentUser", user);

        return "admin_feedback";
    }

    @PostMapping("/{id}/reply")
    public String replyToFeedback(@PathVariable int id,
                                  @RequestParam String response) {
        Feedback1 feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid feedback ID"));

        feedback.setResponse(response);
        feedback.setResolved(true);
        feedbackRepository.save(feedback);

        return "redirect:/admin/feedback";
    }
}