package com.ats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping("/create-resume")
    public String createResumePage() {
        return "create_resume";
    }

    @GetMapping("/create-cover-letter")
    public String createCoverLetterPage() {
        return "create_cover_letter";
    }

    @GetMapping("/blogs")
    public String blogsPage() {
        return "blogs";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/shortlist")
    public String shortlistPage() {
        return "shortlist";
    }

    @GetMapping("/resume-details")
    public String resumeDetailsPage() {
        return "resume-details";
    }
}
