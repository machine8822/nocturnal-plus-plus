package com.model;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class CommentTest {
    private Comment comment;
    private UUID authorId;

    @Before
    public void setUp() {
        authorId = UUID.randomUUID();
        comment = new Comment("Testing comments", authorId);
    }

    @Test
    public void testSetText() {
        assertEquals("Should set text correctly", "Testing comments", comment.getText());
    }

    @Test
    public void testSetAuthorId() {
        assertEquals("Should set authorId correctly", authorId, comment.getAuthorId());
    }

    @Test
    public void testEditChange() {
        comment.edit("Updated comment");

        assertEquals("Text should be edited", "Updated comment", comment.getText());
        assertTrue("Is the comment edited?", comment.isEdited());
    }

    @Test
    public void testEditChangeWithNull() {
        comment.edit(null);

        assertEquals("Text edited with null", "", comment.getText());
        assertTrue("Is the null comment edited?", comment.isEdited());
    }

    @Test
    public void testUpvote() {
        comment.upvote();

        assertEquals("Upvote should increase by 1", 1, comment.getUpvoteCount());
    }

    @Test
    public void testDownvote() {
        comment.downvote();

        assertEquals("Downvote should increase by 1", 1, comment.getDownvoteCount());
    }

    @Test
    public void testVoteScore() {
        comment.upvote();
        comment.downvote();
        comment.upvote();

        assertEquals("Vote score should be 1", 1, comment.getVoteScore());
    }

    @Test
    public void testAddReply() {
        Comment reply = new Comment("Reply text", UUID.randomUUID());
        comment.addReply(reply);

        //assertEquals("Reply should be added", 1, comment.getReplies().size());
        assertEquals("The replies should match", reply, comment.getReplies().get(0));
    }

    @Test
    public void testNullReplyNotAdded() {
        comment.addReply(null);

        assertEquals("Null reply should not be added", 0, comment.getReplies().size());
    }

    @Test
    public void testDeleteReplyByAuthor() {
        UUID replyAuthorId = UUID.randomUUID();
        Comment reply = new Comment("Reply text", replyAuthorId);
        comment.addReply(reply);

        boolean result = comment.deleteReply(reply.getCommentId(), replyAuthorId, false);

        assertTrue("Reply author should be able to delete their own reply", result);
        assertEquals("Reply list should be empty after deletion", 0, comment.getReplies().size());
    }


    @Test
    public void testDeleteReplyByNonAuthorFailure() {
        UUID replyAuthorId = UUID.randomUUID();
        UUID differentUserId = UUID.randomUUID();
        Comment reply = new Comment("Reply text", replyAuthorId);
        comment.addReply(reply);

        boolean result = comment.deleteReply(reply.getCommentId(), differentUserId, false);

        assertFalse("Nonauthor should not be able to delete reply", result);
        assertEquals("Reply should remain after failed deletion", 1, comment.getReplies().size());
    }

    @Test
    public void testDeleteReplyByAdmin() {
        UUID replyAuthorId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        Comment reply = new Comment("Reply text", replyAuthorId);
        comment.addReply(reply);

        boolean result = comment.deleteReply(reply.getCommentId(), adminId, true);

        assertTrue("Admin should be able to delete the reply", result);
        assertEquals("Reply list should be empty after admin deletion", 0, comment.getReplies().size());
    }

    @Test
    public void testSetTimestamp() {
        LocalDateTime time = LocalDateTime.of(2026, 3, 02, 6, 07);

        comment.setTimestamp(time);
        comment.setEdited(true);

        assertEquals("Timestamp should be updated", time, comment.getTimestamp());
        assertTrue("Is is edited?", comment.isEdited());
    }

    


    // Bug #68 — edit() allows setting text to blank

    @Test
    public void comment_edit_withBlankText_storesBlankText() {
        comment.edit("   ");
        assertEquals("   ", comment.getText());
    }

    @Test
    public void comment_edit_withBlankText_setsIsEdited() {
        comment.edit("   ");
        assertTrue(comment.isEdited());
    }
}
