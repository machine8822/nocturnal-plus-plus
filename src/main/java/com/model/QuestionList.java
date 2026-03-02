package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Singleton repository for storing and querying {@link InterviewQuestion} objects
 * by their question ID.
 */
public class QuestionList {

    private static QuestionList instance;
    private HashMap<UUID, InterviewQuestion> questions;

    /**
     * Constructs an empty question repository.
     */
    private QuestionList() {
        questions = new HashMap<>();
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

        UUID id = q.getQuestionId();
        if (questions.containsKey(id)) return false;

        questions.put(id, q);
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
        return questions.get(id);
    }

    /**
     * Removes a question from the repository by ID.
     *
     * @param id the question ID to remove
     * @return {@code true} if a question was removed, otherwise {@code false}
     */
    public boolean removeQuestion(UUID id) {
        if (id == null) return false;
        return questions.remove(id) != null;
    }

    /**
     * Returns a snapshot list of all stored questions.
     *
     * @return a new list containing every question currently in the repository
     */
    public List<InterviewQuestion> getAll() {
        return new ArrayList<>(questions.values());
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

        for (InterviewQuestion q : questions.values()) {
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

        for (InterviewQuestion q : questions.values()) {
            if (q != null && diff.equals(q.getDifficulty())) {
                result.add(q);
            }
        }
        return result;
    }

}
