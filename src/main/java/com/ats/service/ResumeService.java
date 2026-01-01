package com.ats.service;

import com.ats.entity.Resume;
import com.ats.repository.ResumeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ScoringService scoringService;

    public ResumeService(ResumeRepository resumeRepository, ScoringService scoringService) {
        this.resumeRepository = resumeRepository;
        this.scoringService = scoringService;
    }

    public void storeAndAnalyze(MultipartFile file) throws IOException {
        String content = extractText(file);
        int score = scoringService.calculateScore(content);

        Resume resume = new Resume();
        resume.setFilename(file.getOriginalFilename());
        resume.setScore(score);
        resume.setSkills(scoringService.extractSkillsString(content));
        resume.setCgpa(scoringService.extractCgpa(content));
        resume.setProjects(scoringService.extractProjects(content));
        resume.setInternships(scoringService.extractInternships(content));
        resume.setCertificates(scoringService.extractCertificates(content));
        resume.setShortlisted(false);
        resume.setContent(content);

        resumeRepository.save(resume);
    }

    public void shortlistResume(Long id) {
        resumeRepository.findById(id).ifPresent(resume -> {
            resume.setShortlisted(true);
            resumeRepository.save(resume);
        });
    }

    public void removeFromShortlist(Long id) {
        resumeRepository.findById(id).ifPresent(resume -> {
            resume.setShortlisted(false);
            resumeRepository.save(resume);
        });
    }

    public List<Resume> getShortlistedResumes() {
        return resumeRepository.findAll().stream()
                .filter(Resume::isShortlisted)
                .toList();
    }

    public java.util.Optional<Resume> getResumeById(Long id) {
        return resumeRepository.findById(id);
    }

    private String extractText(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null)
            return "";

        try (InputStream is = file.getInputStream()) {
            if (filename.toLowerCase().endsWith(".pdf")) {
                try (PDDocument document = PDDocument.load(is)) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    return stripper.getText(document);
                }
            } else if (filename.toLowerCase().endsWith(".docx")) {
                try (XWPFDocument document = new XWPFDocument(is);
                        XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
                    return extractor.getText();
                }
            } else {
                return new String(file.getBytes());
            }
        }
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }
}
