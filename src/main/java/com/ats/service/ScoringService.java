package com.ats.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ScoringService {

    private static final Map<String, List<String>> ROLE_SKILLS = new HashMap<>();

    static {
        ROLE_SKILLS.put("Software Engineer",
                Arrays.asList("java", "python", "sql", "git", "rest", "spring", "spring boot", "docker", "aws"));
        ROLE_SKILLS.put("Frontend Developer",
                Arrays.asList("javascript", "react", "html", "css", "vue", "angular", "typescript", "bootstrap"));
        ROLE_SKILLS.put("QA Tester",
                Arrays.asList("selenium", "junit", "manual testing", "jira", "testng", "bugzilla"));
        ROLE_SKILLS.put("DevOps Engineer",
                Arrays.asList("jenkins", "docker", "kubernetes", "ansible", "terraform", "aws", "linux"));
    }

    public int calculateScore(String resumeText) {
        String normalizedResume = resumeText.toLowerCase();
        int skillScore = calculateMaxSkillScore(normalizedResume);
        int cgpaScore = extractCgpaScore(normalizedResume);
        int projectScore = containsKeywords(normalizedResume,
                Arrays.asList("project", "built", "developed", "implemented")) ? 100 : 0;
        int internshipScore = containsKeywords(normalizedResume, Arrays.asList("internship", "intern", "trainee")) ? 100
                : 0;
        int certScore = containsKeywords(normalizedResume, Arrays.asList("certification", "certificate", "certified"))
                ? 100
                : 0;

        // Weighted Score Calculation
        double totalScore = (skillScore * 0.45) + (cgpaScore * 0.15) + (projectScore * 0.15) + (internshipScore * 0.15)
                + (certScore * 0.10);
        return (int) totalScore;
    }

    private int calculateMaxSkillScore(String normalizedResume) {
        int maxScore = 0;
        for (Map.Entry<String, List<String>> entry : ROLE_SKILLS.entrySet()) {
            int score = calculateRoleScore(normalizedResume, entry.getValue());
            if (score > maxScore)
                maxScore = score;
        }
        return maxScore;
    }

    private int calculateRoleScore(String resumeText, List<String> skills) {
        long matchCount = skills.stream().filter(resumeText::contains).count();
        return (int) ((double) matchCount / skills.size() * 100);
    }

    public String extractCgpa(String text) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern
                .compile("(cgpa|gpa)\\s*[:\\-]?\\s*([0-9]*\\.?[0-9]+)");
        java.util.regex.Matcher matcher = pattern.matcher(text.toLowerCase());
        if (matcher.find())
            return matcher.group(2);
        return "Not found";
    }

    private int extractCgpaScore(String text) {
        String cgpaStr = extractCgpa(text);
        if (cgpaStr.equals("Not found"))
            return 0;
        try {
            double cgpa = Double.parseDouble(cgpaStr);
            if (cgpa > 9.0)
                return 100;
            if (cgpa > 8.0)
                return 90;
            if (cgpa > 7.0)
                return 80;
            return 70;
        } catch (Exception e) {
            return 50;
        }
    }

    public String extractProjects(String text) {
        return text.toLowerCase().contains("project") ? "Projects Found" : "Not specified";
    }

    public String extractInternships(String text) {
        return text.toLowerCase().contains("intern") ? "Internships Found" : "Not specified";
    }

    public String extractCertificates(String text) {
        return text.toLowerCase().contains("certific") ? "Certificates Found" : "Not specified";
    }

    private boolean containsKeywords(String text, List<String> keywords) {
        return keywords.stream().anyMatch(text::contains);
    }

    public String extractSkillsString(String text) {
        String normalizedText = text.toLowerCase();
        Set<String> foundSkills = new HashSet<>();
        ROLE_SKILLS.values().forEach(skills -> {
            skills.forEach(skill -> {
                if (normalizedText.contains(skill))
                    foundSkills.add(skill);
            });
        });
        return String.join(", ", foundSkills);
    }
}
