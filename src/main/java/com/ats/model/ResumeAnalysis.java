package com.ats.model;

import java.util.Set;

public class ResumeAnalysis {
    private String file;
    private String bestRole;
    private int resumeScore;
    private Set<String> missingSkills;
    private Set<String> matchedSkills;

    public ResumeAnalysis(String file, String bestRole, int resumeScore, Set<String> missingSkills, Set<String> matchedSkills) {
        this.file = file;
        this.bestRole = bestRole;
        this.resumeScore = resumeScore;
        this.missingSkills = missingSkills;
        this.matchedSkills = matchedSkills;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getBestRole() {
        return bestRole;
    }

    public void setBestRole(String bestRole) {
        this.bestRole = bestRole;
    }

    public int getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(int resumeScore) {
        this.resumeScore = resumeScore;
    }

    public Set<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(Set<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public Set<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(Set<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }
}
