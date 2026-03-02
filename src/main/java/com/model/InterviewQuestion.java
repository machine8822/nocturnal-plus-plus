package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class InterviewQuestion {

    private UUID questionId;
    private String title;
    private String description;
    private Difficulty difficulty;
    private QuestionType questionType;
    private Category category;
    private String imageURL;
    private UUID authorId;
    private int totalAttempts;
    private int totalSuccesses;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private ArrayList<Section> sections;

    /**
     * Constructor used by DataLoader (all string/int args parsed from JSON).
     */
    public InterviewQuestion(String questionId, String title, String difficulty,
                             String type, String category, String imageURL,
                             String authorId, int totalAttempts, int totalSuccesses,
                             String createdAt, String lastUpdated) {
        this.questionId = UUID.fromString(questionId);
        this.title = title == null ? "" : title;
        this.description = "";
        this.difficulty = Difficulty.valueOf(difficulty);
        this.questionType = QuestionType.valueOf(type);
        this.category = Category.valueOf(category);
        this.imageURL = imageURL == null ? "" : imageURL;
        this.authorId = UUID.fromString(authorId);
        this.totalAttempts = totalAttempts;
        this.totalSuccesses = totalSuccesses;
        this.createdAt = LocalDateTime.parse(createdAt);
        this.lastUpdated = LocalDateTime.parse(lastUpdated);
        this.sections = new ArrayList<>();
    }

    // ── Getters ──

    public UUID getQuestionId()       { return questionId; }
    public String getTitle()          { return title; }
    public String getDescription()    { return description; }
    public Difficulty getDifficulty()  { return difficulty; }
    public QuestionType getType()     { return questionType; }
    public Category getCategory()     { return category; }
    public String getImageURL()       { return imageURL; }
    public UUID getAuthorId()         { return authorId; }
    public int getTotalAttempts()     { return totalAttempts; }
    public int getTotalSuccesses()    { return totalSuccesses; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public ArrayList<Section> getSections() { return sections; }

    // ── Setters ──

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections == null ? new ArrayList<>() : sections;
    }
}
