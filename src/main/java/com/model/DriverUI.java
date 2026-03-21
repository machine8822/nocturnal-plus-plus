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

		if (driver.addUser(successfulUser)) {
			System.out.println("User Sally Sparrow has been successfully created");
			DataWriter.saveUsers(driver.getInstance().getUsers());
			driver.saveAllData();
		} else {
			System.out.println("Sorry, we couldn't create the user.");
		}
	}

	public void scenario3() {
		System.out.println("Scenario 3: Question Creation with two solutions");

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