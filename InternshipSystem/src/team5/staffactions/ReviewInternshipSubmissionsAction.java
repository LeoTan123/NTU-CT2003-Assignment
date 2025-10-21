package team5.staffactions;

import team5.App;
import team5.CareerCenterStaff;
import team5.Internship;

public class ReviewInternshipSubmissionsAction implements StaffAction {

	@Override
	public void run(CareerCenterStaff staff) {
		System.out.println("===== Pending Internship Submissions =====");
		if (App.internshipList.isEmpty()) {
			System.out.println("No internship submissions found.");
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
		System.out.println("Press Enter to return to the staff menu.");
		App.sc.nextLine();
	}

	private String safeValue(String value) {
		return value != null && !value.isEmpty() ? value : "N/A";
	}

	private String valueOrNA(Enum<?> value) {
		return value != null ? value.name() : "N/A";
	}
}
