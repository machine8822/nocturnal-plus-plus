package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Comment {
    private UUID commentId;
    private String text;
    private UUID authorId;
    private LocalDateTime timestamp;
    private boolean isEdited;
    private int upvoteCount;
    private int downvoteCount;
    private List<Comment> replies;

    public Comment(String text, UUID authorId) {
        this(UUID.randomUUID(), text, authorId, LocalDateTime.now(), false, 0, 0, new ArrayList<>());
    }

    public Comment(UUID commentId,
                   String text,
                   UUID authorId,
                   LocalDateTime timestamp,
                   boolean isEdited,
                   int upvoteCount,
                   int downvoteCount,
                   List<Comment> replies) {
        this.commentId = commentId == null ? UUID.randomUUID() : commentId;
        this.text = text == null ? "" : text;
        this.authorId = authorId;
        this.timestamp = timestamp == null ? LocalDateTime.now() : timestamp;
        this.isEdited = isEdited;
        this.upvoteCount = Math.max(0, upvoteCount);
        this.downvoteCount = Math.max(0, downvoteCount);
        this.replies = replies == null ? new ArrayList<>() : new ArrayList<>(replies);
    }

    // Getters

    public UUID getCommentId() {
        return commentId;
    }

    public String getText() {
        return text;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public int getDownvoteCount() {
        return downvoteCount;
    }

    public void edit(String newText) {
        this.text = newText == null ? "" : newText;
        this.isEdited = true;
        this.timestamp = LocalDateTime.now();
    }

    public void upvote() {
        upvoteCount++;
    }

    public void downvote() {
        downvoteCount++;
    }

    public int getVoteScore() {
        return upvoteCount - downvoteCount;
    }

    public void addReply(Comment reply) {
        if (reply != null) {
            replies.add(reply);
        }
    }

    public boolean deleteReply(UUID commentId, UUID actingUserId, boolean isAdmin) {
        if (commentId == null || actingUserId == null) {
            return false;
        }

        for (int i = 0; i < replies.size(); i++) {
            Comment reply = replies.get(i);
            if (reply.getCommentId().equals(commentId)) {
                if (canBeDeletedBy(reply.getAuthorId(), actingUserId, isAdmin)) {
                    replies.remove(i);
                    return true;
                }
                return false;
            }

            if (reply.deleteReply(commentId, actingUserId, isAdmin)) {
                return true;
            }
        }

        return false;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public int getReplyCount() {
        return replies.size();
    }

    public boolean hasReplies() {
        return !replies.isEmpty();
    }

    public boolean isNetPositive() {
        return getVoteScore() > 0;
    }

    private boolean canBeDeletedBy(UUID contentAuthorId, UUID actingUserId, boolean isAdmin) {
        if (isAdmin) {
            return true;
        }

        return contentAuthorId != null && contentAuthorId.equals(actingUserId);
    }

    // Used by DataLoader when restoring from JSON
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Used by DataLoader when restoring from JSON
    public void setEdited(boolean edited) {
        this.isEdited = edited;
    }

    // Used by DataLoader when restoring from JSON
    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = Math.max(0, upvoteCount);
    }

    // Used by DataLoader when restoring from JSON
    public void setDownvoteCount(int downvoteCount) {
        this.downvoteCount = Math.max(0, downvoteCount);
    }

    // Used by DataLoader when restoring from JSON
    public void setReplies(List<Comment> replies) {
        this.replies = replies == null ? new ArrayList<>() : new ArrayList<>(replies);
    }
}
