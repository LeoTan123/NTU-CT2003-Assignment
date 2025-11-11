package team5.controllers;

import team5.App;
import team5.CompanyRep;
import team5.companyrepactions.CompanyRepAction;
import team5.companyrepactions.CreateInternshipAction;
import team5.companyrepactions.ListOwnInternshipsAction;
import team5.enums.UserType;

public class CompanyRepController {

	private final CompanyRepAction createInternshipAction;
	private final CompanyRepAction listOwnInternshipsAction;

	public CompanyRepController() {
		this.createInternshipAction = new CreateInternshipAction();
		this.listOwnInternshipsAction = new ListOwnInternshipsAction();
	}

	public void showMenu(CompanyRep companyRep) {
		boolean exit = false;
		while (!exit) {
			App.printSectionTitle("Company Representative Menu", true);
			System.out.println("1. Create Internship");
			System.out.println("2. View My Created Internships");
			System.out.println("3. Update Password");
			System.out.println("4. Logout");
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
					boolean updated = companyRep.changePassword();
					if (updated) {
						boolean ok = App.updatePasswordAndPersist(
								UserType.COMREP, 
								companyRep.getUserID(),
								companyRep.getPassword()
								);
						if (ok) {
	                		System.out.println("password saved to CSV.");
	                		companyRep.logout();
	                		System.out.println("Please log in again with your new password.");
	                		exit = true;
	                	}else {
	                		System.out.println("Failed to save new password to CSV.");
	                	}
					}
					break;
				case "4":
					companyRep.logout();
					exit = true;
					break;
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}
}
