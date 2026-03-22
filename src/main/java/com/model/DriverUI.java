package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class DriverUI {
	private SystemFacade driver;

	DriverUI() {
		driver = SystemFacade.getInstance();
	}

	public void run() {
		scenario4();
		// scenario2();
		// scenario3();
		// scenario1();
	}

	public void scenario1() {
		System.out.println("Scenario 1: Duplicate User Creation");

		UUID newUserId = UUID.randomUUID();

		User duplicateUser = new User(
				newUserId,
				"sparrow@example.com",
				User.hashPassword("hashed-password-456"),
				"Sally",
				"Sparrow",
				LocalDateTime.parse("2024-03-10T09:00:00"),
				LocalDateTime.parse("2025-04-15T16:00:00"),
				false,
				false,
				new Profile("University of South Carolina", "Computer Science", 2026, 5,
						"https://example.com/resume.pdf"),
				new ArrayList<>());

		if (driver.addUser(duplicateUser)) {
			System.out.println("User Sally Sparrow has been successfully created");
			DataWriter.saveUsers(driver.getUsers());
			driver.saveAllData();
		} else {
			System.out.println("Sorry, we couldn't create the user. Email already exists.");
		}

	}

	public void scenario2() {
		System.out.println("Scenario 2: Successful User Creation and Login");

		UUID newUserId = UUID.randomUUID();
		User successfulUser = new User(
				newUserId,
				"sally@example.com",
				User.hashPassword("hashed-password-456"),
				"Sally",
				"Sparrow",
				LocalDateTime.parse("2024-03-10T09:00:00"),
				LocalDateTime.parse("2025-04-15T16:00:00"),
				false,
				false,
				new Profile("University of South Carolina", "Computer Science", 2026, 5,
						"https://example.com/resume.pdf"),
				new ArrayList<>());

		if (driver.addUser(successfulUser)) {
			System.out.println("User Sally Sparrow has been successfully created");
			DataWriter.saveUsers(driver.getUsers());
			driver.saveAllData();

		} else {
			System.out.println("Sorry, we couldn't create the user.");
		}

		// Attempt to log in with the new user
		User loggedInUser = driver.login("sally@example.com", "hashed-password-456");
		if (loggedInUser != null) {
			System.out.println("Login successful for user Sally Sparrow");
		} else {
			System.out.println("Login failed for user Sally Sparrow");
		}
	}

	public void scenario3() {
		System.out.println("Scenario 3: Sally question creation with two solutions");

		User loggedInUser = driver.login("sally@example.com", "hashed-password-456");

		if (loggedInUser == null) {
			System.out.println("Login failed for user Sally Sparrow.");
			return;
		}

		System.out.println("Login successful for user Sally Sparrow.");

		loggedInUser.setContributor(true);
		InterviewQuestion question = new InterviewQuestion(
			"Longest Subarray with given Sum",
			"Given an integer array nums and an integer sum k, return the length of the longest contiguous subarray whose total equals k.\nNote: the array can contain negative numbers.",
			Difficulty.MEDIUM,
			Category.ARRAY,
			QuestionType.CODING,
			loggedInUser.getUserId()
		);

		//Description section
		Section desciptionSection = new Section(
			"Problem Description",
			"Given an integer array nums and an integer sum k, return the length of the longest contiguous subarray whose total equals k.\nNote: the array can contain negative numbers.",
			DataType.STRING,
			SectionType.DESCRIPTION
			);

		//Example 1 section
		Section example1Section = new Section(
			"Example 1",
			"Example walkthrough for the first input.",
			DataType.STRING,
			SectionType.EXAMPLE
			);

		example1Section.addExample("Input: nums = [1, -1, 5, -2, 3], k = 3");
		example1Section.addExample("Output: 4");
		example1Section.addExample("Explanation: The subarray [1, -1, 5, -2] sums to 3 and has length 4.");

		//Example 2 section
		Section example2Section = new Section(
			"Example 2",
			"Example walkthrough for the second input.",
			DataType.STRING,
			SectionType.EXAMPLE
			);

		example2Section.addExample("Input: nums = [-2, -1, 2, 1], k = 3");
		example2Section.addExample("Output: 2");

		//Follow-up section
		Section followUpSection = new Section(
			"Follow-up Questions",
			"Additional follow-up questions.",
			DataType.STRING,
			SectionType.EXPLANATION
			);

		followUpSection.addExample("What is the time complexity of your algorithm?");
		followUpSection.addExample("Can you find a way to make your algorithm faster?");

		Section solutionsSection = new Section(
			"Solutions",
			"Two solution approaches with filenames and explanations.",
			DataType.FILE,
			SectionType.EXPLANATION
			);

		Answer bruteForceSolution = new Answer(
			"LongestSubarrayBruteForce.java",
			"Checks every start index and extends subarrays while tracking running sum. Time O(n^2), space O(1).",
			loggedInUser.getUserId()
			);

		Answer prefixSumSolution = new Answer(
			"LongestSubarrayPrefixSum.java",
			"Uses prefix sums and a map of earliest indices to find longest valid window. Time O(n), space O(n).",
			loggedInUser.getUserId()
			);

		solutionsSection.addAnswer(bruteForceSolution);
		solutionsSection.addAnswer(prefixSumSolution);

		question.addSection(desciptionSection);
		question.addSection(example1Section);
		question.addSection(example2Section);
		question.addSection(followUpSection);
		question.addSection(solutionsSection);

		boolean isAdded = driver.addQuestion(question);

		if (isAdded) {
			System.out.println("Question added successfully.");
			driver.saveAllData();
			System.out.println(question);
		} else {
			System.out.println("Failed to add question.");
		}

	}

	public void scenario4() {
		System.out.println("Scenario 4: Daily Task Flow (Hard-Coded Driver Stub)");
		DailyTaskScenario.runDailyTaskScenario();
	}

	public static void main(String[] args) {
		DriverUI appInterface = new DriverUI();
		appInterface.run();
	}
}