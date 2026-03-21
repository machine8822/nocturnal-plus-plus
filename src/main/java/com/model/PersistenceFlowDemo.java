package com.model;

import java.util.Locale;

/**
 * simple terminal demo for save/load behavior.
 *
 * modes:
 * save: simulates login, add data, logout, which saves
 * load: simulates app startup data load and login checks
 * both: runs save then load
 */
public class PersistenceFlowDemo {

    private static final String VALID_EMAIL = "grant.smith@example.com";
    private static final String VALID_PASSWORD = "123grant";

    // run with "save", "load", or "both" args to see different flows.
    public static void main(String[] args) {
        String mode = args.length > 0 ? args[0].toLowerCase(Locale.ROOT).trim() : "both";

       
        System.out.println("persistence flow demo");
        System.out.println("mode: " + mode);

        // run the flow based on the mode argument
        switch (mode) {
            case "save" -> runSaveFlow();
            case "load" -> runLoadFlow();
            case "both" -> {
                runSaveFlow();
                System.out.println("------------------------------");
                runLoadFlow();
            }
            default -> System.out.println("unknown mode, use save, load, or both");
        }
    }

    private static void runSaveFlow() {
        // save flow: login, add some demo data, then logout to save everything.
        System.out.println("saving login > add data > logout/save");

        SystemFacade facade = SystemFacade.getInstance();
        User loggedIn = facade.login(VALID_EMAIL, VALID_PASSWORD);
        System.out.println("login(valid user) > " + (loggedIn != null));

        if (loggedIn == null) {
            System.out.println("cant continue save flow because login failed");
            return;
        }
        // add some demo data to be saved. this will be visible in the load flow and in the json files.
        String uniqueEmail = "demo.user." + System.currentTimeMillis() + "@example.com";
        User demoUser = new User(uniqueEmail, "StrongPass1!", "Demo", "User");
        boolean addUserResult = facade.addUser(demoUser);
        System.out.println("addUser -> " + addUserResult + " (email=" + uniqueEmail + ")");
        
        // create a question with nested data to have data in the save/load demo
        InterviewQuestion demoQuestion = new InterviewQuestion(
                "Demo persisted question " + System.currentTimeMillis(),
                "This qustion is created as demo.",
                Difficulty.EASY,
                Category.ARRAY,
                QuestionType.SHORT_ANSWER,
                loggedIn.getUserId());

        // question-level comment from the logged-in user
        Comment questionComment = new Comment("adding a question level comment from user", loggedIn.getUserId());
        demoQuestion.addComment(questionComment);

        // Add a meaty description section with image, constraints, and examples.
        Section section = new Section(
                "Description",
            "Given an array of integers nums and a target value, return the indices of two numbers that add up to target.",
                DataType.STRING,
                SectionType.DESCRIPTION);
        section.setImageURL("https://images.example.com/questions/two-sum-visual.png");
        section.setExpectedTimeComplexity("O(n)");
        section.setMaxLinesOfCode(20);
        section.setTimeLimitSeconds(1);
        section.addConstraint("2 <= nums.length <= 10^4");
        section.addConstraint("-10^9 <= nums[i] <= 10^9");
        section.addConstraint("-10^9 <= target <= 10^9");
        section.addConstraint("Exactly one valid answer exists");
        section.addExample("Input: nums = [2,7,11,15], target = 9 | Output: [0,1]");
        section.addExample("Input: nums = [3,2,4], target = 6 | Output: [1,2]");
        section.addExample("Input: nums = [3,3], target = 6 | Output: [0,1]");

        // section-level comment from the same user
        Comment sectionComment = new Comment("section note from user", loggedIn.getUserId());
        section.addComment(sectionComment);
        
        // add an answer with a comment to have more nested data in the question
        Answer answer = new Answer("int x = 1;", "Demo answer body", loggedIn.getUserId());
        Comment answerComment = new Comment("answer comment from user", loggedIn.getUserId());
        answerComment.addReply(new Comment("quick reply from same user", loggedIn.getUserId()));
        answer.addComment(answerComment);
        section.addAnswer(answer);
        demoQuestion.addSection(section);

        Section constraintsSection = new Section(
            "Hard Constraints",
            "Your implementation should avoid brute force and fit within practical interview limits.",
            DataType.STRING,
            SectionType.CONSTRAINT);
        constraintsSection.setExpectedTimeComplexity("O(n)");
        constraintsSection.setMaxLinesOfCode(25);
        constraintsSection.setTimeLimitSeconds(2);
        constraintsSection.addConstraint("Do not use nested loops over nums");
        constraintsSection.addConstraint("Use at most O(n) extra space");
        constraintsSection.addConstraint("Return indices, not values");
        demoQuestion.addSection(constraintsSection);

        Section exampleSection = new Section(
            "Walkthrough Examples",
            "These examples clarify edge behavior and indexing expectations.",
            DataType.STRING,
            SectionType.EXAMPLE);
        exampleSection.addExample("nums = [1,5,3,7], target = 8 -> [0,3]");
        exampleSection.addExample("nums = [-3,4,3,90], target = 0 -> [0,2]");
        exampleSection.addExample("nums = [0,4,3,0], target = 0 -> [0,3]");
        demoQuestion.addSection(exampleSection);
        
        
        boolean addQuestionResult = facade.addQuestion(demoQuestion);
        System.out.println("addQuestion -> " + addQuestionResult + " (questionId=" + demoQuestion.getQuestionId() + ")");
        // logout at the end to trigger save of all data, including the new user and question added in this flow.

        boolean logoutResult = facade.logout();
        System.out.println("logout > " + logoutResult + " ( calls saveAllData inside)");
        System.out.println("save flow done. data should now be in json/users.json and json/questions.json");
    }

    private static void runLoadFlow() {
        // load flow: show counts from loaded data, then check valid/invalid login.
        System.out.println("loading app startup > data load > login checks");

        //
        SystemFacade facade = SystemFacade.getInstance();
        int userCount = facade.getUsers().size();
        int questionCount = QuestionList.getInstance().getAll().size();

        // these counts should reflect the data added in the save flow, plus any existing data in the json files.
        System.out.println("loaded users count: " + userCount);
        System.out.println("loaded questions count: " + questionCount);

        if (questionCount > 0) {
            InterviewQuestion latest = QuestionList.getInstance().getAll().get(questionCount - 1);
            System.out.println("latest question comment count: " + latest.getComments().size());
            if (!latest.getSections().isEmpty()) {
                Section latestSection = latest.getSections().get(0);
                System.out.println("latest section comment count: " + latestSection.getComments().size());
                if (!latestSection.getAnswers().isEmpty()) {
                    Answer latestAnswer = latestSection.getAnswers().get(0);
                    System.out.println("latest answer comment count: " + latestAnswer.getComments().size());
                }
            }
        }

        User validLogin = facade.login(VALID_EMAIL, VALID_PASSWORD);
        System.out.println("login(valid user) returned user: " + (validLogin != null));

        // this should fail and return null, showing that invalid credentials are not authenticated.
        User invalidLogin = facade.login("missing.user@example.com", "badpass");
        System.out.println("login(missing user) return null: " + (invalidLogin == null));

        // logout at the end to show that logout works after load even if no new data is added in this flow.
        boolean logoutResult = facade.logout();
        System.out.println("logout > " + logoutResult);
        System.out.println("data loading done");
    }
}
