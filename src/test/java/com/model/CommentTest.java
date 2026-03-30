package com.model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    // Bug #68 — edit() allows setting text to blank

    @Test
    public void comment_edit_withValidText_updatesText() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("Updated text");
        assertEquals("Updated text", c.getText());
    }

    @Test
    public void comment_edit_withValidText_setsIsEdited() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("Updated text");
        assertTrue(c.isEdited());
    }

    @Test
    public void comment_edit_withNullText_storesEmptyString() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit(null);
        assertEquals("", c.getText());
    }

    @Test
    public void comment_edit_withBlankText_storesBlankText() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("   ");
        assertEquals("   ", c.getText());
    }

    @Test
    public void comment_edit_withBlankText_setsIsEdited() {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        c.edit("   ");
        assertTrue(c.isEdited());
    }

    @Test
    public void comment_edit_updatesTimestamp() throws InterruptedException {
        Comment c = new Comment("Original text", java.util.UUID.randomUUID());
        java.time.LocalDateTime before = c.getTimestamp();
        Thread.sleep(10);
        c.edit("Updated text");
        assertTrue(c.getTimestamp().isAfter(before));
    }

    // Constructor

    @Test
    public void constructor_nullText_defaultsToEmpty() {
        Comment c = new Comment(null, java.util.UUID.randomUUID());
        assertEquals("", c.getText());
    }

    @Test
    public void constructor_validText_storesText() {
        Comment c = new Comment("Hello", java.util.UUID.randomUUID());
        assertEquals("Hello", c.getText());
    }

    @Test
    public void constructor_setsCommentId() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        assertNotNull(c.getCommentId());
    }

    @Test
    public void constructor_setsAuthorId() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Comment c = new Comment("text", authorId);
        assertEquals(authorId, c.getAuthorId());
    }

    @Test
    public void constructor_setsTimestamp() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        assertNotNull(c.getTimestamp());
    }

    @Test
    public void constructor_isEditedDefaultsFalse() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        assertFalse(c.isEdited());
    }

    @Test
    public void fullConstructor_negativeUpvotes_clampsToZero() {
        Comment c = new Comment(java.util.UUID.randomUUID(), "text", java.util.UUID.randomUUID(),
                java.time.LocalDateTime.now(), false, -5, 0, null);
        assertEquals(0, c.getUpvoteCount());
    }

    @Test
    public void fullConstructor_negativeDownvotes_clampsToZero() {
        Comment c = new Comment(java.util.UUID.randomUUID(), "text", java.util.UUID.randomUUID(),
                java.time.LocalDateTime.now(), false, 0, -3, null);
        assertEquals(0, c.getDownvoteCount());
    }

    @Test
    public void fullConstructor_nullReplies_defaultsToEmptyList() {
        Comment c = new Comment(java.util.UUID.randomUUID(), "text", java.util.UUID.randomUUID(),
                java.time.LocalDateTime.now(), false, 0, 0, null);
        assertNotNull(c.getReplies());
        assertTrue(c.getReplies().isEmpty());
    }

    // upvote / downvote / getVoteScore

    @Test
    public void upvote_incrementsUpvoteCount() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.upvote();
        assertEquals(1, c.getUpvoteCount());
    }

    @Test
    public void upvote_calledMultipleTimes_incrementsCorrectly() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.upvote();
        c.upvote();
        c.upvote();
        assertEquals(3, c.getUpvoteCount());
    }

    @Test
    public void downvote_incrementsDownvoteCount() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.downvote();
        assertEquals(1, c.getDownvoteCount());
    }

    @Test
    public void downvote_calledMultipleTimes_incrementsCorrectly() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.downvote();
        c.downvote();
        assertEquals(2, c.getDownvoteCount());
    }

    @Test
    public void getVoteScore_returnsUpvotesMinusDownvotes() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.upvote();
        c.upvote();
        c.downvote();
        assertEquals(1, c.getVoteScore());
    }

    @Test
    public void getVoteScore_noVotes_returnsZero() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        assertEquals(0, c.getVoteScore());
    }

    @Test
    public void getVoteScore_moreDownvotes_returnsNegative() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.downvote();
        c.downvote();
        c.upvote();
        assertEquals(-1, c.getVoteScore());
    }

    // addReply

    @Test
    public void addReply_valid_addsReply() {
        Comment c = new Comment("parent", java.util.UUID.randomUUID());
        c.addReply(new Comment("reply", java.util.UUID.randomUUID()));
        assertEquals(1, c.getReplies().size());
    }

    @Test
    public void addReply_null_doesNotAdd() {
        Comment c = new Comment("parent", java.util.UUID.randomUUID());
        c.addReply(null);
        assertEquals(0, c.getReplies().size());
    }

    @Test
    public void addReply_multipleReplies_addsAll() {
        Comment c = new Comment("parent", java.util.UUID.randomUUID());
        c.addReply(new Comment("r1", java.util.UUID.randomUUID()));
        c.addReply(new Comment("r2", java.util.UUID.randomUUID()));
        c.addReply(new Comment("r3", java.util.UUID.randomUUID()));
        assertEquals(3, c.getReplies().size());
    }

    // deleteReply

    @Test
    public void deleteReply_byAuthor_returnsTrue() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Comment parent = new Comment("parent", java.util.UUID.randomUUID());
        Comment reply = new Comment("reply", authorId);
        parent.addReply(reply);
        assertTrue(parent.deleteReply(reply.getCommentId(), authorId, false));
        assertEquals(0, parent.getReplies().size());
    }

    @Test
    public void deleteReply_byAdmin_returnsTrue() {
        Comment parent = new Comment("parent", java.util.UUID.randomUUID());
        Comment reply = new Comment("reply", java.util.UUID.randomUUID());
        parent.addReply(reply);
        assertTrue(parent.deleteReply(reply.getCommentId(), java.util.UUID.randomUUID(), true));
    }

    @Test
    public void deleteReply_byNonAuthor_returnsFalse() {
        Comment parent = new Comment("parent", java.util.UUID.randomUUID());
        Comment reply = new Comment("reply", java.util.UUID.randomUUID());
        parent.addReply(reply);
        assertFalse(parent.deleteReply(reply.getCommentId(), java.util.UUID.randomUUID(), false));
    }

    @Test
    public void deleteReply_nullCommentId_returnsFalse() {
        Comment parent = new Comment("parent", java.util.UUID.randomUUID());
        assertFalse(parent.deleteReply(null, java.util.UUID.randomUUID(), false));
    }

    @Test
    public void deleteReply_nullActingUser_returnsFalse() {
        Comment parent = new Comment("parent", java.util.UUID.randomUUID());
        Comment reply = new Comment("reply", java.util.UUID.randomUUID());
        parent.addReply(reply);
        assertFalse(parent.deleteReply(reply.getCommentId(), null, false));
    }

    @Test
    public void deleteReply_nestedReply_returnsTrue() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Comment root = new Comment("root", java.util.UUID.randomUUID());
        Comment child = new Comment("child", java.util.UUID.randomUUID());
        Comment grandchild = new Comment("grandchild", authorId);
        child.addReply(grandchild);
        root.addReply(child);
        assertTrue(root.deleteReply(grandchild.getCommentId(), authorId, false));
    }

    @Test
    public void deleteReply_nonExistentId_returnsFalse() {
        Comment parent = new Comment("parent", java.util.UUID.randomUUID());
        parent.addReply(new Comment("reply", java.util.UUID.randomUUID()));
        assertFalse(parent.deleteReply(java.util.UUID.randomUUID(), java.util.UUID.randomUUID(), true));
    }

    // Setters

    @Test
    public void setTimestamp_updatesTimestamp() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        java.time.LocalDateTime newTime = java.time.LocalDateTime.of(2020, 1, 1, 0, 0);
        c.setTimestamp(newTime);
        assertEquals(newTime, c.getTimestamp());
    }

    @Test
    public void setEdited_updatesFlag() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        assertFalse(c.isEdited());
        c.setEdited(true);
        assertTrue(c.isEdited());
    }

    @Test
    public void setUpvoteCount_negativeValue_clampsToZero() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.setUpvoteCount(-5);
        assertEquals(0, c.getUpvoteCount());
    }

    @Test
    public void setUpvoteCount_positiveValue_setsCorrectly() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.setUpvoteCount(10);
        assertEquals(10, c.getUpvoteCount());
    }

    @Test
    public void setDownvoteCount_negativeValue_clampsToZero() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.setDownvoteCount(-3);
        assertEquals(0, c.getDownvoteCount());
    }

    @Test
    public void setDownvoteCount_positiveValue_setsCorrectly() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.setDownvoteCount(7);
        assertEquals(7, c.getDownvoteCount());
    }

    @Test
    public void setReplies_null_setsEmptyList() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        c.setReplies(null);
        assertNotNull(c.getReplies());
        assertTrue(c.getReplies().isEmpty());
    }

    @Test
    public void setReplies_validList_setsReplies() {
        Comment c = new Comment("text", java.util.UUID.randomUUID());
        List<Comment> replies = new ArrayList<>();
        replies.add(new Comment("r1", java.util.UUID.randomUUID()));
        c.setReplies(replies);
        assertEquals(1, c.getReplies().size());
    }
}
