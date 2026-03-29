package com.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
    void testGetSuccessRate_NoAttempts(){
        InterviewQuestion q = createQuestion();

        double rate = q.getSuccessRate();

        assertEquals(0.0, rate);
    }

    @Test
    void testGetSuccessRate_AllCorrect() {
        InterviewQuestion q = createQuestion();

        q.recordAttempt(true);
        q.recordAttempt(true);
        q.recordAttempt(true);

        double rate = q.getSuccessRate();

        assertEquals(1.0, rate);
    }

    @Test
    void testGetSuccessRate_AllIncorrect() {
        InterviewQuestion q = createQuestion();

        q.recordAttempt(false);
        q.recordAttempt(false);
        q.recordAttempt(false);

        double rate = q.getSuccessRate();

        assertEquals(0.0, rate);
    }

    @Test
    void testGetSuccessRate_Mixed() {
        InterviewQuestion q = createQuestion();

        q.recordAttempt(true);
        q.recordAttempt(false);
        q.recordAttempt(true);
        q.recordAttempt(false);

        double rate = q.getSuccessRate();

        assertEquals(0.5, rate);
    }

    // ----------- Record Attempt -----------

    @Test
    void testRecordAttempt_IncrementsCounts() {
        InterviewQuestion q = createQuestion();

        q.recordAttempt(true);
        q.recordAttempt(false);

        assertEquals(2, q.getTotalAttempts());
        assertEquals(1, q.getTotalSuccesses());
    }

    // ----------- Sections -----------

    @Test
    void testGetSection_OutOfBounds() {
        InterviewQuestion q = createQuestion();

        assertNull(q.getSection(0)); // no sections added yet
    }

    @Test
    void testAddSection_NullIgnored() {
        InterviewQuestion q = createQuestion();

        q.addSection(null);

        assertTrue(q.getSections().isEmpty());
    }

    // ----------- Comments -----------

    @Test
    void testAddComment_NullIgnored() {
        InterviewQuestion q = createQuestion();

        q.addComment(null);

        assertTrue(q.getComments().isEmpty());
    }

    // ----------- Update Content -----------

    @Test
    void testUpdateContent_BlankIgnored() {
        InterviewQuestion q = createQuestion();

        q.updateContent("", "");

        assertEquals("Title", q.getTitle());
        assertEquals("Description", q.getDescription());
    }

    @Test
    void testUpdateContent_ValidUpdate() {
        InterviewQuestion q = createQuestion();

        q.updateContent("New Title", "New Description");

        assertEquals("New Title", q.getTitle());
        assertEquals("New Description", q.getDescription());
    }

    // ----------- Image URL -----------

    @Test
    void testSetImageURL_NullDefaultsToEmpty() {
        InterviewQuestion q = createQuestion();

        q.setImageURL(null);

        assertEquals("", q.getImageURL());
    }

    // ----------- Delete Comment (basic safety) -----------

    @Test
    void testDeleteComment_InvalidInputs() {
        InterviewQuestion q = createQuestion();

        boolean result = q.deleteComment(null, null, false);

        assertFalse(result);
    }

    @Test
    void testDeleteAnswer_InvalidInputs() {
        InterviewQuestion q = createQuestion();

        boolean result = q.deleteAnswer(null, null, false);

        assertFalse(result);
    }

}
