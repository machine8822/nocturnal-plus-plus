package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Section {

    private String title;
    private String body;
    private SectionType sectionType;
    private DataType dataType;
    private List<Answer> answers;
    private List<Comment> comments;

    public Section(String title, String body, DataType dataType, SectionType sectionType)
    {
        this.title = title == null ? "" : title;
        this.body = body == null ? "" : body;
        this.dataType = dataType;
        this.sectionType = sectionType == null ? SectionType.DESCRIPTION : sectionType;
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

    public List<Answer> getAnswers()
    {
        return answers;
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
