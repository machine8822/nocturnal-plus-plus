package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Section {

    private String title;
    private String body;
    private SectionType sectionType;
    private DataType dataType;
    private String imageURL;
    private List<String> constraints;
    private List<String> examples;
    private String expectedTimeComplexity;
    private Integer maxLinesOfCode;
    private Integer timeLimitSeconds;
    private List<Answer> answers;
    private List<Comment> comments;

    public Section(String title, String body, DataType dataType, SectionType sectionType)
    {
        this.title = title == null ? "" : title;
        this.body = body == null ? "" : body;
        this.dataType = dataType;
        this.sectionType = sectionType == null ? SectionType.DESCRIPTION : sectionType;
        this.imageURL = "";
        this.constraints = new ArrayList<>();
        this.examples = new ArrayList<>();
        this.expectedTimeComplexity = "";
        this.maxLinesOfCode = null;
        this.timeLimitSeconds = null;
        this.answers = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    /**
     * Convenience constructor used by DataLoader (string-based section type, no data type).
     */
    public Section(String title, String body, String sectionType)
    {
        this(title, body, null, SectionType.valueOf(sectionType));
    }

    public String getTitle()
    {
        return title;
    }

    public String getBody()
    {
        return body;
    }

    public SectionType getSectionType()
    {
        return sectionType;
    }

    public DataType getDataType()
    {
        return dataType;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public List<String> getConstraints()
    {
        return constraints;
    }

    public List<String> getExamples()
    {
        return examples;
    }

    public String getExpectedTimeComplexity()
    {
        return expectedTimeComplexity;
    }

    public Integer getMaxLinesOfCode()
    {
        return maxLinesOfCode;
    }

    public Integer getTimeLimitSeconds()
    {
        return timeLimitSeconds;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL == null ? "" : imageURL;
    }

    public void setExpectedTimeComplexity(String expectedTimeComplexity)
    {
        this.expectedTimeComplexity = expectedTimeComplexity == null ? "" : expectedTimeComplexity;
    }

    public void setMaxLinesOfCode(Integer maxLinesOfCode)
    {
        this.maxLinesOfCode = maxLinesOfCode;
    }

    public void setTimeLimitSeconds(Integer timeLimitSeconds)
    {
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public void setConstraints(List<String> constraints)
    {
        this.constraints.clear();
        if (constraints == null) {
            return;
        }

        for (String constraint : constraints) {
            if (constraint != null && !constraint.isBlank()) {
                this.constraints.add(constraint);
            }
        }
    }

    public void addConstraint(String constraint)
    {
        if (constraint != null && !constraint.isBlank()) {
            constraints.add(constraint);
        }
    }

    public void setExamples(List<String> examples)
    {
        this.examples.clear();
        if (examples == null) {
            return;
        }

        for (String example : examples) {
            if (example != null && !example.isBlank()) {
                this.examples.add(example);
            }
        }
    }

    public void addExample(String example)
    {
        if (example != null && !example.isBlank()) {
            examples.add(example);
        }
    }

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public int getAnswerCount()
    {
        return answers.size();
    }

    public boolean hasAnswers()
    {
        return !answers.isEmpty();
    }

    public void addAnswer(Answer answer)
    {
        if (answer != null)
        {
            answers.add(answer);
        }
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public int getCommentCount()
    {
        return comments.size();
    }

    public boolean hasComments()
    {
        return !comments.isEmpty();
    }

    public void addComment(Comment comment) {
        if (comment != null) {
            comments.add(comment);
        }
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
                    return true;
                }
                return false;
            }

            if (comment.deleteReply(commentId, actingUserId, isAdmin)) {
                return true;
            }
        }

        for (Answer answer : answers) {
            if (answer.deleteComment(commentId, actingUserId, isAdmin)) {
                return true;
            }
        }

        return false;
    }

    public boolean deleteAnswer(UUID answerId, UUID actingUserId, boolean isAdmin) {
        if (answerId == null || actingUserId == null) {
            return false;
        }

        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            if (answer.getAnswerId().equals(answerId)) {
                if (canBeDeletedBy(answer.getAuthorId(), actingUserId, isAdmin)) {
                    answers.remove(i);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean canBeDeletedBy(UUID contentAuthorId, UUID actingUserId, boolean isAdmin) {
        if (isAdmin) {
            return true;
        }

        return contentAuthorId != null && contentAuthorId.equals(actingUserId);
    }
}
