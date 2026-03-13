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
		// scenario2();
		// scenario3();
		// scenario4();
	}

	// User Scenarios
	public void scenario1() {
		System.out.println("Scenario 1: Login");

		User loggedInUser = driver.login("grant.smith@example.com", "123grant");
		User failedLogin = driver.login("asshat@example.com", "wrongpassword");
		if (loggedInUser == null) {
			System.out.println("Sorry we couldn't login.");
			return;
		}

		System.out.println("Grant Smith is now logged in");

		if (failedLogin == null) {
			System.out.println("Failed login attempt with asshat@example.com");

		}

	}

	public void scenario2() {
		System.out.println("Scenario 2: User Creation");

		UUID user1Id = UUID.randomUUID();
		User newUser = new User(
				user1Id,
				"john.doe@example.com",
				"hashed-password-123",
				"John",
				"Doe",
				LocalDateTime.parse("2024-01-15T10:30:00"),
				LocalDateTime.parse("2026-02-20T14:45:00"),
				true,
				true,
				new Profile("University of South Carolina", "Computer Science", 2026, 5,
						"https://example.com/resume.pdf"),
				new ArrayList<>());

		if (driver.addUser(newUser)) {
			System.out.println("User John Doe has been successfully created");

			DataWriter.saveUsers(driver.getInstance().getUsers());
			driver.saveAllData();
			// driver.deleteUser(newUser.getUserId());
		} else {
			System.out.println("Sorry, we couldn't create the user.");
		}
	}

	public void scenario3() {
		System.out.println("Scenario 3: Logout");

		if (driver.logout()) {
			System.out.println("User has been successfully logged out");
		} else {
			System.out.println("Sorry, we couldn't log out the user.");
		}

	}

	// Question Scenarios
	public void scenario4() {
		System.out.println("Scenario 4: Question Creation");

		// Login a user first (questions need an author)
		User loggedInUser = driver.login("grant.smith@example.com", "123grant");
		if (loggedInUser == null) {
			System.out.println("Must login first to create a question.");
			return;
		}

		// Create a new interview question
		InterviewQuestion newQuestion = new InterviewQuestion(
				"How far can fish fly?",
				"Given the weight and size, how far will the fish fly?",
				Difficulty.MEDIUM,
				Category.LINKED_LIST,
				QuestionType.SHORT_ANSWER,
				loggedInUser.getUserId());

		if (driver.addQuestion(newQuestion)) {
			System.out.println("Question created successfully: " + newQuestion.getTitle());

			driver.saveAllData();
			System.out.println("Question saved to questions.json");
		} else {
			System.out.println("Failed to create question.");
		}

	}

	public static void main(String[] args) {
		DriverUI appInterface = new DriverUI();
		appInterface.run();
	}
}