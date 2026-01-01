package com.ats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    private final com.ats.service.ResumeService resumeService;

    public NavigationController(com.ats.service.ResumeService resumeService) {
        this.resumeService = resumeService;
    }

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
    public String shortlistPage(org.springframework.ui.Model model) {
        model.addAttribute("resumes", resumeService.getShortlistedResumes());
        return "shortlist";
    }

    @GetMapping("/resume-details")
    public String resumeDetailsPage() {
        return "resume-details";
    }
}
