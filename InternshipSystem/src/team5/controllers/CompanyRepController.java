package team5.controllers;

import team5.App;
import team5.CompanyRep;
import team5.boundaries.ConsoleBoundary;
import team5.companyrepactions.CompanyRepAction;
import team5.companyrepactions.CreateInternshipAction;
import team5.companyrepactions.ListOwnInternshipsAction;
import team5.companyrepactions.ReviewApplicationsAction;

public class CompanyRepController {

	private final CompanyRepAction createInternshipAction;
	private final CompanyRepAction listOwnInternshipsAction;
	private final CompanyRepAction reviewApplicationsAction;

	public CompanyRepController() {
		this.createInternshipAction = new CreateInternshipAction();
		this.listOwnInternshipsAction = new ListOwnInternshipsAction();
		this.reviewApplicationsAction = new ReviewApplicationsAction();
	}

	public void showMenu(CompanyRep companyRep) {
		boolean exit = false;
		while (!exit) {
			ConsoleBoundary.printSectionTitle("Company Representative Menu", true);
			System.out.println("1. Create Internship");
			System.out.println("2. View My Created Internships");
			System.out.println("3. Review Internship Applications");
			System.out.println("4. Update Password");
			System.out.println("5. Logout");
			System.out.println("Please choose an option:");

			String input = App.sc.nextLine();
			switch (input) {
				case "1":
					createInternshipAction.run(companyRep);
					break;
				case "2":
					listOwnInternshipsAction.run(companyRep);
					break;
				case "3":
					reviewApplicationsAction.run(companyRep);
					break;
				case "4":
					boolean updated = companyRep.changePassword();
					if (updated) {
						companyRep.logout();
						System.out.println("You have been logged out. Please log in again with your new password.");
						exit = true;
					}
					break;
				case "5":
					companyRep.logout();
					exit = true;
					break;
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}
}
