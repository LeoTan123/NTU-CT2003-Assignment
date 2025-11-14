package team5.controllers;

import team5.App;
import team5.CompanyRep;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.CsvFileBoundary;
import team5.companyrepactions.CompanyRepAction;
import team5.companyrepactions.CreateInternshipAction;
import team5.companyrepactions.ListOwnInternshipsAction;
import team5.enums.UserType;
import team5.companyrepactions.ReviewApplicationsAction;
import team5.interfaces.CsvRepository;

public class CompanyRepController extends UserController {

	private final CompanyRep companyRep;
	private final CompanyRepAction createInternshipAction;
	private final CompanyRepAction listOwnInternshipsAction;
	private final CompanyRepAction reviewApplicationsAction;
	private final CsvRepository fileBoundary = new CsvFileBoundary();

	public CompanyRepController(CompanyRep companyRep) {
		this.companyRep = companyRep;
		this.createInternshipAction = new CreateInternshipAction(fileBoundary);
		this.listOwnInternshipsAction = new ListOwnInternshipsAction(fileBoundary);
		this.reviewApplicationsAction = new ReviewApplicationsAction(fileBoundary);
	}

	@Override
	public void showMenu() {
		boolean exit = false;
		while (!exit) {
			ConsoleBoundary.printSectionTitle("Company Representative Menu", true);
			System.out.println("1. Create Internship");
			System.out.println("2. View My Created Internships");
			System.out.println("3. Review Internship Applications");
			System.out.println("4. Update Password");
			System.out.println("5. Logout");
			String input = ConsoleBoundary.promptUserInput();
			
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
						boolean ok = App.updatePasswordAndPersist(UserType.COMREP, companyRep.getUserID(), companyRep.getPassword());
						if (ok) {
	                		companyRep.logout();
	                		System.out.println("Please log in again with your new password.");
	                		exit = true;
	                	}
						else {
							ConsoleBoundary.printErrorMessage();
	                	}
					}
					break;
				case "5":
					companyRep.logout();
					exit = true;
					break;
				default:
					ConsoleBoundary.printInvalidSelection();
			}
		}
	}
}
