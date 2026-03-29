package com.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;

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

}
