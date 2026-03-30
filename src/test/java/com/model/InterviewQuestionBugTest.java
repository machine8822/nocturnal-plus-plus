package com.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class InterviewQuestionBugTest {

    // Bug #65 — InterviewQuestion title null check is missing

    @Test
    public void updateContent_withValidTitle_updatesTitle() {
        InterviewQuestion q = new InterviewQuestion(
                "Old Title", "Old Desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        q.updateContent("New Title", "New Desc");
        assertEquals("New Title", q.getTitle());
    }

    @Test
    public void updateContent_withNullTitle_doesNotChangeTitle() {
        InterviewQuestion q = new InterviewQuestion(
                "Original", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        q.updateContent(null, "New Desc");
        assertEquals("Original", q.getTitle());
    }

    @Test
    public void updateContent_withBlankTitle_doesNotChangeTitle() {
        InterviewQuestion q = new InterviewQuestion(
                "Original", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        q.updateContent("   ", "New Desc");
        assertEquals("Original", q.getTitle());
    }

    @Test
    public void interviewQuestion_constructedWithNullTitle_titleIsNull() {
        InterviewQuestion q = new InterviewQuestion(
                null, "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        assertNull(q.getTitle());
    }

    @Test
    public void updateContent_onNullTitleQuestion_setsNewTitle() {
        InterviewQuestion q = new InterviewQuestion(
                null, "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        q.updateContent("Fixed Title", "desc");
        assertEquals("Fixed Title", q.getTitle());
    }

    @Test
    public void updateContent_updatesLastUpdatedTimestamp() throws InterruptedException {
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        java.time.LocalDateTime before = q.getLastUpdated();
        Thread.sleep(10);
        q.updateContent("New Title", "New Desc");
        assertTrue(q.getLastUpdated().isAfter(before));
    }

    // Bug #74 — updateContent() bumps lastUpdated even when nothing changes

    @Test
    public void updateContent_nullTitleAndNullDescription_stillUpdatesTimestamp() throws InterruptedException {
        InterviewQuestion q = new InterviewQuestion(
                "Title", "Desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        java.time.LocalDateTime before = q.getLastUpdated();
        Thread.sleep(10);
        q.updateContent(null, null);
        assertTrue(q.getLastUpdated().isAfter(before));
    }

    @Test
    public void updateContent_blankTitleAndBlankDescription_stillUpdatesTimestamp() throws InterruptedException {
        InterviewQuestion q = new InterviewQuestion(
                "Title", "Desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        java.time.LocalDateTime before = q.getLastUpdated();
        Thread.sleep(10);
        q.updateContent("   ", "   ");
        assertTrue(q.getLastUpdated().isAfter(before));
    }

    @Test
    public void updateContent_validInputs_correctlyUpdatesTimestamp() throws InterruptedException {
        InterviewQuestion q = new InterviewQuestion(
                "Title", "Desc", Difficulty.EASY,
                Category.ARRAY, QuestionType.CODING, null);
        java.time.LocalDateTime before = q.getLastUpdated();
        Thread.sleep(10);
        q.updateContent("New Title", "New Desc");
        assertTrue(q.getLastUpdated().isAfter(before));
    }
}
