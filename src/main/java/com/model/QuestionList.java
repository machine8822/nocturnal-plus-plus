package com.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Singleton repository for storing and querying {@link InterviewQuestion} objects.
 */
public class QuestionList {

    private static QuestionList instance;
    private List<InterviewQuestion> questions;

    /**
     * Constructs an empty question repository.
     */
    private QuestionList() {
        questions = new ArrayList<>();
        ArrayList<InterviewQuestion> loaded = DataLoader.loadQuestions();
        for (InterviewQuestion question : loaded) {
            addQuestion(question);
        }
    }

    /**
     * Returns the shared {@code QuestionList} instance, creating it on first use.
     *
     * @return the singleton {@code QuestionList}
     */
    public static QuestionList getInstance() {
        if (instance == null) {
            instance = new QuestionList();
        }
        return instance;
    }

    /**
     * Adds a question to the repository when the question and its ID are valid and
     * that ID is not already present.
     *
     * @param q the question to add
     * @return {@code true} if the question was added, otherwise {@code false}
     */
    public boolean addQuestion(InterviewQuestion q) {
        if (q == null || q.getQuestionId() == null) return false;

        for (InterviewQuestion existing : questions) {
            if (existing != null && q.getQuestionId().equals(existing.getQuestionId())) {
                return false;
            }
        }

        questions.add(q);
        return true;
    }

    /**
     * Retrieves a question by its unique ID.
     *
     * @param id the question ID
     * @return the matching question, or {@code null} if not found or if {@code id}
     *         is {@code null}
     */
    public InterviewQuestion getQuestion(UUID id) {
        if (id == null) return null;
        for (InterviewQuestion q : questions) {
            if (q != null && id.equals(q.getQuestionId())) return q;
        }
        return null;
    }

    /**
     * Removes a question from the repository by ID.
     *
     * @param id the question ID to remove
     * @return {@code true} if a question was removed, otherwise {@code false}
     */
    public boolean removeQuestion(UUID id) {
        if (id == null) return false;
        return questions.removeIf(q -> q != null && id.equals(q.getQuestionId()));
    }

    /**
     * Returns a snapshot list of all stored questions.
     *
     * @return a new list containing every question currently in the repository
     */
    public List<InterviewQuestion> getAll() {
        return new ArrayList<>(questions);
    }

    public InterviewQuestion getFirstQuestion() {
        return questions.isEmpty() ? null : questions.get(0);
    }

    /**
     * Returns all questions matching the specified category.
     *
     * @param cat the category to filter by
     * @return a list of matching questions, or an empty list if {@code cat} is
     *         {@code null} or no matches exist
     */
    public List<InterviewQuestion> getByCategory(Category cat) {
        List<InterviewQuestion> result = new ArrayList<>();
        if (cat == null) return result;

        for (InterviewQuestion q : questions) {
            if (q != null && cat.equals(q.getCategory())) {
                result.add(q);
            }
        }
        return result;
    }

    /**
     * Returns all questions matching the specified difficulty.
     *
     * @param diff the difficulty to filter by
     * @return a list of matching questions, or an empty list if {@code diff} is
     *         {@code null} or no matches exist
     */
    public List<InterviewQuestion> getByDifficulty(Difficulty diff) {
        List<InterviewQuestion> result = new ArrayList<>();
        if (diff == null) return result;

        for (InterviewQuestion q : questions) {
            if (q != null && diff.equals(q.getDifficulty())) {
                result.add(q);
            }
        }
        return result;
    }

}
