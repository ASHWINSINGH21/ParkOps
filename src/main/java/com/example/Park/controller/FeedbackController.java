package com.example.Park.controller;
import com.example.Park.User.User_master;
import com.example.Park.User.user_master_repo;
import com.example.Park.model.Feedback1;
import com.example.Park.repository.FeedbackRepository;
import com.example.Park.repository.RideRepository;
import com.example.Park.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class FeedbackController {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private user_master_repo userRepo;

    @GetMapping("/feedback")
    public String showFeedbackForm(Model model, Principal principal) {
        User_master user = userRepo.findByEmail(principal.getName());

        model.addAttribute("rides", rideRepository.findAll());
        model.addAttribute("visitors", visitorRepository.findByUid(user.getUser_id()));
        model.addAttribute("currentUser", user);

        return "submit_feedback";
    }
    @PostMapping("/submit")
    public String submitFeedback(@RequestParam("visitor") int visitorId,
                                 @RequestParam("ride") int rideId,
                                 @RequestParam(value = "rating", required = false) Integer rating,
                                 @RequestParam(value = "comments", required = false) String comments,
                                 Principal principal,
                                 Model model) {

        Feedback1 feedback = new Feedback1();
        feedback.setVisitor_id(visitorId);
        feedback.setRide_id(rideId);
        feedback.setRating(rating);
        feedback.setComment(comments);
        feedback.setResolved(false);
        feedback.setSubmitted_at(LocalDate.now());

        feedbackRepository.save(feedback);
        User_master user = userRepo.findByEmail(principal.getName());
        model.addAttribute("rides", rideRepository.findAll());
        model.addAttribute("visitors", visitorRepository.findByUid(user.getUser_id()));
        model.addAttribute("currentUser", user);
        model.addAttribute("successMessage", "Review Succesfully Registered");
        return "submit_feedback";
    }
}