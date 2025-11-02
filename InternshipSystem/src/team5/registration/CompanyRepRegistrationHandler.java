package team5.registration;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import team5.App;
import team5.CompanyRep;
import team5.Internship;
import team5.enums.UserAccountStatus;

public class CompanyRepRegistrationHandler {

	public void startRegistration() {
		boolean continueRegistration = true;
		while (continueRegistration) {
			App.printSectionTitle("Company Representative Registration");
			System.out.println("Enter the details below (press 0 at any time to cancel):");

			String name = promptInput("Full Name");
			if (name == null) {
				return;
			}

			String companyName = promptInput("Company Name");
			if (companyName == null) {
				return;
			}

			String department = promptInput("Department");
			if (department == null) {
				return;
			}

			String position = promptInput("Position");
			if (position == null) {
				return;
			}

			String email = promptUniqueCompanyEmail();
			if (email == null) {
				return;
			}

			//String generatedId = generateUniqueId(name);

			App.printSectionTitle("Registration Summary");
			System.out.println("Representative ID: " + email);
			System.out.println("Name: " + name);
			System.out.println("Company: " + companyName);
			System.out.println("Department: " + department);
			System.out.println("Position: " + position);
			System.out.println("Email: " + email);
			//System.out.println("Status: Pending Approval");

			boolean awaitingDecision = true;
			while (awaitingDecision) {
				System.out.println("Confirm registration?");
				System.out.println("1. Confirm and submit");
				System.out.println("2. Start over");
				System.out.println("0. Cancel");

				String choice = App.sc.nextLine();
				switch (choice) {
					case "1":
						boolean isSuccessful = submitRegistration(email, name, companyName, department, position, email);
						if (isSuccessful) {
							System.out.println("Registration submitted. A career center staff member will review your account.");
						}
						else {
							System.out.println(App.ERROR_MESSAGE);
						}
						
						awaitingDecision = false;
						continueRegistration = false;
						break;
					case "2":
						awaitingDecision = false;
						break;
					case "0":
						System.out.println("Registration cancelled.");
						return;
					default:
						System.out.println("Invalid option. Please choose 1, 2, or 0.");
				}
			}
		}
	}

	private String promptInput(String label) {
		while (true) {
			System.out.println(label + ":");
			String input = App.sc.nextLine().trim();
			if ("0".equals(input)) {
				System.out.println("Registration cancelled.");
				return null;
			}
			if (!input.isEmpty()) {
				return input;
			}
			System.out.println("Input cannot be empty. Please try again.");
		}
	}

	private String promptUniqueCompanyEmail() {
		while (true) {
			String emailInput = promptInput("Company Email");
			if (emailInput == null) {
				return null;
			}
			if (isEmailInUse(emailInput)) {
				System.out.println("An account with this company email already exists. Please use a different email.");
				continue;
			}
			return emailInput;
		}
	}

	private boolean isEmailInUse(String email) {
		return App.compRepList.stream()
				.anyMatch(rep -> rep.getUserID().equalsIgnoreCase(email)
						|| rep.getEmail().equalsIgnoreCase(email));
	}

	private boolean submitRegistration(String id, String name, String companyName, String department,
			String position, String email) 
	{
		ArrayList<Internship> createdInternships = new ArrayList<Internship>();
		CompanyRep registration = new CompanyRep(id, name, email, "password", companyName, 
				department, position, UserAccountStatus.PENDING, createdInternships);
		
		boolean isSuccessful = appendRegistrationToCsv(registration);
		if (isSuccessful) {
			// only add to list after saving successfully to CSV
			App.compRepList.add(registration);
		}
		return isSuccessful;
	}

	private boolean appendRegistrationToCsv(CompanyRep registration) {
		try (FileWriter writer = new FileWriter(App.envFilePathRep, true)) {
			StringBuilder sb = new StringBuilder();
			sb.append(registration.getUserID()).append(",")
			  .append(registration.getName()).append(",")
			  .append(registration.getCompanyName()).append(",")
			  .append(registration.getDepartment()).append(",")
			  .append(registration.getPosition()).append(",")
			  .append(registration.getEmail()).append(",")
			  .append(registration.getAccountStatus().name()).append("\n");
			writer.append(sb.toString());
			writer.flush();
			return true;
		} catch (IOException e) {
			System.out.println("Failed to save registration to file: " + e.getMessage());
			return false;
		}
	}
}
