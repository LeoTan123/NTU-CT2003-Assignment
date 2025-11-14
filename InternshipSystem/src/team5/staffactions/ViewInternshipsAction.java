package team5.staffactions;

import java.util.ArrayList;
import java.util.List;

import team5.App;
import team5.CareerCenterStaff;
import team5.Internship;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.InternshipBoundary;
import team5.enums.InternshipFilterOption;
import team5.filters.InternshipFilter;
import team5.filters.InternshipFilterCriteria;
import team5.filters.InternshipFilterPrompt;

public class ViewInternshipsAction implements StaffAction {

	private static final List<InternshipFilterOption> FILTER_OPTIONS = List.of(
			InternshipFilterOption.PREFERRED_MAJOR,
			InternshipFilterOption.INTERNSHIP_LEVEL,
			InternshipFilterOption.INTERNSHIP_STATUS,
			InternshipFilterOption.APPLICATION_CLOSE_TO);

	@Override
	public void run(CareerCenterStaff staff) {
		ConsoleBoundary.printSectionTitle("All Internship Details");
		if (App.internshipList.isEmpty()) {
			System.out.println("No internships available at the moment.");
			return;
		}

		List<Internship> baseList = new ArrayList<>(App.internshipList);
		List<Internship> workingList = new ArrayList<>(baseList);
		int pageIndex = 0;
		boolean browsing = true;

		while (browsing) {
			if (workingList.isEmpty()) {
				System.out.println("No internships match your current filter.");
				System.out.println("Enter 'a' to show all internships or 0 to return.");
				String emptyInput = ConsoleBoundary.promptUserInput();
				if ("0".equals(emptyInput)) {
					return;
				}
				if ("a".equalsIgnoreCase(emptyInput)) {
					workingList = new ArrayList<>(baseList);
					pageIndex = 0;
					continue;
				}
				ConsoleBoundary.printInvalidInput();
				continue;
			}
			int start = pageIndex * ConsoleBoundary.PAGE_SIZE;
			int end = Math.min(start + ConsoleBoundary.PAGE_SIZE, workingList.size());

			for (int i = start; i < end; i++) {
				Internship internship = workingList.get(i);
				System.out.printf("%d. %s%n", i + 1, InternshipBoundary.buildSummaryLine(internship, true));
			}

			boolean hasNext = end < workingList.size();
			boolean hasPrev = pageIndex > 0;
			printNavigationPrompt(hasPrev, hasNext);

			String input = ConsoleBoundary.promptUserInput();
			if ("0".equals(input)) {
				return;
			} 
			else if ("f".equalsIgnoreCase(input)) {
				baseList = new ArrayList<>(App.internshipList);
				InternshipFilterCriteria criteria = InternshipFilterPrompt.prompt(FILTER_OPTIONS);
				if (criteria == null) {
					return;
				}
				workingList = InternshipFilter.apply(baseList, criteria);
				pageIndex = 0;
			} 
			else if ("a".equalsIgnoreCase(input)) {
				baseList = new ArrayList<>(App.internshipList);
				workingList = new ArrayList<>(baseList);
				pageIndex = 0;
			} 
			else if (hasNext && "n".equalsIgnoreCase(input)) {
				pageIndex++;
			} 
			else if (hasPrev && "p".equalsIgnoreCase(input)) {
				pageIndex = Math.max(pageIndex - 1, 0);
			} 
			else {
				try {
					int selection = Integer.parseInt(input);
					if (selection < 1 || selection > workingList.size()) {
						ConsoleBoundary.printInvalidSelection();
						continue;
					}
					Internship chosen = workingList.get(selection - 1);
					displayInternshipDetails(chosen);
				} 
				catch (NumberFormatException ex) {
					ConsoleBoundary.printInvalidInput();
				}
			}
		}
	}

	private void displayInternshipDetails(Internship internship) {
		ConsoleBoundary.printSectionTitle("Internship Details");
		System.out.println("Internship ID: " + ConsoleBoundary.safeValue(internship.getInternshipId()));
		System.out.println("Title: " + ConsoleBoundary.safeValue(internship.getTitle()));
		System.out.println("Description: " + ConsoleBoundary.safeValue(internship.getDescription()));
		System.out.println("Level: " + ConsoleBoundary.valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Major: " + ConsoleBoundary.safeValue(internship.getPreferredMajor()));
		System.out.println("Application Open Date: " + ConsoleBoundary.safeValue(internship.getApplicationOpenDate()));
		System.out.println("Application Close Date: " + ConsoleBoundary.safeValue(internship.getApplicationCloseDate()));
		System.out.println("Status: " + ConsoleBoundary.valueOrNA(internship.getInternshipStatus()));
		System.out.println("Number of Slots: " + internship.getNumOfSlots());
		System.out.println("Press Enter to return to the internship list.");
		while (true) {
	        System.out.println("Enter 0 to return to the internship action list:");
	        String input = ConsoleBoundary.promptUserInput();
	        if (input.equals("0")) {
	            return;
	        } else {
	            System.out.println("Invalid input. Please enter 0 to return.");
	        }
	    }
	}

	private void printNavigationPrompt(boolean hasPrev, boolean hasNext) {
		System.out.print("Enter a number to view details");
		if (hasPrev) {
			System.out.print(", 'p' for previous page");
		}
		if (hasNext) {
			System.out.print(", 'n' for next page");
		}
		System.out.println(", 'f' to filter, 'a' to show all, or 0 to return:");
	}
}
