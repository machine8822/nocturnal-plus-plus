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
        this(questionId, title, description, difficulty, category, type, authorId, createdAt,
                lastUpdated, totalAttempts, totalSuccesses, imageURL, new ArrayList<>(), new ArrayList<>());
    }

    public InterviewQuestion(UUID questionId, String title, String description, Difficulty difficulty,
    Category category, QuestionType type, UUID authorId, LocalDateTime createdAt,
    LocalDateTime lastUpdated, int totalAttempts,int totalSuccesses, String imageURL,
    List<Comment> comments, List<Section> sections)
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

        this.comments = comments == null ? new ArrayList<>() : new ArrayList<>(comments);
        this.sections = sections == null ? new ArrayList<>() : new ArrayList<>(sections);
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

    public boolean deleteComment(UUID commentId, UUID actingUserId, boolean isAdmin) {
        if (commentId == null || actingUserId == null) {
            return false;
        }

        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            if (comment.getCommentId().equals(commentId)) {
                if (canBeDeletedBy(comment.getAuthorId(), actingUserId, isAdmin)) {
                    comments.remove(i);
                    touch();
                    return true;
                }
                return false;
            }

            if (comment.deleteReply(commentId, actingUserId, isAdmin)) {
                touch();
                return true;
            }
        }

        for (Section section : sections) {
            if (section.deleteComment(commentId, actingUserId, isAdmin)) {
                touch();
                return true;
            }
        }

        return false;
    }

    public boolean deleteAnswer(UUID answerId, UUID actingUserId, boolean isAdmin) {
        if (answerId == null || actingUserId == null) {
            return false;
        }

        for (Section section : sections) {
            if (section.deleteAnswer(answerId, actingUserId, isAdmin)) {
                touch();
                return true;
            }
        }
        return false;
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

    private boolean canBeDeletedBy(UUID contentAuthorId, UUID actingUserId, boolean isAdmin) {
        if (isAdmin) {
            return true;
        }

        return contentAuthorId != null && contentAuthorId.equals(actingUserId);
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
     * To string to display the question in a readable format.
     * @return String representation of the question, including sections and comments.
     */

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        // Question header
        sb.append("=== Interview Question ===\n");
        sb.append("ID: ").append(questionId).append("\n");
        sb.append("Title: ").append(title).append("\n");
        
        String displayDescription = (description == null || description.isBlank())
            ? "(no description)"
            : description;
        sb.append("Description: ").append(displayDescription).append("\n");
        
        sb.append("Difficulty: ").append(difficulty).append("\n");
        sb.append("Type: ").append(type).append("\n");
        sb.append("Category: ").append(category).append("\n");
        sb.append("Author ID: ").append(authorId).append("\n");
        sb.append("Image URL: ").append(imageURL.isEmpty() ? "(none)" : imageURL).append("\n");
        sb.append("Total Attempts: ").append(totalAttempts).append("\n");
        sb.append("Total Successes: ").append(totalSuccesses).append("\n");
        sb.append("Success Rate: ").append(String.format("%.2f%%", getSuccessRate() * 100)).append("\n");
        sb.append("Created At: ").append(createdAt).append("\n");
        sb.append("Last Updated: ").append(lastUpdated).append("\n");
        
        // Sections
        if (!sections.isEmpty()) {
            sb.append("\n--- Sections (").append(sections.size()).append(") ---\n");
            for (int i = 0; i < sections.size(); i++) {
                Section section = sections.get(i);
                sb.append("\n  Section ").append(i + 1).append(":\n");
                sb.append("    Title: ").append(section.getTitle()).append("\n");
                sb.append("    Body: ").append(section.getBody()).append("\n");
                sb.append("    Type: ").append(section.getSectionType()).append("\n");
                sb.append("    Data Type: ").append(section.getDataType()).append("\n");
                sb.append("    Image URL: ").append(section.getImageURL().isEmpty() ? "(none)" : section.getImageURL()).append("\n");
                
                List<String> constraints = section.getConstraints();
                if (!constraints.isEmpty()) {
                    sb.append("    Constraints: ").append(constraints).append("\n");
                }
                
                List<String> examples = section.getExamples();
                if (!examples.isEmpty()) {
                    sb.append("    Examples: ").append(examples).append("\n");
                }
                
                String timeComplexity = section.getExpectedTimeComplexity();
                if (timeComplexity != null && !timeComplexity.isEmpty()) {
                    sb.append("    Expected Time Complexity: ").append(timeComplexity).append("\n");
                }
                
                Integer maxLines = section.getMaxLinesOfCode();
                if (maxLines != null) {
                    sb.append("    Max Lines of Code: ").append(maxLines).append("\n");
                }
                
                Integer timeLimit = section.getTimeLimitSeconds();
                if (timeLimit != null) {
                    sb.append("    Time Limit (seconds): ").append(timeLimit).append("\n");
                }
                
                List<Answer> answers = section.getAnswers();
                if (!answers.isEmpty()) {
                    sb.append("    Number of Answers: ").append(answers.size()).append("\n");
                }
                
                List<Comment> sectionComments = section.getComments();
                if (!sectionComments.isEmpty()) {
                    sb.append("    Number of Comments: ").append(sectionComments.size()).append("\n");
                }
            }
        }
        
        // Comments
        if (!comments.isEmpty()) {
            sb.append("\n--- Comments (").append(comments.size()).append(") ---\n");
        }
        
        return sb.toString();
    }
}
