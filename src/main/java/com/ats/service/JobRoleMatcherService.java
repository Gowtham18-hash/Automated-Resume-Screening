package com.ats.service;

import com.ats.model.ResumeAnalysis;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class JobRoleMatcherService {

        private static final Map<String, List<String>> JOB_ROLES = new HashMap<>();

        static {
                JOB_ROLES.put("Data Scientist", Arrays.asList("Python", "Machine Learning", "SQL", "TensorFlow",
                                "Pandas", "Numpy", "Deep Learning", "Scikit-Learn", "Keras", "Matplotlib", "Seaborn",
                                "Statistics", "Big Data", "Spark", "Hadoop", "Data Mining", "NLP", "Tableau"));
                JOB_ROLES.put("Web Developer",
                                Arrays.asList("JavaScript", "React", "Node.js", "HTML", "CSS", "Django", "Angular",
                                                "Bootstrap", "Vue.js", "TypeScript", "Webpack", "SASS", "REST API",
                                                "GraphQL", "jQuery", "PHP", "Laravel", "Git"));
                JOB_ROLES.put("Software Engineer",
                                Arrays.asList("Java", "C++", "OOP", "Git", "Linux", "Spring Boot", "Microservices",
                                                "Algorithms", "Data Structures", "Design Patterns", "System Design",
                                                "JIRA", "CI/CD", "SQL", "Testing", "Debugging", "Agile"));
                JOB_ROLES.put("Cloud Engineer",
                                Arrays.asList("AWS", "Docker", "Kubernetes", "Terraform", "Azure", "GCP", "DevOps",
                                                "Ansible", "Jenkins", "CloudFormation", "Lambda", "EC2", "S3", "Linux",
                                                "Bash", "Networking"));
                JOB_ROLES.put("AI Engineer",
                                Arrays.asList("Artificial Intelligence", "Deep Learning", "Computer Vision", "NLP",
                                                "PyTorch", "TensorFlow", "Reinforcement Learning", "Generative AI",
                                                "LLM", "OpenCV", "Neural Networks", "Robotics"));
                JOB_ROLES.put("Data Analyst",
                                Arrays.asList("Excel", "SQL", "Power BI", "Tableau", "Python", "Data Visualization",
                                                "R", "SAS", "Google Analytics", "Business Intelligence", "Statistics",
                                                "Data Cleaning"));
                JOB_ROLES.put("Cybersecurity Analyst",
                                Arrays.asList("Cybersecurity", "Penetration Testing", "Networking", "Ethical Hacking",
                                                "Firewall", "Cryptography", "SIEM", "Wireshark", "Linux",
                                                "Risk Assessment", "Incident Response", "Security Auditing"));
                JOB_ROLES.put("Frontend Developer",
                                Arrays.asList("HTML", "CSS", "JavaScript", "React", "Vue", "Angular", "Tailwind",
                                                "Bootstrap", "Redux", "TypeScript", "Figma", "Responsive Design",
                                                "UI/UX", "DOM", "AJAX"));
                JOB_ROLES.put("Backend Developer",
                                Arrays.asList("Java", "Python", "Node.js", "Go", "Ruby", "PHP", "SQL", "NoSQL",
                                                "MongoDB", "PostgreSQL", "Redis", "API Design", "Docker", "Spring Boot",
                                                "Microservices", "Express", "Django"));
                JOB_ROLES.put("Mobile Developer", Arrays.asList("Swift", "Kotlin", "Flutter", "React Native", "iOS",
                                "Android", "Dart", "Firebase", "Mobile UI/UX", "Xcode", "Android Studio"));
                JOB_ROLES.put("Full Stack Developer",
                                Arrays.asList("JavaScript", "React", "Node.js", "Express", "MongoDB", "SQL", "HTML",
                                                "CSS", "Git", "AWS", "Docker", "REST API", "Java", "Spring Boot",
                                                "Python", "Django"));
                JOB_ROLES.put("DevOps Engineer",
                                Arrays.asList("Docker", "Kubernetes", "Jenkins", "Git", "AWS", "Azure", "Linux", "Bash",
                                                "Terraform", "Ansible", "Prometheus", "Grafana", "CI/CD", "Scripting"));
                JOB_ROLES.put("Java Developer", Arrays.asList("Java", "Spring Boot", "Hibernate", "JPA", "SQL",
                                "Microservices", "Maven", "Gradle", "JUnit", "REST API", "Git", "OOP"));
        }

        public List<String> extractSkills(String text) {
                String lowerText = text.toLowerCase();
                Set<String> allSkills = JOB_ROLES.values().stream()
                                .flatMap(List::stream)
                                .collect(Collectors.toSet());

                List<String> foundSkills = new ArrayList<>();
                for (String skill : allSkills) {
                        // Regex to match whole words, case insensitive
                        if (Pattern.compile("\\b" + Pattern.quote(skill.toLowerCase()) + "\\b").matcher(lowerText)
                                        .find()) {
                                foundSkills.add(skill);
                        }
                }
                return foundSkills;
        }

        public ResumeAnalysis analyzeResume(String filename, List<String> extractedSkills) {
                String bestRole = null;
                Set<String> bestMatchedSkills = new HashSet<>();
                int bestMatchCount = 0;
                Set<String> unmatchedSkills = new HashSet<>();
                int resumeScore = 0;

                for (Map.Entry<String, List<String>> entry : JOB_ROLES.entrySet()) {
                        String role = entry.getKey();
                        List<String> requiredSkills = entry.getValue();

                        Set<String> matched = new HashSet<>(extractedSkills);
                        matched.retainAll(requiredSkills);

                        if (matched.size() > bestMatchCount) {
                                bestMatchCount = matched.size();
                                bestRole = role;
                                bestMatchedSkills = matched;
                        }
                }

                if (bestRole != null) {
                        int totalRequired = JOB_ROLES.get(bestRole).size();
                        resumeScore = (int) Math.round(((double) bestMatchCount / totalRequired) * 10);
                        unmatchedSkills = new HashSet<>(JOB_ROLES.get(bestRole));
                        unmatchedSkills.removeAll(bestMatchedSkills);
                }

                return new ResumeAnalysis(filename, bestRole, resumeScore, unmatchedSkills, bestMatchedSkills);
        }
}
