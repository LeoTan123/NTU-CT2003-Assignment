package team5.controllers;

import team5.App;
import team5.CareerCenterStaff;
import team5.enums.UserType;
import team5.boundaries.ConsoleBoundary;
import team5.staffactions.ReviewCompanyRegistrationsAction;
import team5.staffactions.ReviewInternshipSubmissionsAction;
import team5.staffactions.StaffAction;
import team5.staffactions.ViewInternshipsAction;

public class CareerCenterStaffController {

	private final StaffAction reviewCompanyAction;
	private final StaffAction reviewInternshipAction;
	private final StaffAction viewInternshipsAction;

	public CareerCenterStaffController() {
		this.reviewCompanyAction = new ReviewCompanyRegistrationsAction();
		this.reviewInternshipAction = new ReviewInternshipSubmissionsAction();
		this.viewInternshipsAction = new ViewInternshipsAction();
	}

	public void showMenu(CareerCenterStaff staff) {
		boolean exit = false;
		while (!exit) {
			ConsoleBoundary.printSectionTitle("Career Center Staff Menu", true);
			System.out.println("1. Review Company Representative Registrations");
			System.out.println("2. Review Internship Submissions");
			System.out.println("3. View Internship Opportunities");
			System.out.println("4. Update Password");
			System.out.println("5. Logout");
			String input = ConsoleBoundary.promptUserInput(true);
			
			switch (input) {
			case "1":
				reviewCompanyAction.run(staff);
				break;
			case "2":
				reviewInternshipAction.run(staff);
				break;
			case "3":
				viewInternshipsAction.run(staff);
				break;
			case "4":
				boolean updated = staff.changePassword();
				if (updated) {
					boolean ok = App.updatePasswordAndPersist(UserType.CCSTAFF, staff.getUserID(), staff.getPassword());
                	if (ok) {
                		staff.logout();
                		System.out.println("Please log in again with your new password.");
                		exit = true;
                	}
                	else {
                		ConsoleBoundary.printErrorMessage();
                	}
				}
				break;
			case "5":
				staff.logout();
				exit = true;
				break;
			default:
				ConsoleBoundary.printInvalidSelection();
			}
		}
	}
}
