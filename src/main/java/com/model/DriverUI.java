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
		scenario1();
		scenario2();
		scenario3();
		// scenario4();
	}

	public void scenario1() {
		System.out.println("Scenario 1: Duplicate User Creation");

		UUID newUserId = UUID.randomUUID();

		User duplicateUser = new User(
				newUserId,
				"sparrow@example.com",
				"hashed-password-456",
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
			DataWriter.saveUsers(driver.getInstance().getUsers());
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
			DataWriter.saveUsers(driver.getInstance().getUsers());
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
			"Addional follow-up questions.",
			DataType.STRING,
			SectionType.EXAMPLE
			);

		followUpSection.addExample("What is the time compleity of your algorithm?");
		followUpSection.addExample("Can you you find a way to make you algoritm faster?");

		//May need to add answer sections for each solution
		
		question.addSection(desciptionSection);
		question.addSection(example1Section);
		question.addSection(example2Section);
		question.addSection(followUpSection);

		boolean isAdded = driver.addQuestion(question);

		if (isAdded) {
			System.out.println("Question added successfully.");
			driver.saveAllData();
			System.out.println(question);
		} else {
			System.out.println("Failed to add question.");
		}

	}

		

	public static void main(String[] args) {
		DriverUI appInterface = new DriverUI();
		appInterface.run();
	}
}