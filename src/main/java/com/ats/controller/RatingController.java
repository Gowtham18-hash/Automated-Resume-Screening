package com.ats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class RatingController {

    @GetMapping("/rating")
    public String ratingPage() {
        return "rating";
    }

    @PostMapping("/rating")
    public String submitRating(@RequestParam int rating, @RequestParam String feedback, Model model) {
        // In a real app, save to database
        System.out.println("Received Rating: " + rating + ", Feedback: " + feedback);
        model.addAttribute("message", "Thank you for your feedback!");
        return "rating";
    }
}
