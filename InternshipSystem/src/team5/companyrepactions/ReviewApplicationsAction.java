package team5.companyrepactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team5.App;
import team5.CompanyRep;
import team5.Internship;
import team5.InternshipApplication;
import team5.Student;
import team5.boundaries.ConsoleBoundary;
import team5.enums.InternshipApplicationStatus;

/**
 * Action class for company representatives to review and approve/reject student applications
 * for their internship postings.
 *
 * This class allows company reps to:
 * 1. View all applications submitted to their internships
 * 2. See applicant details (name, major, year, application date)
 * 3. Approve applications (status -> SUCCESSFUL)
 * 4. Reject applications (status -> UNSUCCESSFUL)
 */
public class ReviewApplicationsAction implements CompanyRepAction {

	@Override
	public void run(CompanyRep rep) {
		boolean reviewing = true;

		while (reviewing) {
			ConsoleBoundary.printSectionTitle("Review Internship Applications", true);

			// Get all internships owned by this company rep
			ArrayList<Internship> ownedInternships = rep.getInternships();

			if (ownedInternships.isEmpty()) {
				System.out.println("You have not created any internships yet.");
				return;
			}

			// Find all applications for these internships
			Map<Internship, List<InternshipApplication>> applicationsByInternship =
				findApplicationsForInternships(ownedInternships);

			// Check if there are any applications
			if (applicationsByInternship.isEmpty() || !hasAnyApplications(applicationsByInternship)) {
				System.out.println("No applications have been submitted to your internships yet.");
				return;
			}

			// Display internships with application counts
			displayInternshipsWithApplications(applicationsByInternship);

			System.out.println("\nSelect internship number to review applications (or 0 to return):");
			String input = App.sc.nextLine().trim();

			if ("0".equals(input)) {
				reviewing = false;
				continue;
			}

			try {
				int choice = Integer.parseInt(input);

				if (choice < 1 || choice > applicationsByInternship.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}

				// Get the selected internship
				List<Internship> internshipList = new ArrayList<>(applicationsByInternship.keySet());
				Internship selectedInternship = internshipList.get(choice - 1);
				List<InternshipApplication> applications = applicationsByInternship.get(selectedInternship);

				// Review applications for this internship
				reviewApplicationsForInternship(selectedInternship, applications);

			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}
	}

	/**
	 * Finds all applications submitted by students for the given internships.
	 *
	 * @param internships List of internships to find applications for
	 * @return Map of internship to list of applications
	 */
	private Map<Internship, List<InternshipApplication>> findApplicationsForInternships(
			ArrayList<Internship> internships) {

		Map<Internship, List<InternshipApplication>> applicationMap = new HashMap<>();

		// Initialize map with empty lists for each internship
		for (Internship internship : internships) {
			applicationMap.put(internship, new ArrayList<>());
		}

		// Loop through all students to find applications
		for (Student student : App.studentList) {
			ArrayList<InternshipApplication> studentApplications = student.getInternshipApplications();

			for (InternshipApplication application : studentApplications) {
				Internship appliedInternship = application.getInternshipInfo();

				// Check if this application is for one of the company rep's internships
				for (Internship ownedInternship : internships) {
					if (appliedInternship.getInternshipId().equals(ownedInternship.getInternshipId())) {
						applicationMap.get(ownedInternship).add(application);
						break;
					}
				}
			}
		}

		return applicationMap;
	}

	/**
	 * Checks if there are any applications in the map.
	 *
	 * @param applicationMap Map of internships to applications
	 * @return true if at least one application exists, false otherwise
	 */
	private boolean hasAnyApplications(Map<Internship, List<InternshipApplication>> applicationMap) {
		for (List<InternshipApplication> apps : applicationMap.values()) {
			if (!apps.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Displays all internships with their application counts.
	 *
	 * @param applicationMap Map of internships to applications
	 */
	private void displayInternshipsWithApplications(
			Map<Internship, List<InternshipApplication>> applicationMap) {

		System.out.println("\nYour Internships with Applications:");
		System.out.println("=" .repeat(80));

		int index = 1;
		for (Map.Entry<Internship, List<InternshipApplication>> entry : applicationMap.entrySet()) {
			Internship internship = entry.getKey();
			List<InternshipApplication> applications = entry.getValue();

			if (!applications.isEmpty()) {
				// Count pending applications
				long pendingCount = applications.stream()
					.filter(app -> app.getStatus() == InternshipApplicationStatus.PENDING)
					.count();

				System.out.printf("%d. %s (ID: %s)%n",
					index,
					internship.getTitle(),
					internship.getInternshipId());
				System.out.printf("   Total Applications: %d | Pending: %d | Reviewed: %d%n",
					applications.size(),
					pendingCount,
					applications.size() - pendingCount);
				System.out.println();
				index++;
			}
		}
	}

	/**
	 * Reviews applications for a specific internship.
	 * Allows company rep to view applicant details and approve/reject applications.
	 *
	 * @param internship The internship to review applications for
	 * @param applications List of applications for this internship
	 */
	private void reviewApplicationsForInternship(Internship internship,
			List<InternshipApplication> applications) {

		boolean reviewing = true;

		while (reviewing) {
			ConsoleBoundary.printSectionTitle("Applications for: " + internship.getTitle());

			if (applications.isEmpty()) {
				System.out.println("No applications for this internship.");
				return;
			}

			// Display all applications
			displayApplicationList(applications);

			System.out.println("\nSelect application number to review (or 0 to return):");
			String input = App.sc.nextLine().trim();

			if ("0".equals(input)) {
				reviewing = false;
				continue;
			}

			try {
				int choice = Integer.parseInt(input);

				if (choice < 1 || choice > applications.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}

				InternshipApplication selectedApplication = applications.get(choice - 1);

				// Show application details and allow approve/reject
				processApplication(selectedApplication);

			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}
	}

	/**
	 * Displays a list of applications with key details.
	 *
	 * @param applications List of applications to display
	 */
	private void displayApplicationList(List<InternshipApplication> applications) {
		System.out.println("\nApplications:");
		System.out.println("-".repeat(80));

		for (int i = 0; i < applications.size(); i++) {
			InternshipApplication app = applications.get(i);
			Student student = app.getStudentInfo();

			System.out.printf("%d. Student: %s | Major: %s | Year: %d | Status: %s%n",
				i + 1,
				student.getName(),
				student.getMajor().getFullName(),
				student.getYear(),
				app.getStatus().name());
			System.out.printf("   Applied Date: %s | Application ID: %d%n",
				app.getAppliedDate().format(App.DATE_DISPLAY_FORMATTER),
				app.getApplicationId());
			System.out.println();
		}
	}

	/**
	 * Processes a single application - shows details and allows approve/reject.
	 *
	 * @param application The application to process
	 */
	private void processApplication(InternshipApplication application) {
		Student student = application.getStudentInfo();

		// Display detailed application information
		ConsoleBoundary.printSectionTitle("Application Details");
		System.out.println("Application ID: " + application.getApplicationId());
		System.out.println("Student Name: " + student.getName());
		System.out.println("Student ID: " + student.getUserID());
		System.out.println("Email: " + student.getEmail());
		System.out.println("Major: " + student.getMajor().getFullName());
		System.out.println("Year of Study: " + student.getYear());
		System.out.println("Application Date: " + application.getAppliedDate().format(App.DATE_DISPLAY_FORMATTER));
		System.out.println("Current Status: " + application.getStatus().name());
		System.out.println();

		// If already reviewed, don't allow changes
		if (application.getStatus() != InternshipApplicationStatus.PENDING) {
			System.out.println("This application has already been reviewed.");
			System.out.println("\nPress Enter to return...");
			App.sc.nextLine();
			return;
		}

		// Show action options
		System.out.println("What would you like to do?");
		System.out.println("1. Approve Application");
		System.out.println("2. Reject Application");
		System.out.println("0. Return to application list");
		System.out.print("\nEnter your choice: ");

		String choice = App.sc.nextLine().trim();

		switch (choice) {
			case "1":
				approveApplication(application, student);
				break;
			case "2":
				rejectApplication(application, student);
				break;
			case "0":
				return;
			default:
				System.out.println("Invalid choice. Returning to application list.");
		}
	}

	/**
	 * Approves an application by changing its status to SUCCESSFUL.
	 *
	 * @param application The application to approve
	 * @param student The student who submitted the application
	 */
	private void approveApplication(InternshipApplication application, Student student) {
		System.out.println("\nAre you sure you want to APPROVE this application?");
		System.out.print("Enter 'yes' to confirm: ");
		String confirmation = App.sc.nextLine().trim();

		if (!confirmation.equalsIgnoreCase("yes")) {
			System.out.println("Application approval cancelled.");
			return;
		}

		// Update application status
		application.setStatus(InternshipApplicationStatus.SUCCESSFUL);

		System.out.println("\n" + "=".repeat(80));
		System.out.println("APPLICATION APPROVED!");
		System.out.println("=".repeat(80));
		System.out.println("Student: " + student.getName());
		System.out.println("Application ID: " + application.getApplicationId());
		System.out.println("Status: SUCCESSFUL");
		System.out.println("\nThe student will be able to accept this offer.");
		System.out.println("=".repeat(80));

		// Pause to show success message
		System.out.println("\nPress Enter to continue...");
		App.sc.nextLine();
	}

	/**
	 * Rejects an application by changing its status to UNSUCCESSFUL.
	 *
	 * @param application The application to reject
	 * @param student The student who submitted the application
	 */
	private void rejectApplication(InternshipApplication application, Student student) {
		System.out.println("\nAre you sure you want to REJECT this application?");
		System.out.print("Enter 'yes' to confirm: ");
		String confirmation = App.sc.nextLine().trim();

		if (!confirmation.equalsIgnoreCase("yes")) {
			System.out.println("Application rejection cancelled.");
			return;
		}

		// Update application status
		application.setStatus(InternshipApplicationStatus.UNSUCCESSFUL);

		System.out.println("\n" + "=".repeat(80));
		System.out.println("APPLICATION REJECTED");
		System.out.println("=".repeat(80));
		System.out.println("Student: " + student.getName());
		System.out.println("Application ID: " + application.getApplicationId());
		System.out.println("Status: UNSUCCESSFUL");
		System.out.println("\nThe student will be notified of the rejection.");
		System.out.println("=".repeat(80));

		// Pause to show message
		System.out.println("\nPress Enter to continue...");
		App.sc.nextLine();
	}
}
