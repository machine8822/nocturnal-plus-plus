package com.model;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.UUID;

public class InterviewQuestionTest {

    private InterviewQuestion createQuestion(){
        return new InterviewQuestion(
            "Title",
            "Description",
            Difficulty.EASY,
            Category.ARRAY,
            QuestionType.CODING,
            UUID.randomUUID()
        );
    }

    @Test
    public void testGetSuccessRate_NoAttempts(){
        InterviewQuestion q = createQuestion();

        double rate = q.getSuccessRate();

        assertEquals(0.0, rate, 0.0001);
    }

    @Test
    public void testGetSuccessRate_AllCorrect() {
        InterviewQuestion q = createQuestion();

        q.recordAttempt(true);
        q.recordAttempt(true);
        q.recordAttempt(true);

        double rate = q.getSuccessRate();

        assertEquals(1.0, rate, 0.0001);
    }

    @Test
    public void testGetSuccessRate_AllIncorrect() {
        InterviewQuestion q = createQuestion();

        q.recordAttempt(false);
        q.recordAttempt(false);
        q.recordAttempt(false);

        double rate = q.getSuccessRate();

        assertEquals(0.0, rate, 0.0001);
    }

    @Test
    public void testGetSuccessRate_Mixed() {
        InterviewQuestion q = createQuestion();

        q.recordAttempt(true);
        q.recordAttempt(false);
        q.recordAttempt(true);
        q.recordAttempt(false);

        double rate = q.getSuccessRate();

        assertEquals(0.5, rate, 0.0001);
    }

    // ----------- Record Attempt -----------

    @Test
    public void testRecordAttempt_IncrementsCounts() {
        InterviewQuestion q = createQuestion();

        q.recordAttempt(true);
        q.recordAttempt(false);

        assertEquals(2, q.getTotalAttempts());
        assertEquals(1, q.getTotalSuccesses());
    }

    // ----------- Sections -----------

    @Test
    public void testGetSection_OutOfBounds() {
        InterviewQuestion q = createQuestion();

        assertNull(q.getSection(0)); // no sections added yet
    }

    @Test
    public void testAddSection_NullIgnored() {
        InterviewQuestion q = createQuestion();

        q.addSection(null);

        assertTrue(q.getSections().isEmpty());
    }

    // ----------- Comments -----------

    @Test
    public void testAddComment_NullIgnored() {
        InterviewQuestion q = createQuestion();

        q.addComment(null);

        assertTrue(q.getComments().isEmpty());
    }

    // ----------- Update Content -----------

    @Test
    public void testUpdateContent_BlankIgnored() {
        InterviewQuestion q = createQuestion();

        q.updateContent("", "");

        assertEquals("Title", q.getTitle());
        assertEquals("Description", q.getDescription());
    }

    @Test
    public void testUpdateContent_ValidUpdate() {
        InterviewQuestion q = createQuestion();

        q.updateContent("New Title", "New Description");

        assertEquals("New Title", q.getTitle());
        assertEquals("New Description", q.getDescription());
    }

    // ----------- Image URL -----------

    @Test
    public void testSetImageURL_NullDefaultsToEmpty() {
        InterviewQuestion q = createQuestion();

        q.setImageURL(null);

        assertEquals("", q.getImageURL());
    }

    // ----------- Delete Comment (basic safety) -----------

    @Test
    public void testDeleteComment_InvalidInputs() {
        InterviewQuestion q = createQuestion();

        boolean result = q.deleteComment(null, null, false);

        assertFalse(result);
    }

    @Test
    public void testDeleteAnswer_InvalidInputs() {
        InterviewQuestion q = createQuestion();

        boolean result = q.deleteAnswer(null, null, false);

        assertFalse(result);
    }

}
