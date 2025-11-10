package team5.companyrepactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import team5.App;
import team5.CompanyRep;
import team5.Internship;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.CsvFileBoundary;
import team5.boundaries.InternshipBoundary;
import team5.enums.InternshipFilterOption;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;
import team5.filters.InternshipFilter;
import team5.filters.InternshipFilterCriteria;
import team5.filters.InternshipFilterPrompt;
import team5.interfaces.FileBoundary;

public class ListOwnInternshipsAction implements CompanyRepAction {
	private static final List<InternshipFilterOption> FILTER_OPTIONS = List.of(
			InternshipFilterOption.PREFERRED_MAJOR,
			InternshipFilterOption.INTERNSHIP_LEVEL,
			InternshipFilterOption.INTERNSHIP_STATUS,
			InternshipFilterOption.APPLICATION_CLOSE_TO);
	
    private final FileBoundary fileBoundary = new CsvFileBoundary();

	@Override
	public void run(CompanyRep rep) {
		ConsoleBoundary.printSectionTitle("My Created Internships", true);
		ArrayList<Internship> ownInternships = rep.getInternships();
		if (ownInternships.isEmpty()) {
			InternshipBoundary.printNoInternshipCreated();
			return;
		}

		List<Internship> baseList = new ArrayList<>(ownInternships);
		List<Internship> workingList = new ArrayList<>(baseList);
		int pageIndex = 0;
		
		while (true) {
			if (workingList.isEmpty()) {
				System.out.println("No internships match your current filter.");
				System.out.println("Enter 'a' to show all internships or 0 to return.");
				String emptyInput = ConsoleBoundary.promptUserInput();
				if ("0".equals(emptyInput)) {
					return;
				}
				if ("a".equalsIgnoreCase(emptyInput)) {
					baseList = new ArrayList<>(rep.getInternships());
					workingList = new ArrayList<>(baseList);
					pageIndex = 0;
					continue;
				}
				ConsoleBoundary.printInvalidSelection();
				continue;
			}
			int start = pageIndex * ConsoleBoundary.PAGE_SIZE;
			int end = Math.min(start + ConsoleBoundary.PAGE_SIZE, workingList.size());

			if (start >= workingList.size()) {
				pageIndex = 0;
				continue;
			}

			for (int i = start; i < end; i++) {
				Internship internship = workingList.get(i);
				System.out.printf("%d. %s%n", i + 1, InternshipBoundary.buildSummaryLine(internship, true));
			}

			printNavigationPrompt(pageIndex > 0, end < workingList.size());
			String input = ConsoleBoundary.promptUserInput();

			if ("0".equals(input)) {
				return;
			} 
			else if ("f".equalsIgnoreCase(input)) {
				baseList = new ArrayList<>(rep.getInternships());
				InternshipFilterCriteria criteria = InternshipFilterPrompt.prompt(FILTER_OPTIONS);
				if (criteria == null) {
					return;
				}
				workingList = InternshipFilter.apply(baseList, criteria);
				pageIndex = 0;
			} 
			else if ("a".equalsIgnoreCase(input)) {
				baseList = new ArrayList<>(rep.getInternships());
				workingList = new ArrayList<>(baseList);
				pageIndex = 0;
			} 
			else if ("n".equalsIgnoreCase(input)) {
				if (end < workingList.size()) {
					pageIndex++;
				} 
				else {
					ConsoleBoundary.printInvalidSelection();
				}
			} 
			else if ("p".equalsIgnoreCase(input)) {
				if (pageIndex > 0) {
					pageIndex--;
				} 
				else {
					ConsoleBoundary.printInvalidSelection();
				}
			} 
			else {
				try {
					int selection = Integer.parseInt(input);
					if (selection < 1 || selection > workingList.size()) {
						ConsoleBoundary.printInvalidSelection();
						continue;
					}
					
					Internship chosen = workingList.get(selection - 1);
					InternshipBoundary.printInternshipDetails(chosen);
					handleInternshipActions(rep, chosen);
					// refresh lists to capture any updates/deletions
					baseList = new ArrayList<>(rep.getInternships());
					workingList = new ArrayList<>(baseList);
					pageIndex = 0;
				} 
				catch (NumberFormatException ex) {
					ConsoleBoundary.printInvalidInput();
				}
			}
		}
	}

	private void handleInternshipActions(CompanyRep rep, Internship chosen) {
		while (true) {
			boolean showUpdateOption = chosen.getInternshipStatus() == InternshipStatus.PENDING;
			int selectedOption = InternshipBoundary.displaySubMenu(showUpdateOption);
			if (selectedOption == 0) {
				return;
			} 
			else if (selectedOption == 1) {
				ConsoleBoundary.printWIP();
			} 
			else if (selectedOption == 2) {
				if (showUpdateOption) {
					updateInternship(chosen, rep, 0);
				} 
				else {
					deleteInternship();
				}
			} 
			else if (showUpdateOption && selectedOption == 3) {
				deleteInternship();
			} 
			else {
				ConsoleBoundary.printInvalidSelection();
			}
		}
	}
	
	private void updateInternship(Internship chosenInternship, CompanyRep rep, int selectedInternshipOption) {
		
		Internship updatedInternship = new Internship(
				chosenInternship.getInternshipId(),
				chosenInternship.getTitle(), 
				chosenInternship.getDescription(), 
				chosenInternship.getInternshipLevel(), 
				chosenInternship.getPreferredMajor(), 
				chosenInternship.getApplicationOpenDate(), 
				chosenInternship.getApplicationCloseDate(), 
				chosenInternship.getInternshipStatus(), 
				chosenInternship.getCompanyName(), 
				chosenInternship.getCompanyRep(), 
				chosenInternship.getNumOfSlots()
		);
		
		int selectedOption = InternshipBoundary.displayUpdateInternshipMenu();
		switch(selectedOption) {
			case 0: // return
				return;
			case 1: // Title
				ConsoleBoundary.printText("Current title: " + chosenInternship.getTitle());
				String newTitle = ConsoleBoundary.promptFormInput("New title");
				if (newTitle == null) {
					return;
				}
				
				updatedInternship.setTitle(newTitle);
				break;
			case 2: // Description
				ConsoleBoundary.printText("Current description: " + chosenInternship.getDescription());
				String newDescription = ConsoleBoundary.promptFormInput("New description");
				if (newDescription == null) {
					return;
				}
				
				updatedInternship.setDescription(newDescription);
				break;
			case 3: // Level
				ConsoleBoundary.printText("Current level: " + chosenInternship.getInternshipLevel());
				InternshipLevel newLevel = InternshipBoundary.promptInternshipLevel();
				if (newLevel == null) {
					return;
				}
				
				updatedInternship.setInternshipLevel(newLevel);
				break;
			case 4: // Preferred Major
				ConsoleBoundary.printText("Current preferred major: " + chosenInternship.getPreferredMajor().getFullName());
				StudentMajor newPreferredMajor = InternshipBoundary.promptPreferredMajor();
				if (newPreferredMajor == null) {
					return;
				}
				
				updatedInternship.setPreferredMajor(newPreferredMajor);
				break;
			case 5: // Application Open Date
				ConsoleBoundary.printText("Current application open date: " + chosenInternship.getApplicationOpenDate().format(App.DATE_DISPLAY_FORMATTER));
				LocalDate newApplicationOpenDate = ConsoleBoundary.promptDate("New application start date");
				if (newApplicationOpenDate == null) {
					return;
				}
				
				updatedInternship.setApplicationOpenDate(newApplicationOpenDate);
				break;
			case 6: // Application Close Date
				ConsoleBoundary.printText("Current application close date: " + chosenInternship.getApplicationCloseDate().format(App.DATE_DISPLAY_FORMATTER));
				LocalDate newApplicationCloseDate = ConsoleBoundary.promptDate("New application close date");
				if (newApplicationCloseDate == null) {
					return;
				}
				
				updatedInternship.setApplicationCloseDate(newApplicationCloseDate);
				break;
			case 7: // Number of Slots
				ConsoleBoundary.printText("Current number of slots: " + chosenInternship.getNumOfSlots());
				int newNumOfSlots = InternshipBoundary.promptNumOfSlots("New number of slots");
				if (newNumOfSlots == 0) {
					return;
				}
				
				updatedInternship.setNumOfSlots(newNumOfSlots);
				break;
		}
		
		// update global internshipList
		Internship globalInternship = findCurrentInternshipInList(chosenInternship, App.internshipList, rep);
		if (globalInternship == null) {
			ConsoleBoundary.printText("Cannot find internship in global internship list.");
			return;
		}
		globalInternship.setTitle(updatedInternship.getTitle());
		globalInternship.setDescription(updatedInternship.getDescription());
		globalInternship.setInternshipLevel(updatedInternship.getInternshipLevel()); 
		globalInternship.setPreferredMajor(updatedInternship.getPreferredMajor());
		globalInternship.setApplicationOpenDate(updatedInternship.getApplicationOpenDate());
		globalInternship.setApplicationCloseDate(updatedInternship.getApplicationCloseDate());
		globalInternship.setNumOfSlots(updatedInternship.getNumOfSlots());
		
		boolean tryAgain = false;
		
		do {
			// Save to file
			boolean success = fileBoundary.writeInternship(App.internshipList);
			if (success) {
				// update company rep createdInternships
				chosenInternship.setTitle(updatedInternship.getTitle());
				chosenInternship.setDescription(updatedInternship.getDescription());
				chosenInternship.setInternshipLevel(updatedInternship.getInternshipLevel()); 
				chosenInternship.setPreferredMajor(updatedInternship.getPreferredMajor());
				chosenInternship.setApplicationOpenDate(updatedInternship.getApplicationOpenDate());
				chosenInternship.setApplicationCloseDate(updatedInternship.getApplicationCloseDate());
				chosenInternship.setNumOfSlots(updatedInternship.getNumOfSlots());
				
				System.out.println("Update successful.");
				return;
			}
			else {
				// check if user want to try again
				tryAgain = ConsoleBoundary.promptTryAgain();
			}
		} while (tryAgain);
		
		// revert back if error
		globalInternship.setTitle(chosenInternship.getTitle());
		globalInternship.setDescription(chosenInternship.getDescription());
		globalInternship.setInternshipLevel(chosenInternship.getInternshipLevel()); 
		globalInternship.setPreferredMajor(chosenInternship.getPreferredMajor());
		globalInternship.setApplicationOpenDate(chosenInternship.getApplicationOpenDate());
		globalInternship.setApplicationCloseDate(chosenInternship.getApplicationCloseDate());
		globalInternship.setNumOfSlots(chosenInternship.getNumOfSlots());
	}
	
	private void deleteInternship() {
		ConsoleBoundary.printWIP();
	}
	
	private Internship findCurrentInternshipInList(Internship currentInternship, ArrayList<Internship> internshipList, CompanyRep rep) {
		Optional<Internship> currentInternshipOptional = internshipList.stream().filter(i -> 
			(i.getInternshipId().equalsIgnoreCase(currentInternship.getInternshipId())) 
			&& (i.getCompanyRep().equalsIgnoreCase(rep.getUserID()))
		).findFirst();
		
		try {
			Internship foundInternship = currentInternshipOptional.get();
			return foundInternship;
		}
		catch (Exception e) {
			ConsoleBoundary.printErrorMessage();
			return null;
		}
	}
	
	private void printNavigationPrompt(boolean hasPrev, boolean hasNext) {
		System.out.print("Enter a number to view/edit details");
		if (hasPrev) {
			System.out.print(", 'p' for previous page");
		}
		if (hasNext) {
			System.out.print(", 'n' for next page");
		}
		System.out.println(", 'f' to filter, 'a' to show all, or 0 to return:");
	}
}
