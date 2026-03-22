package com.model;

/**
 * A simple Profile attached to a User.
 *
 * @author Jonah Mosquera
 */
public class Profile {
    private String school;
    private String major;
    private int gradYear;
    private int totalUpvotes;
    private String resumeURL;

    public Profile() {
        this("", "", 0, 0, "");
    }

    public Profile(String school, String major, int gradYear) {
        this(school, major, gradYear, 0, "");
    }

    public Profile(String school, String major, int gradYear, int totalUpvotes, String resumeURL) {
        this.school = school == null ? "" : school;
        this.major = major == null ? "" : major;
        this.gradYear = Math.max(0, gradYear);
        this.totalUpvotes = Math.max(0, totalUpvotes);
        this.resumeURL = resumeURL == null ? "" : resumeURL;
    }

    public String getSchool() {
        return school;
    }

    public String getMajor() {
        return major;
    }

    public int getGradYear() {
        return gradYear;
    }

    public int getTotalUpvotes() {
        return totalUpvotes;
    }

    public String getResumeURL() {
        return resumeURL;
    }

    public void updateProfile(String school, String major, int gradYear) {
        if (school != null) this.school = school;
        if (major != null) this.major = major;
        if (gradYear >= 0) this.gradYear = gradYear;
    }

    public void updateUpvotes(int amount) {
        totalUpvotes = Math.max(0, totalUpvotes + amount);
    }

    public void setResumeURL(String resumeURL) {
        this.resumeURL = resumeURL == null ? "" : resumeURL;
    }

    public boolean hasResumeURL() {
        return resumeURL != null && !resumeURL.isBlank();
    }

    public String getAcademicSummary() {
        String schoolText = school == null || school.isBlank() ? "Unknown School" : school;
        String majorText = major == null || major.isBlank() ? "Undeclared" : major;
        String gradYearText = gradYear <= 0 ? "N/A" : String.valueOf(gradYear);
        return schoolText + " · " + majorText + " · " + gradYearText;
    }
}
