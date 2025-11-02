package team5.companyrep;

import java.util.ArrayList;

import team5.App;
import team5.CompanyRep;
import team5.Internship;

public class ListOwnInternshipsAction implements CompanyRepAction {

	@Override
	public void run(CompanyRep rep) {
		boolean continueView = true;
		while (continueView) {
			App.printSectionTitle("My Created Internships");
			ArrayList<Internship> createdIntenship = rep.getInternships();
			if(createdIntenship.isEmpty()){
				System.out.println("You have not created any internship yet.");
				return;
			}
			else{
				for (int i = 0; i < createdIntenship.size(); i++) {
					Internship internship = createdIntenship.get(i);
					System.out.printf("%d. Internship ID: %s | Title: %s | Level: %s | Status: %s%n",
							i + 1,
							internship.getInternshipId(),
							safeValue(internship.getTitle()),
							valueOrNA(internship.getInternshipLevel()),
							valueOrNA(internship.getInternshipStatus()));
				}
			}
			System.out.println("Enter 0 to return to the company representative menu:");
			String input = App.sc.nextLine();
			if ("0".equals(input)) {
				continueView = false;
			} else {
				System.out.println("Invalid selection. Please enter 0 to go back.");
			}
		}
	}
	
	private String safeValue(Object value) {
		if (value == null) {
			return "N/A";
		}
		if (value instanceof String) {
			String str = (String) value;
			return str.isEmpty() ? "N/A" : str;
		}
		return value.toString();
	}

	private String valueOrNA(Enum<?> value) {
		return value != null ? value.name() : "N/A";
	}
}
