package com.ats.controller;

import com.ats.model.ResumeAnalysis;
import com.ats.service.JobRoleMatcherService;
import com.ats.service.ResumeParserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class ResumeController {

    private final ResumeParserService resumeParserService;
    private final JobRoleMatcherService jobRoleMatcherService;

    public ResumeController(ResumeParserService resumeParserService, JobRoleMatcherService jobRoleMatcherService) {
        this.resumeParserService = resumeParserService;
        this.jobRoleMatcherService = jobRoleMatcherService;
    }

    @GetMapping("/")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("files") MultipartFile[] files, Model model) {
        List<ResumeAnalysis> parsedResumes = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            try {
                String text = resumeParserService.extractText(file);
                if (!text.isEmpty()) {
                    List<String> skills = jobRoleMatcherService.extractSkills(text);
                    ResumeAnalysis analysis = jobRoleMatcherService.analyzeResume(file.getOriginalFilename(), skills);
                    parsedResumes.add(analysis);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // In a real app, handle error gracefully
            }
        }

        if (parsedResumes.isEmpty()) {
            return "upload"; // Or show error message
        }

        // Sort by score descending
        parsedResumes.sort(Comparator.comparingInt(ResumeAnalysis::getResumeScore).reversed());

        model.addAttribute("parsed_resumes", parsedResumes);
        return "index";
    }
}
