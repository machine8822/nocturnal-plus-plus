package com.model;

<<<<<<< HEAD
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
=======
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class AnswerTest {
    private UUID authorId;
    private UUID otherUserId;
    private UUID adminId;
    private LocalDateTime createdAt;
    private Answer answer;
    private Comment authorTopLevelComment;
    private Comment otherUsersTopLevelComment;
    private Comment nestedParentComment;
    private Comment nestedReply;

    @Before
    public void setUp() {
        authorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        otherUserId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        adminId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        createdAt = LocalDateTime.of(2026, 3, 29, 12, 0, 0);

        answer = new Answer("return sum;", "Use a running total.", authorId);

        authorTopLevelComment = new Comment(
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                "Author comment",
                authorId,
                createdAt,
                false,
                0,
                0,
                new ArrayList<>());
        otherUsersTopLevelComment = new Comment(
                UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                "Other user comment",
                otherUserId,
                createdAt,
                false,
                0,
                0,
                new ArrayList<>());
        nestedReply = new Comment(
                UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                "Nested reply",
                authorId,
                createdAt,
                false,
                0,
                0,
                new ArrayList<>());
        nestedParentComment = new Comment(
                UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"),
                "Nested parent",
                otherUserId,
                createdAt,
                false,
                0,
                0,
                new ArrayList<>());
        nestedParentComment.addReply(nestedReply);
    }

    @Test
    public void constructor_withBasicInputs_setsDefaultVotesAndEmptyComments() {
        Answer localAnswer = new Answer("return sum;", "Use a running total.", authorId);

        assertEquals(0, localAnswer.getUpvoteCount());
        assertEquals(0, localAnswer.getDownvoteCount());
        assertEquals(0, localAnswer.getCommentCount());
        assertEquals(authorId, localAnswer.getAuthorId());
    }

    @Test
    public void constructor_withBasicInputs_generatesIdAndTimestamp() {
        Answer localAnswer = new Answer("return sum;", "Use a running total.", authorId);

        assertNotNull(localAnswer.getAnswerId());
        assertNotNull(localAnswer.getCreatedAt());
    }

    @Test
    public void fullConstructor_withNullAndNegativeInputs_normalizesValues() {
        Answer localAnswer = new Answer(null, null, null, -4, -2, authorId, null, null);

        assertNotNull(localAnswer.getAnswerId());
        assertEquals("", localAnswer.getCodeSnippet());
        assertEquals("", localAnswer.getExplanation());
        assertEquals(0, localAnswer.getUpvoteCount());
        assertEquals(0, localAnswer.getDownvoteCount());
        assertEquals(authorId, localAnswer.getAuthorId());
        assertNotNull(localAnswer.getCreatedAt());
        assertTrue(localAnswer.getComments().isEmpty());
    }

    @Test
    public void fullConstructor_withProvidedComments_makesDefensiveCopy() {
        List<Comment> originalComments = new ArrayList<>();
        originalComments.add(authorTopLevelComment);

        Answer localAnswer = new Answer(
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                "code",
                "explanation",
                1,
                0,
                authorId,
                createdAt,
                originalComments);
        originalComments.add(otherUsersTopLevelComment);

        assertEquals(1, localAnswer.getComments().size());
        assertSame(authorTopLevelComment, localAnswer.getComments().get(0));
    }

    @Test
    public void setCodeSnippet_withNull_storesEmptyString() {
        answer.setCodeSnippet(null);

        assertEquals("", answer.getCodeSnippet());
    }

    @Test
    public void setExplanation_withNull_storesEmptyString() {
        answer.setExplanation(null);

        assertEquals("", answer.getExplanation());
    }

    @Test
    public void setComments_withNull_replacesWithEmptyList() {
        answer.addComment(authorTopLevelComment);

        answer.setComments(null);

        assertTrue(answer.getComments().isEmpty());
    }

    @Test
    public void setComments_withProvidedList_makesDefensiveCopy() {
        List<Comment> replacementComments = new ArrayList<>();
        replacementComments.add(authorTopLevelComment);

        answer.setComments(replacementComments);
        replacementComments.add(otherUsersTopLevelComment);

        assertEquals(1, answer.getComments().size());
        assertSame(authorTopLevelComment, answer.getComments().get(0));
    }

    @Test
    public void setUpvoteCount_withNegativeValue_clampsToZero() {
        answer.setUpvoteCount(-5);

        assertEquals(0, answer.getUpvoteCount());
    }

    @Test
    public void setDownvoteCount_withNegativeValue_clampsToZero() {
        answer.setDownvoteCount(-7);

        assertEquals(0, answer.getDownvoteCount());
    }

    @Test
    public void hasCodeSnippet_withNonBlankSnippet_returnsTrue() {
        assertTrue(answer.hasCodeSnippet());
    }

    @Test
    public void hasCodeSnippet_withBlankSnippet_returnsFalse() {
        answer.setCodeSnippet("   ");

        assertFalse(answer.hasCodeSnippet());
    }

    @Test
    public void hasExplanation_withNonBlankExplanation_returnsTrue() {
        assertTrue(answer.hasExplanation());
    }

    @Test
    public void hasExplanation_withBlankExplanation_returnsFalse() {
        answer.setExplanation(null);

        assertFalse(answer.hasExplanation());
    }

    @Test
    public void upvote_incrementsUpvoteCount() {
        answer.upvote();

        assertEquals(1, answer.getUpvoteCount());
    }

    @Test
    public void downvote_incrementsDownvoteCount() {
        answer.downvote();

        assertEquals(1, answer.getDownvoteCount());
    }

    @Test
    public void getVoteScore_returnsUpvotesMinusDownvotes() {
        answer.upvote();
        answer.upvote();
        answer.downvote();

        assertEquals(1, answer.getVoteScore());
    }

    @Test
    public void addComment_withNonNullComment_addsComment() {
        answer.addComment(authorTopLevelComment);

        assertEquals(1, answer.getComments().size());
        assertSame(authorTopLevelComment, answer.getComments().get(0));
    }

    @Test
    public void addComment_withNull_doesNothing() {
        answer.addComment(null);

        assertEquals(0, answer.getCommentCount());
    }

    @Test
    public void getCommentCount_returnsCurrentTopLevelCommentCount() {
        answer.addComment(authorTopLevelComment);
        answer.addComment(otherUsersTopLevelComment);

        assertEquals(2, answer.getCommentCount());
    }

    @Test
    public void deleteComment_withNullCommentId_returnsFalse() {
        answer.addComment(authorTopLevelComment);

        boolean deleted = answer.deleteComment(null, authorId, false);

        assertFalse(deleted);
        assertEquals(1, answer.getCommentCount());
    }

    @Test
    public void deleteComment_withNullActingUserId_returnsFalse() {
        answer.addComment(authorTopLevelComment);

        boolean deleted = answer.deleteComment(authorTopLevelComment.getCommentId(), null, false);

        assertFalse(deleted);
        assertEquals(1, answer.getCommentCount());
    }

    @Test
    public void deleteComment_authorDeletesOwnTopLevelComment_returnsTrueAndRemovesComment() {
        answer.addComment(authorTopLevelComment);

        boolean deleted = answer.deleteComment(authorTopLevelComment.getCommentId(), authorId, false);

        assertTrue(deleted);
        assertTrue(answer.getComments().isEmpty());
    }

    @Test
    public void deleteComment_adminDeletesAnotherUsersTopLevelComment_returnsTrueAndRemovesComment() {
        answer.addComment(otherUsersTopLevelComment);

        boolean deleted = answer.deleteComment(otherUsersTopLevelComment.getCommentId(), adminId, true);

        assertTrue(deleted);
        assertTrue(answer.getComments().isEmpty());
    }

    @Test
    public void deleteComment_nonAuthorNonAdmin_returnsFalseAndKeepsComment() {
        answer.addComment(otherUsersTopLevelComment);

        boolean deleted = answer.deleteComment(otherUsersTopLevelComment.getCommentId(), authorId, false);

        assertFalse(deleted);
        assertEquals(1, answer.getCommentCount());
        assertSame(otherUsersTopLevelComment, answer.getComments().get(0));
    }

    @Test
    public void deleteComment_authorDeletesOwnNestedReply_returnsTrueAndRemovesReply() {
        answer.addComment(nestedParentComment);

        boolean deleted = answer.deleteComment(nestedReply.getCommentId(), authorId, false);

        assertTrue(deleted);
        assertEquals(0, nestedParentComment.getReplies().size());
    }

    @Test
    public void deleteComment_adminDeletesNestedReply_returnsTrueAndRemovesReply() {
        answer.addComment(nestedParentComment);

        boolean deleted = answer.deleteComment(nestedReply.getCommentId(), adminId, true);

        assertTrue(deleted);
        assertEquals(0, nestedParentComment.getReplies().size());
    }

    @Test
    public void deleteComment_withUnknownCommentId_returnsFalse() {
        answer.addComment(authorTopLevelComment);
        answer.addComment(nestedParentComment);

        boolean deleted = answer.deleteComment(
                UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"),
                authorId,
                false);

        assertFalse(deleted);
        assertEquals(2, answer.getCommentCount());
        assertEquals(1, nestedParentComment.getReplies().size());
>>>>>>> 9eae5fdf910f7ec6dd80a2dcb7d3b84cb2863315
    }
}
