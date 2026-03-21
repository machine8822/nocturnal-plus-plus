package com.model;

import java.util.UUID;

public class ContentDeletionTest {

    public static void main(String[] args) {
        testAuthorDeletesOwnQuestionComment();
        testAuthorDeletesOwnAnswer();
        testAdminDeletesNestedReply();
        testNonAuthorCannotDeleteAnotherUsersComment();

        System.out.println("ContentDeletionTest passed.");
    }

    private static void testAuthorDeletesOwnQuestionComment() {
        UUID authorId = UUID.randomUUID();
        InterviewQuestion question = new InterviewQuestion(
                "Two Sum",
                "Find two numbers that add to a target.",
                Difficulty.EASY,
                Category.ARRAY,
                QuestionType.SHORT_ANSWER,
                authorId);

        Comment comment = new Comment("Helpful clarification", authorId);
        question.addComment(comment);

        assertCondition(question.deleteComment(comment.getCommentId(), authorId, false),
                "Author should be able to delete their own question comment.");
        assertCondition(question.getComments().isEmpty(),
                "Question comment should be removed after deletion.");
    }

    private static void testAuthorDeletesOwnAnswer() {
        UUID authorId = UUID.randomUUID();
        InterviewQuestion question = new InterviewQuestion(
                "Reverse Linked List",
                "Reverse a linked list iteratively.",
                Difficulty.MEDIUM,
                Category.LINKED_LIST,
                QuestionType.SHORT_ANSWER,
                authorId);

        Section section = new Section("Explanation", "Describe the approach.", DataType.STRING, SectionType.EXPLANATION);
        Answer answer = new Answer("prev = null;", "Track previous and current nodes.", authorId);
        section.addAnswer(answer);
        question.addSection(section);

        assertCondition(question.deleteAnswer(answer.getAnswerId(), authorId, false),
                "Author should be able to delete their own answer.");
        assertCondition(section.getAnswers().isEmpty(),
                "Answer should be removed from the section after deletion.");
    }

    private static void testAdminDeletesNestedReply() {
        UUID questionAuthorId = UUID.randomUUID();
        UUID commentAuthorId = UUID.randomUUID();
        UUID replyAuthorId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        InterviewQuestion question = new InterviewQuestion(
                "Binary Search",
                "Explain binary search.",
                Difficulty.EASY,
                Category.ARRAY,
                QuestionType.SHORT_ANSWER,
                questionAuthorId);

        Comment rootComment = new Comment("Top-level comment", commentAuthorId);
        Comment reply = new Comment("Nested reply", replyAuthorId);
        rootComment.addReply(reply);
        question.addComment(rootComment);

        assertCondition(question.deleteComment(reply.getCommentId(), adminId, true),
                "Admin should be able to delete another user's nested reply.");
        assertCondition(rootComment.getReplies().isEmpty(),
                "Nested reply should be removed after admin deletion.");
    }

    private static void testNonAuthorCannotDeleteAnotherUsersComment() {
        UUID questionAuthorId = UUID.randomUUID();
        UUID commentAuthorId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        InterviewQuestion question = new InterviewQuestion(
                "Merge Intervals",
                "Explain how to merge intervals.",
                Difficulty.MEDIUM,
                Category.ARRAY,
                QuestionType.SHORT_ANSWER,
                questionAuthorId);

        Section section = new Section("Description", "Walk through the steps.", DataType.STRING, SectionType.DESCRIPTION);
        Comment sectionComment = new Comment("This is another user's comment.", commentAuthorId);
        section.addComment(sectionComment);
        question.addSection(section);

        assertCondition(!question.deleteComment(sectionComment.getCommentId(), otherUserId, false),
                "Non-author non-admin should not be able to delete another user's comment.");
        assertCondition(section.getComments().size() == 1,
                "Section comment should remain when deletion is unauthorized.");
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
