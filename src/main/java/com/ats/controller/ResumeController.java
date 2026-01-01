package com.ats.controller;

import com.ats.entity.Resume;
import com.ats.service.ResumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/upload")
    public String uploadResumes(@RequestParam("files") MultipartFile[] files, Model model) throws IOException {
        for (MultipartFile file : files) {
            resumeService.storeAndAnalyze(file);
        }
        model.addAttribute("resumes", resumeService.getAllResumes());
        return "results";
    }

    @GetMapping("/results")
    public String results(Model model) {
        List<com.ats.entity.Resume> nonShortlisted = resumeService.getAllResumes().stream()
                .filter(r -> !r.isShortlisted())
                .toList();
        model.addAttribute("resumes", nonShortlisted);
        return "results";
    }

    @PostMapping("/shortlist/{id}")
    public String shortlist(@PathVariable Long id) {
        resumeService.shortlistResume(id);
        return "redirect:/results";
    }

    @PostMapping("/unshortlist/{id}")
    public String unshortlist(@PathVariable Long id) {
        resumeService.removeFromShortlist(id);
        return "redirect:/shortlist";
    }

    @GetMapping("/resume-details/{id}")
    public String resumeDetails(@PathVariable Long id, Model model) {
        resumeService.getResumeById(id).ifPresent(resume -> model.addAttribute("resume", resume));
        return "resume_details";
    }
}
