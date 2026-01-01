package com.ats.controller;

import com.ats.entity.Resume;
import com.ats.service.ResumeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "*") // For development flexibility
public class ResumeRestController {

    private final ResumeService resumeService;

    public ResumeRestController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<Resume>> uploadResumes(@RequestParam("files") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            resumeService.storeAndAnalyze(file);
        }
        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    @GetMapping("/results")
    public ResponseEntity<List<Resume>> getNonShortlisted() {
        List<Resume> nonShortlisted = resumeService.getAllResumes().stream()
                .filter(r -> !r.isShortlisted())
                .collect(Collectors.toList());
        return ResponseEntity.ok(nonShortlisted);
    }

    @GetMapping("/shortlisted")
    public ResponseEntity<List<Resume>> getShortlisted() {
        return ResponseEntity.ok(resumeService.getShortlistedResumes());
    }

    @PostMapping("/shortlist/{id}")
    public ResponseEntity<Void> shortlist(@PathVariable Long id) {
        resumeService.shortlistResume(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unshortlist/{id}")
    public ResponseEntity<Void> unshortlist(@PathVariable Long id) {
        resumeService.removeFromShortlist(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResumeDetails(@PathVariable Long id) {
        Optional<Resume> resume = resumeService.getResumeById(id);
        return resume.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
