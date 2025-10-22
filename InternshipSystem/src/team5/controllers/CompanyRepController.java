package team5.controllers;

import team5.App;
import team5.CompanyRepRegistration;
import team5.User;
import team5.companyrep.CompanyRepAction;
import team5.companyrep.CreateInternshipAction;
import team5.companyrep.ListOwnInternshipsAction;
import team5.companyrep.ViewInternshipsAction;

public class CompanyRepController {

	private final CompanyRepAction viewInternshipsAction;
	private final CompanyRepAction createInternshipAction;
	private final CompanyRepAction listOwnInternshipsAction;

	public CompanyRepController() {
		this.viewInternshipsAction = new ViewInternshipsAction();
		this.createInternshipAction = new CreateInternshipAction();
		this.listOwnInternshipsAction = new ListOwnInternshipsAction();
	}

	public void showMenu(User currentUser, CompanyRepRegistration companyRep) {
		boolean exit = false;
		while (!exit) {
			System.out.println("===== Company Representative Menu =====");
			System.out.println("1. View Internship Opportunities");
			System.out.println("2. Create Internship");
			System.out.println("3. Display My Created Internships");
			System.out.println("4. Update Password");
			System.out.println("5. Logout");
			System.out.println("Please choose an option:");

			String input = App.sc.nextLine();
			switch (input) {
				case "1":
					viewInternshipsAction.run(companyRep);
					break;
				case "2":
					createInternshipAction.run(companyRep);
					break;
				case "3":
					listOwnInternshipsAction.run(companyRep);
					break;
				case "4":
					boolean updated = currentUser.changePassword();
					if (updated) {
						currentUser.logout();
						System.out.println("You have been logged out. Please log in again with your new password.");
						exit = true;
					}
					break;
				case "5":
					currentUser.logout();
					exit = true;
					break;
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}
}
