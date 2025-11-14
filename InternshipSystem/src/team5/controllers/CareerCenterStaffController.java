package team5.controllers;

import team5.App;
import team5.CareerCenterStaff;
import team5.enums.UserType;
import team5.boundaries.ConsoleBoundary;
import team5.staffactions.ReviewCompanyRegistrationsAction;
import team5.staffactions.ReviewInternshipSubmissionsAction;
import team5.staffactions.StaffAction;
import team5.staffactions.ViewInternshipsAction;

/**
 * Controller class for managing career center staff user interactions.
 * This class handles the staff menu and delegates actions to appropriate StaffAction
 * implementations. Staff can review company registrations, approve internship submissions,
 * view all internships, update password, and logout.
 */
public class CareerCenterStaffController extends UserController {

	private final CareerCenterStaff staff;
	private final StaffAction reviewCompanyAction;
	private final StaffAction reviewInternshipAction;
	private final StaffAction viewInternshipsAction;

	/**
	 * Constructs a new CareerCenterStaffController for the specified staff member.
	 * Initializes action handlers for reviewing company registrations, reviewing
	 * internship submissions, and viewing all internships in the system.
	 *
	 * @param staff the career center staff member who will be using this controller
	 */
	public CareerCenterStaffController(CareerCenterStaff staff) {
		this.staff = staff;
		this.reviewCompanyAction = new ReviewCompanyRegistrationsAction();
		this.reviewInternshipAction = new ReviewInternshipSubmissionsAction();
		this.viewInternshipsAction = new ViewInternshipsAction();
	}

	/**
	 * Displays the career center staff menu and handles user input.
	 * Provides options to review company registrations, review internship submissions,
	 * view all internships, update password, and logout. The menu continues to display
	 * until the staff member chooses to logout or exits after changing their password.
	 */
	public void showMenu() {
		boolean exit = false;
		while (!exit) {
			ConsoleBoundary.printSectionTitle("Career Center Staff Menu", true);
			System.out.println("1. Review Company Representative Registrations");
			System.out.println("2. Review Internship Submissions");
			System.out.println("3. View Internship Opportunities");
			System.out.println("4. Update Password");
			System.out.println("5. Logout");
			String input = ConsoleBoundary.promptUserInput();
			
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
