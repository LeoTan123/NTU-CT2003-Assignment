package team5.studentactions;

import java.util.List;

import team5.App;
import team5.InternshipApplication;
import team5.Student;
import team5.enums.InternshipApplicationStatus;

public class CheckApplicationStatusAction implements StudentAction {

	@Override
	public void run(Student student) {
        List<InternshipApplication> applications = student.getInternshipApplications();
        boolean reviewing = true;
        while (reviewing) {
            System.out.println("===== Internship Applications =====");
            if (applications.isEmpty()) {
                System.out.println("No applications found.");
            } else {
                // Display application with status
            }
            System.out.println("Enter application number to view details (or 0 to return):");

            String input = App.sc.nextLine();
            if ("0".equals(input)) {
                reviewing = false;
                continue;
            }

            try {
                int selection = Integer.parseInt(input);
                if (selection < 1 || selection > applications.size()) {
                    System.out.println("Invalid selection. Please try again.");
                    continue;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
	}
	
	private void displayApplicationStatus(Student student, InternshipApplication application) {

    }

    private void handleOfferDecision(Student student, InternshipApplication application) {

    }

    private void acceptOffer(Student student, InternshipApplication application) {

    }

    private void rejectOffer(Student student, InternshipApplication application) {

    }
}
