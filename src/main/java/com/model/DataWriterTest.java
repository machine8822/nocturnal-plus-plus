package com.model;

import java.util.ArrayList;

/**
 * Simple test for DataWriter to check if it can save users and questions to JSON files.
 * 
 * Run this to test if saveUsers() and saveQuestions() work.
 * Check the json/ folder to see if the files were created.
 */
public class DataWriterTest {

	public static void main(String[] args) {
		System.out.println("=== Testing DataWriter ===\n");

		// TEST 1: Save Users
		System.out.println("TEST 1: Saving Users...");
		testSaveUsers();

		System.out.println("\n" + "=".repeat(50) + "\n");

		// TEST 2: Save Questions
		System.out.println("TEST 2: Saving Questions...");
		testSaveQuestions();

		System.out.println("\n=== Tests Complete ===");
		System.out.println("Check json/users.json and json/questions.json to see the results!");
	}

	/**
	 * Create some dummy users and try to save them
	 */
	private static void testSaveUsers() {
		// Create a list of test users
		ArrayList<User> users = new ArrayList<>();

		// Create User 1 (you'll need to adjust this based on your User constructor)
		// EDIT THESE LINES to match your User class constructor!
		User user1 = new User(
			"user-id-1",
			"john.doe@example.com",
			"hashed-password-123",
			"John",
			"Doe",
			"2024-01-15T10:30:00",
			"2026-02-20T14:45:00",
			true,  // isAdmin
			true   // isContributor
		);
		// Set the profile (adjust based on your Profile class)
		user1.getProfile().setSchool("University of South Carolina");
		user1.getProfile().setMajor("Computer Science");
		user1.getProfile().setTotalUpvotes(5);
		user1.getProfile().setResumeURL("https://example.com/resume.pdf");

		// Create User 2
		User user2 = new User(
			"user-id-2",
			"jane.smith@example.com",
			"hashed-password-456",
			"Jane",
			"Smith",
			"2024-02-01T10:30:00",
			"2026-02-18T14:45:00",
			false, // isAdmin
			true   // isContributor
		);
		user2.getProfile().setSchool("University of North Carolina");
		user2.getProfile().setMajor("Computer Engineering");
		user2.getProfile().setTotalUpvotes(10);
		user2.getProfile().setResumeURL("https://example.com/resume2.pdf");

		// Add users to the list
		users.add(user1);
		users.add(user2);

		// Call saveUsers() and check if it worked
		boolean success = DataWriter.saveUsers(users);

		if (success) {
			System.out.println("✓ SUCCESS: Users saved to json/users.json");
			System.out.println("  Total users saved: " + users.size());
		} else {
			System.out.println("✗ FAILED: Could not save users");
		}
	}

	/**
	 * Create some dummy questions and try to save them
	 */
	private static void testSaveQuestions() {
		// Create a list of test questions
		ArrayList<InterviewQuestion> questions = new ArrayList<>();

		// Create Question 1 (adjust based on your InterviewQuestion constructor)
		InterviewQuestion question1 = new InterviewQuestion(
			"q-id-1",
			"Binary search time complexity",
			"EASY",
			"SHORT_ANSWER",
			"ARRAY",
			"",  // imageURL
			"user-id-2",  // authorId
			0,  // totalAttempts
			0   // totalSuccesses
		);
		// Set timestamps (adjust based on your class methods)
		question1.setCreatedAt("2026-02-21T13:30:00");
		question1.setLastUpdated("2026-02-21T13:30:00");

		// Add a section to the question
		Section section1 = new Section(
			"Description",
			"What is the time complexity of binary search on a sorted array and why?",
			"DESCRIPTION"
		);
		question1.addSection(section1);

		// Create Question 2
		InterviewQuestion question2 = new InterviewQuestion(
			"q-id-2",
			"Detecting duplicates in an array",
			"MEDIUM",
			"SHORT_ANSWER",
			"ARRAY",
			"",  // imageURL
			"user-id-1",  // authorId
			0,  // totalAttempts
			0   // totalSuccesses
		);
		question2.setCreatedAt("2026-02-21T14:00:00");
		question2.setLastUpdated("2026-02-21T14:00:00");

		// Add a section to the question
		Section section2 = new Section(
			"Description",
			"Given an integer array, explain an efficient approach to determine whether the array contains any duplicate values.",
			"DESCRIPTION"
		);
		question2.addSection(section2);

		// Add questions to the list
		questions.add(question1);
		questions.add(question2);

		// Call saveQuestions() and check if it worked
		boolean success = DataWriter.saveQuestions(questions);

		if (success) {
			System.out.println("✓ SUCCESS: Questions saved to json/questions.json");
			System.out.println("  Total questions saved: " + questions.size());
		} else {
			System.out.println("✗ FAILED: Could not save questions");
		}
	}
}
