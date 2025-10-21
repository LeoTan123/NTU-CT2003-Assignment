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
			} catch (NumberFormatException ex) {
				System.out.println("Please enter a valid number.");
			}
		}
	}

	private void displayInternshipDetails(Internship internship) {

	}
}
