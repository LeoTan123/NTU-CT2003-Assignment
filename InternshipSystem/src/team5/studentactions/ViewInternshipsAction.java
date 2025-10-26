package team5.studentactions;

import team5.App;
import team5.Internship;
import team5.Student;

public class ViewInternshipsAction implements StudentAction {

	@Override
	public void run(Student student) {
		boolean browsing = true;
		while (browsing) {
			System.out.println("===== Internship Opportunities =====");
			if (App.internshipList.isEmpty()) {
				System.out.println("No internships available at the moment.");
			} else {
				for (int i = 0; i < App.internshipList.size(); i++) {
					Internship internship = App.internshipList.get(i);
					System.out.printf("%d. Internship ID: %d | Title: %s | Level: %s | Status: %s%n",
							i + 1,
							internship.getInternshipId(),
							safeValue(internship.getTitle()),
							valueOrNA(internship.getInternshipLevel()),
							valueOrNA(internship.getInternshipStatus()));
				}
			}

			System.out.println("Enter internship number to view details (or 0 to return):");
			String input = App.sc.nextLine();
			if ("0".equals(input)) {
				browsing = false;
				continue;
			}

			try {
				int selection = Integer.parseInt(input);
				if (selection < 1 || selection > App.internshipList.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}

				Internship chosen = App.internshipList.get(selection - 1);
				displayInternshipDetails(chosen);
			} catch (NumberFormatException ex) {
				System.out.println("Please enter a valid number.");
			}
		}
	}

	private void displayInternshipDetails(Internship internship) {
		System.out.println("===== Internship Details =====");
		System.out.println("Internship ID: " + internship.getInternshipId());
		System.out.println("Title: " + safeValue(internship.getTitle()));
		System.out.println("Description: " + safeValue(internship.getDescription()));
		System.out.println("Level: " + valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Majors: " + safeValue(internship.getPreferredMajor()));
		System.out.println("Application Open Date: " + safeValue(internship.getApplicationOpenDate()));
		System.out.println("Application Close Date: " + safeValue(internship.getApplicationCloseDate()));
		System.out.println("Status: " + valueOrNA(internship.getInternshipStatus()));
		System.out.println("Number of Slots: " + internship.getNumOfSlots());
		System.out.println("Press Enter to return to the internship list.");
		App.sc.nextLine();
	}

	private String safeValue(Object value) {
		if (value == null) {
			return "N/A";
		}
		if (value instanceof String) {
			String str = (String) value;
			return str.isEmpty() ? "N/A" : str;
		}
		return value.toString();
	}

	private String valueOrNA(Enum<?> value) {
		return value != null ? value.name() : "N/A";
	}
}
