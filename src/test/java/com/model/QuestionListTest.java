package com.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Coverage tests for QuestionList class
 */
public class QuestionListTest {

    @Before
    public void resetSingletons() throws Exception {
        Field userListField = UserList.class.getDeclaredField("instance");
        userListField.setAccessible(true);
        userListField.set(null, null);

        Field questionListField = QuestionList.class.getDeclaredField("instance");
        questionListField.setAccessible(true);
        questionListField.set(null, null);
    }

    @After
    public void resetSingletonsAfter() throws Exception {
        resetSingletons();
    }

    // --- addQuestion ---

    @Test
    public void addQuestion_valid_returnsTrue() {
        QuestionList ql = QuestionList.getInstance();
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        assertTrue(ql.addQuestion(q));
    }

    @Test
    public void addQuestion_null_returnsFalse() {
        assertFalse(QuestionList.getInstance().addQuestion(null));
    }

    @Test
    public void addQuestion_duplicateId_returnsFalse() {
        QuestionList ql = QuestionList.getInstance();
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        ql.addQuestion(q);
        assertFalse(ql.addQuestion(q));
    }

    // --- getQuestion ---

    @Test
    public void getQuestion_existing_returnsQuestion() {
        QuestionList ql = QuestionList.getInstance();
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        ql.addQuestion(q);
        assertSame(q, ql.getQuestion(q.getQuestionId()));
    }

    @Test
    public void getQuestion_null_returnsNull() {
        assertNull(QuestionList.getInstance().getQuestion(null));
    }

    @Test
    public void getQuestion_nonExistent_returnsNull() {
        assertNull(QuestionList.getInstance().getQuestion(java.util.UUID.randomUUID()));
    }

    // --- removeQuestion ---

    @Test
    public void removeQuestion_existing_returnsTrue() {
        QuestionList ql = QuestionList.getInstance();
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        ql.addQuestion(q);
        assertTrue(ql.removeQuestion(q.getQuestionId()));
        assertNull(ql.getQuestion(q.getQuestionId()));
    }

    @Test
    public void removeQuestion_null_returnsFalse() {
        assertFalse(QuestionList.getInstance().removeQuestion(null));
    }

    @Test
    public void removeQuestion_nonExistent_returnsFalse() {
        assertFalse(QuestionList.getInstance().removeQuestion(java.util.UUID.randomUUID()));
    }

    // --- getAll ---

    @Test
    public void getAll_returnsNewListEachTime() {
        QuestionList ql = QuestionList.getInstance();
        assertNotSame(ql.getAll(), ql.getAll());
    }

    @Test
    public void getAll_containsAddedQuestion() {
        QuestionList ql = QuestionList.getInstance();
        InterviewQuestion q = new InterviewQuestion(
                "Title", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        ql.addQuestion(q);
        assertTrue(ql.getAll().contains(q));
    }

    // --- getFirstQuestion ---

    @Test
    public void getFirstQuestion_emptyList_returnsNull() {
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        assertNull(ql.getFirstQuestion());
    }

    @Test
    public void getFirstQuestion_nonEmpty_returnsFirst() {
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        InterviewQuestion q1 = new InterviewQuestion(
                "First", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        InterviewQuestion q2 = new InterviewQuestion(
                "Second", "desc", Difficulty.HARD, Category.LINKED_LIST, QuestionType.CODING, null);
        ql.addQuestion(q1);
        ql.addQuestion(q2);
        assertSame(q1, ql.getFirstQuestion());
    }

    // --- getByCategory ---

    @Test
    public void getByCategory_matchingCategory_returnsMatches() {
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        InterviewQuestion arrayQ = new InterviewQuestion(
                "Array Q", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        InterviewQuestion llQ = new InterviewQuestion(
                "LL Q", "desc", Difficulty.EASY, Category.LINKED_LIST, QuestionType.CODING, null);
        ql.addQuestion(arrayQ);
        ql.addQuestion(llQ);
        List<InterviewQuestion> result = ql.getByCategory(Category.ARRAY);
        assertEquals(1, result.size());
        assertSame(arrayQ, result.get(0));
    }

    @Test
    public void getByCategory_null_returnsEmptyList() {
        assertTrue(QuestionList.getInstance().getByCategory(null).isEmpty());
    }

    @Test
    public void getByCategory_noMatch_returnsEmptyList() {
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        assertTrue(ql.getByCategory(Category.BIG_O).isEmpty());
    }

    // --- getByDifficulty ---

    @Test
    public void getByDifficulty_matchingDifficulty_returnsMatches() {
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        InterviewQuestion easyQ = new InterviewQuestion(
                "Easy Q", "desc", Difficulty.EASY, Category.ARRAY, QuestionType.CODING, null);
        InterviewQuestion hardQ = new InterviewQuestion(
                "Hard Q", "desc", Difficulty.HARD, Category.ARRAY, QuestionType.CODING, null);
        ql.addQuestion(easyQ);
        ql.addQuestion(hardQ);
        List<InterviewQuestion> result = ql.getByDifficulty(Difficulty.EASY);
        assertEquals(1, result.size());
        assertSame(easyQ, result.get(0));
    }

    @Test
    public void getByDifficulty_null_returnsEmptyList() {
        assertTrue(QuestionList.getInstance().getByDifficulty(null).isEmpty());
    }

    @Test
    public void getByDifficulty_noMatch_returnsEmptyList() {
        QuestionList ql = QuestionList.getInstance();
        for (InterviewQuestion q : new ArrayList<>(ql.getAll())) {
            ql.removeQuestion(q.getQuestionId());
        }
        assertTrue(ql.getByDifficulty(Difficulty.MEDIUM).isEmpty());
    }
}
