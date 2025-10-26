package team5.companyrep;

import team5.App;
import team5.CompanyRep;
import team5.enums.InternshipLevel;

public class CreateInternshipAction implements CompanyRepAction {

	@Override
	public void run(CompanyRep rep) {
		boolean done = false;
		while (!done) {
			App.PrintSectionTitle("Create internship");
			System.out.println("Enter the details below (press 0 at any time to cancel):");

			String title = App.PromptFormInput("Title");
			if (title == null) {
				return;
			}
			
			String description = App.PromptFormInput("Description");
			if (description == null) {
				return;
			}
			
			
			InternshipLevel internshipLevel = PromptInternshipLevel();
			if (internshipLevel == null) {
				return;
			}
			
			
			System.out.println("Enter 0 to return to the company representative menu:");
			String input = App.sc.nextLine();
			if ("0".equals(input)) {
				done = true;
				continue;
			} else {
				System.out.println("Invalid selection. Please enter 0 to go back.");
			}
		}
	}
	
	private InternshipLevel PromptInternshipLevel() {
		while (true) {
			System.out.println("Choose internship level:");
			System.out.println("1: Basic");
			System.out.println("2: Intermediate");
			System.out.println("3: Advanced");
			String input = App.sc.nextLine().trim();
			switch (input) {
				
			}
			return InternshipLevel.BASIC;
		}
		
	}
}
