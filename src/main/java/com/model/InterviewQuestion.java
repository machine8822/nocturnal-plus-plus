package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InterviewQuestion {

    private UUID questionId;
    private String title;
    private String description;
    private Difficulty difficulty;
    private QuestionType type;
    private Category category;
    private String imageURL;
    private UUID authorId;

    private int totalAttempts;
    private int totalSuccesses;

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    private List<Comment> comments;
    private List<Section> sections;

    /**
     * Creation constructor.
     * Used when a user creates a new question.
     * Automatically generates ID and timestamps.
     */
    public InterviewQuestion(String title, String description, Difficulty difficulty,
    Category category, QuestionType type, UUID authorId)
    {

        this.questionId = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.type = type;
        this.authorId = authorId;

        this.imageURL = "";
        this.totalAttempts = 0;
        this.totalSuccesses = 0;

        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();

        this.comments = new ArrayList<>();
        this.sections = new ArrayList<>();
    }

    /**
     * DataLoader constructor.
     * Used when restoring from JSON.
     */
    public InterviewQuestion(UUID questionId, String title, String description, Difficulty difficulty,
    Category category, QuestionType type, UUID authorId, LocalDateTime createdAt,
    LocalDateTime lastUpdated, int totalAttempts,int totalSuccesses, String imageURL)
    {

        this.questionId = questionId;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.type = type;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.totalAttempts = totalAttempts;
        this.totalSuccesses = totalSuccesses;
        this.imageURL = imageURL == null ? "" : imageURL;

        this.comments = new ArrayList<>();
        this.sections = new ArrayList<>();
    }

    /**
     * section methods
     * @param section
     */
    public void addSection(Section section) 
    {
        if (section != null) 
        {
            sections.add(section);
            touch();
        }
    }

    public List<Section> getSections() 
    {
        return sections;
    }

    public Section getSection(int index) 
    {
        if (index < 0 || index >= sections.size())
        {
            return null;
        }
        return sections.get(index);
    }

    /**
     * Comment methods
     * @param comment
     */

    public void addComment(Comment comment)
    {
        if (comment != null)
        {
            comments.add(comment);
            touch();
        }
    }

    public List<Comment> getComments() 
    {
        return comments;
    }

    /**
     * Statistics methods
     * @param success
     */

    public void recordAttempt(boolean success)
    {
        totalAttempts++;
        if (success) 
        {
            totalSuccesses++;
        }
        touch();
    }

    public double getSuccessRate()
    {
        if (totalAttempts == 0)
        {
            return 0.0;
        }
        return (double) totalSuccesses / totalAttempts;
    }

    /**
     * update methods
     * @param newTitle
     * @param newDescription
     */

    public void updateContent(String newTitle, String newDescription) 
    {
        if (newTitle != null && !newTitle.isBlank()) 
        {
            this.title = newTitle;
        }

        if (newDescription != null && !newDescription.isBlank())
        {
            this.description = newDescription;
        }

        touch();
    }

    public void setImageURL(String imageURL) 
    {
        this.imageURL = imageURL == null ? "" : imageURL;
        touch();
    }

    private void touch() 
    {
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * getters
     * @return
     */

    public UUID getQuestionId() 
    {
        return questionId;
    }

    public String getTitle() 
    {
        return title;
    }

    public String getDescription() 
    {
        return description;
    }

    public Difficulty getDifficulty() 
    {
        return difficulty;
    }

    public QuestionType getType() 
    {
        return type;
    }

    public Category getCategory() 
    {
        return category;
    }

    public UUID getAuthorId() 
    {
        return authorId;
    }

    public int getTotalAttempts() 
    {
        return totalAttempts;
    }

    public int getTotalSuccesses() 
    {
        return totalSuccesses;
    }

    public LocalDateTime getCreatedAt() 
    {
        return createdAt;
    }

    public LocalDateTime getLastUpdated() 
    {
        return lastUpdated;
    }

    public String getImageURL() 
    {
        return imageURL;
    }

    /**
     * Utility
     */

    @Override
    public String toString()
    {
        String displayDescription = (description == null || description.isBlank())
            ? "(no description)"
            : description;

        return "InterviewQuestion{" +
                "id=" + questionId +
                ", title='" + title + '\'' +
            ", description='" + displayDescription + '\'' +
                ", difficulty=" + difficulty +
                ", type=" + type +
                ", category=" + category +
                ", attempts=" + totalAttempts +
                ", successes=" + totalSuccesses +
                '}';
    }
}