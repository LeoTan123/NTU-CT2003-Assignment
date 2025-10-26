package team5.companyrep;

import team5.App;
import team5.CompanyRep;

public class ListOwnInternshipsAction implements CompanyRepAction {

	@Override
	public void run(CompanyRep rep) {
		boolean awaiting = true;
		while (awaiting) {
			System.out.println("Enter 0 to return to the company representative menu:");
			String input = App.sc.nextLine();
			if ("0".equals(input)) {
				awaiting = false;
			} else {
				System.out.println("Invalid selection. Please enter 0 to go back.");
			}
		}
	}
}
