package com.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class InterviewQuestionTest {

    private InterviewQuestion createQuestion()
    {
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
    void testGetSuccessRate_NoAttempts()
    {
        InterviewQuestion q = createQuestion();

        double rate = q.getSuccessRate();

        assertEquals(0.0, rate);
    }

    @Test

}
