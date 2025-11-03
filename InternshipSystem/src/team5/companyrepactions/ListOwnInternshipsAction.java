package team5.companyrepactions;

import java.util.ArrayList;

import team5.App;
import team5.CompanyRep;
import team5.Internship;
import team5.boundaries.InternshipBoundary;

public class ListOwnInternshipsAction implements CompanyRepAction {

	@Override
	public void run(CompanyRep rep) {
		boolean browsing = true;
		boolean browsingSub = true;
		while (browsing) {
			browsingSub = true;
			App.printSectionTitle("My Created Internships", true);
			ArrayList<Internship> createdInternships = rep.getInternships();
			if(createdInternships.isEmpty()){
				System.out.println("You have not created any internship yet.");
				return;
			}
			
			InternshipBoundary.displayInternshipList(createdInternships);
			System.out.println("Choose option to view details (or 0 to return)");
			String inputInternship = App.sc.nextLine();
			if ("0".equals(inputInternship)) {
				browsing = false;
				continue;
			} 
			
			try {
				int selectedInternshipOption = Integer.parseInt(inputInternship);
				if (selectedInternshipOption < 1 || selectedInternshipOption > createdInternships.size()) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				
				Internship chosen = createdInternships.get(selectedInternshipOption - 1);
				InternshipBoundary.displayInternshipDetails(chosen);
				
				while (browsingSub) {
					displaySubMenu();
					
					String selectedMenuOption = App.sc.nextLine();
					if ("0".equals(selectedMenuOption)) {
						browsingSub = false;
						continue;
					}
					
					try {
						int selectedSubMenu = Integer.parseInt(selectedMenuOption);
						
						if (selectedSubMenu == 1) {
							System.out.println("WIP");
						}
						else {
							System.out.println("Invalid selection. Please try again.");
							continue;
						}
					}
					catch (NumberFormatException ex) {
						System.out.println("Please enter a valid number.");
					}
				}
				
				
				
			} 
			catch (NumberFormatException ex) {
				System.out.println("Please enter a valid number.");
			}
		}
	}
	
	private void displaySubMenu() {
		System.out.println();
		System.out.println("Choose option: (or 0 to return)");
		System.out.println("1. View student applications");
	}
}
