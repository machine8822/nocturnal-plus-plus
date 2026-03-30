package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
    }
}
