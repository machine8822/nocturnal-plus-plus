package com.model;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Coverage tests for Answer class
 */
public class AnswerTest {

    @Test
    public void answer_constructor_setsFields() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Answer a = new Answer("int x = 1;", "Explanation text", authorId);
        assertEquals("int x = 1;", a.getCodeSnippet());
        assertEquals("Explanation text", a.getExplanation());
        assertEquals(authorId, a.getAuthorId());
        assertNotNull(a.getAnswerId());
        assertNotNull(a.getCreatedAt());
    }

    @Test
    public void answer_constructor_nullCodeSnippet_defaultsToEmpty() {
        Answer a = new Answer(null, "exp", java.util.UUID.randomUUID());
        assertEquals("", a.getCodeSnippet());
    }

    @Test
    public void answer_constructor_nullExplanation_defaultsToEmpty() {
        Answer a = new Answer("code", null, java.util.UUID.randomUUID());
        assertEquals("", a.getExplanation());
    }

    @Test
    public void answer_hasCodeSnippet_withCode_returnsTrue() {
        Answer a = new Answer("int x = 1;", "exp", java.util.UUID.randomUUID());
        assertTrue(a.hasCodeSnippet());
    }

    @Test
    public void answer_hasCodeSnippet_emptyCode_returnsFalse() {
        Answer a = new Answer("", "exp", java.util.UUID.randomUUID());
        assertFalse(a.hasCodeSnippet());
    }

    @Test
    public void answer_hasCodeSnippet_blankCode_returnsFalse() {
        Answer a = new Answer("   ", "exp", java.util.UUID.randomUUID());
        assertFalse(a.hasCodeSnippet());
    }

    @Test
    public void answer_hasExplanation_withText_returnsTrue() {
        Answer a = new Answer("code", "Some explanation", java.util.UUID.randomUUID());
        assertTrue(a.hasExplanation());
    }

    @Test
    public void answer_hasExplanation_emptyText_returnsFalse() {
        Answer a = new Answer("code", "", java.util.UUID.randomUUID());
        assertFalse(a.hasExplanation());
    }

    @Test
    public void answer_upvote_incrementsCount() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.upvote();
        assertEquals(1, a.getUpvoteCount());
    }

    @Test
    public void answer_downvote_incrementsCount() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.downvote();
        assertEquals(1, a.getDownvoteCount());
    }

    @Test
    public void answer_getVoteScore_returnsUpvotesMinusDownvotes() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.upvote();
        a.upvote();
        a.upvote();
        a.downvote();
        assertEquals(2, a.getVoteScore());
    }

    @Test
    public void answer_addComment_valid_addsComment() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.addComment(new Comment("nice answer", java.util.UUID.randomUUID()));
        assertEquals(1, a.getCommentCount());
    }

    @Test
    public void answer_addComment_null_doesNotAdd() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.addComment(null);
        assertEquals(0, a.getCommentCount());
    }

    @Test
    public void answer_deleteComment_byAuthor_returnsTrue() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        Comment c = new Comment("comment", authorId);
        a.addComment(c);
        assertTrue(a.deleteComment(c.getCommentId(), authorId, false));
        assertEquals(0, a.getCommentCount());
    }

    @Test
    public void answer_deleteComment_byAdmin_returnsTrue() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        Comment c = new Comment("comment", java.util.UUID.randomUUID());
        a.addComment(c);
        assertTrue(a.deleteComment(c.getCommentId(), java.util.UUID.randomUUID(), true));
    }

    @Test
    public void answer_deleteComment_byNonAuthor_returnsFalse() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        Comment c = new Comment("comment", java.util.UUID.randomUUID());
        a.addComment(c);
        assertFalse(a.deleteComment(c.getCommentId(), java.util.UUID.randomUUID(), false));
    }

    @Test
    public void answer_deleteComment_nullCommentId_returnsFalse() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        assertFalse(a.deleteComment(null, java.util.UUID.randomUUID(), false));
    }

    @Test
    public void answer_deleteComment_nullActingUser_returnsFalse() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        Comment c = new Comment("comment", java.util.UUID.randomUUID());
        a.addComment(c);
        assertFalse(a.deleteComment(c.getCommentId(), null, false));
    }

    @Test
    public void answer_deleteComment_nestedReply_returnsTrue() {
        java.util.UUID authorId = java.util.UUID.randomUUID();
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        Comment parent = new Comment("comment", java.util.UUID.randomUUID());
        Comment reply = new Comment("reply", authorId);
        parent.addReply(reply);
        a.addComment(parent);
        assertTrue(a.deleteComment(reply.getCommentId(), authorId, false));
    }

    @Test
    public void answer_setCodeSnippet_null_setsEmpty() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.setCodeSnippet(null);
        assertEquals("", a.getCodeSnippet());
    }

    @Test
    public void answer_setExplanation_null_setsEmpty() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.setExplanation(null);
        assertEquals("", a.getExplanation());
    }

    @Test
    public void answer_setUpvoteCount_negativeValue_clampsToZero() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.setUpvoteCount(-10);
        assertEquals(0, a.getUpvoteCount());
    }

    @Test
    public void answer_setDownvoteCount_negativeValue_clampsToZero() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.setDownvoteCount(-10);
        assertEquals(0, a.getDownvoteCount());
    }

    @Test
    public void answer_fullConstructor_negativeVotes_clampToZero() {
        Answer a = new Answer(java.util.UUID.randomUUID(), "code", "exp", -5, -3,
                java.util.UUID.randomUUID(), java.time.LocalDateTime.now(), null);
        assertEquals(0, a.getUpvoteCount());
        assertEquals(0, a.getDownvoteCount());
    }

    @Test
    public void answer_setComments_null_setsEmptyList() {
        Answer a = new Answer("code", "exp", java.util.UUID.randomUUID());
        a.setComments(null);
        assertNotNull(a.getComments());
        assertTrue(a.getComments().isEmpty());
    }
}
