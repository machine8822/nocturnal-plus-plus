package com.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Super simple scenario stub for a daily task flow.
 */
public class DailyTaskScenario {

    public static void main(String[] args) {
        runDailyTaskScenario();
    }

    public static void runDailyTaskScenario() {
        User jimmy = new User("jimmy.bauer@example.com", "TempPass1!", "Jimmy", "Bauer");
        int startingStreak = 8;

        // Super simple hard-coded user preferences for this scenario driver.
        Map<String, String> preferredTopicsByEmail = new HashMap<>();
        preferredTopicsByEmail.put("jimmy.bauer@example.com", "Trees + Visualization");

        Map<String, String> skillLevelByEmail = new HashMap<>();
        skillLevelByEmail.put("jimmy.bauer@example.com", "Intermediate");

        System.out.println("Jimmy logs into the system.");
        System.out.println("Current streak: " + startingStreak + " days");

        DailyTaskAssignment assignment = assignDailyTaskForUser(
                jimmy,
                startingStreak,
                preferredTopicsByEmail,
                skillLevelByEmail);

        InterviewQuestion dailyChallenge = assignment.question;
        System.out.println("\nDaily challenge selected for Jimmy (tailored by skill + preference):");
        System.out.println("- Skill level: " + assignment.skillLevel);
        System.out.println("- Preference: " + assignment.preference);
        System.out.println("- Question: " + dailyChallenge.getTitle());

        Section mainSection = dailyChallenge.getSection(0);
        if (mainSection == null) {
            System.out.println("No section available in stub.");
            return;
        }

        System.out.println("\nJimmy clicks the question and reviews solutions:");
        printSolutions(mainSection.getAnswers());

        if (mainSection.getAnswers().size() >= 2) {
            Answer secondSolution = mainSection.getAnswers().get(1);
            String commentText = "I am confused on solution 2. Why does this always stay O(log n)?";
            Comment jimmyComment = new Comment(commentText, jimmy.getUserId());
            secondSolution.addComment(jimmyComment);

            System.out.println("\nJimmy posts a comment on solution #2:");
            System.out.println("Name: " + jimmy.getFirstName() + " " + jimmy.getLastName());
            System.out.println("Date: " + LocalDate.now());
            System.out.println("Question: " + commentText);
        }

        Path outputPath = Paths.get("docs", "daily-task-question.txt");
        try {
            writeQuestionToTextFile(dailyChallenge, mainSection.getAnswers(), outputPath);
            System.out.println("\nPrinted to formatted text file: " + outputPath);
        } catch (IOException e) {
            System.out.println("\nCould not write text file: " + e.getMessage());
        }

        List<InterviewQuestion> allQuestions = buildQuestionPool(jimmy, dailyChallenge);
        List<InterviewQuestion> bstResults = searchByTitle(allQuestions, "Binary Search Tree");

        System.out.println("\nJimmy searches all questions for: Binary Search Tree");
        System.out.println("Matches found: " + bstResults.size());
        for (InterviewQuestion q : bstResults) {
            System.out.println("- " + q.getTitle());
        }

        int endingStreak = assignment.streakAfterCompletion;
        System.out.println("\nDaily streak increased to: " + endingStreak);
        System.out.println("Jimmy logs out.");

        // TODO finish soon: replace hard-coded maps with real user profile and recommendation data.
    }

    private static DailyTaskAssignment assignDailyTaskForUser(
            User user,
            int currentStreak,
            Map<String, String> preferredTopicsByEmail,
            Map<String, String> skillLevelByEmail) {

        String email = user.getEmail() == null ? "" : user.getEmail();
        String skillLevel = skillLevelByEmail.getOrDefault(email, "Beginner");
        String preference = preferredTopicsByEmail.getOrDefault(email, "Arrays + Strings");

        InterviewQuestion selected = buildDailyChallenge(user, skillLevel, preference);
        int streakAfterCompletion = currentStreak + 1;

        return new DailyTaskAssignment(selected, skillLevel, preference, streakAfterCompletion);
    }

    private static InterviewQuestion buildDailyChallenge(User jimmy, String skillLevel, String preference) {
        InterviewQuestion q = new InterviewQuestion(
                "Binary Search Tree: Validate and Find Kth Smallest",
                "Given a binary search tree, validate it and return the kth smallest element.",
                Difficulty.MEDIUM,
            Category.ARRAY,
                QuestionType.SHORT_ANSWER,
                jimmy.getUserId());

        Section problem = new Section(
                "Problem Statement",
                "You are given a root node and integer k. Check BST validity, then return kth smallest value.",
                DataType.STRING,
                SectionType.DESCRIPTION);

        problem.addConstraint("1 <= number of nodes <= 10^5");
        problem.addConstraint("Tree values are in 32-bit integer range");
        problem.addExample("Input: [5,3,7,2,4,6,8], k=3 -> Output: 4");
        problem.addExample("Input: [2,1,3], k=1 -> Output: 1");
        problem.setExpectedTimeComplexity("Target: O(n) for validation, O(h+k) for kth lookup");

        String solutionOneCode = "// Solution 1: inorder traversal stores all values\n"
                + "List<Integer> vals = new ArrayList<>();\n"
                + "inorder(root, vals);\n"
                + "return vals.get(k - 1);";

        String solutionTwoCode = "// Solution 2: iterative inorder with stack\n"
                + "Stack<Node> st = new Stack<>();\n"
                + "while (root != null || !st.isEmpty()) { ... }";

        Answer solutionOne = new Answer(
                solutionOneCode,
                "Traverse in-order, collect sorted values, return index k-1. Simple and readable.",
                jimmy.getUserId());

        Answer solutionTwo = new Answer(
                solutionTwoCode,
                "Use stack-based in-order and stop early after visiting k nodes. Better memory in practice.",
                jimmy.getUserId());

        problem.addAnswer(solutionOne);
        problem.addAnswer(solutionTwo);
        q.addSection(problem);

        // Basic metadata for presentation.
        q.addComment(new Comment("Tailored for " + skillLevel + " with preference: " + preference, jimmy.getUserId()));

        return q;
    }

    private static void printSolutions(List<Answer> solutions) {
        for (int i = 0; i < solutions.size(); i++) {
            Answer answer = solutions.get(i);
            System.out.println("\nSolution " + (i + 1));
            System.out.println("Explanation: " + answer.getExplanation());
            System.out.println("Code snippet:\n" + answer.getCodeSnippet());
        }
    }

    private static List<InterviewQuestion> buildQuestionPool(User jimmy, InterviewQuestion dailyChallenge) {
        List<InterviewQuestion> questions = new ArrayList<>();
        questions.add(dailyChallenge);

        InterviewQuestion bstOne = new InterviewQuestion(
                "Binary Search Tree Basics",
                "Implement insert and search in BST.",
                Difficulty.EASY,
            Category.ARRAY,
                QuestionType.SHORT_ANSWER,
                jimmy.getUserId());

        InterviewQuestion bstTwo = new InterviewQuestion(
                "Binary Search Tree Deletion Deep Dive",
                "Delete a node from BST and preserve ordering.",
                Difficulty.MEDIUM,
            Category.ARRAY,
                QuestionType.SHORT_ANSWER,
                jimmy.getUserId());

        InterviewQuestion other = new InterviewQuestion(
                "Hash Map Two Sum",
                "Return indices for two numbers that add to target.",
                Difficulty.EASY,
                Category.ARRAY,
                QuestionType.SHORT_ANSWER,
                jimmy.getUserId());

        questions.add(bstOne);
        questions.add(bstTwo);
        questions.add(other);

        return questions;
    }

    private static List<InterviewQuestion> searchByTitle(List<InterviewQuestion> questions, String text) {
        List<InterviewQuestion> results = new ArrayList<>();
        String needle = text.toLowerCase();

        for (InterviewQuestion q : questions) {
            if (q.getTitle() != null && q.getTitle().toLowerCase().contains(needle)) {
                results.add(q);
            }
        }

        return results;
    }

    private static void writeQuestionToTextFile(InterviewQuestion question, List<Answer> answers, Path outputPath)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("NOCTURNAL++ OFFLINE REVIEW\n");
        sb.append("==========================\n\n");
        sb.append("Title: ").append(question.getTitle()).append("\n");
        sb.append("Difficulty: ").append(question.getDifficulty()).append("\n");
        sb.append("Category: ").append(question.getCategory()).append("\n\n");
        sb.append("Description:\n").append(question.getDescription()).append("\n\n");

        if (question.getSection(0) != null) {
            Section sec = question.getSection(0);
            sb.append("Constraints:\n");
            for (String c : sec.getConstraints()) {
                sb.append("- ").append(c).append("\n");
            }
            sb.append("\nExamples:\n");
            for (String ex : sec.getExamples()) {
                sb.append("- ").append(ex).append("\n");
            }
            sb.append("\n");
        }

        sb.append("Solutions:\n");
        for (int i = 0; i < answers.size(); i++) {
            Answer a = answers.get(i);
            sb.append("\n[").append(i + 1).append("] ").append(a.getExplanation()).append("\n");
            sb.append(a.getCodeSnippet()).append("\n");
        }

        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, sb.toString(), StandardCharsets.UTF_8);
    }

    private static class DailyTaskAssignment {
        private final InterviewQuestion question;
        private final String skillLevel;
        private final String preference;
        private final int streakAfterCompletion;

        private DailyTaskAssignment(
                InterviewQuestion question,
                String skillLevel,
                String preference,
                int streakAfterCompletion) {
            this.question = question;
            this.skillLevel = skillLevel;
            this.preference = preference;
            this.streakAfterCompletion = streakAfterCompletion;
        }
    }
}
