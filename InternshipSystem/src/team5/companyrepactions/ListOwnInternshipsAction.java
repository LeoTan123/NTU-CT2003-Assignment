package team5.companyrepactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import team5.App;
import team5.CompanyRep;
import team5.Internship;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.FileBoundary;
import team5.boundaries.InternshipBoundary;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

public class ListOwnInternshipsAction implements CompanyRepAction {

	@Override
	public void run(CompanyRep rep) {
		boolean browsing = true;
		while (browsing) {
			
			App.printSectionTitle("My Created Internships", true);
			ArrayList<Internship> createdInternships = rep.getInternships();
			if(createdInternships.isEmpty()){
				InternshipBoundary.printNoInternshipCreated();
				return;
			}
			
			InternshipBoundary.printInternshipList(createdInternships);
			ConsoleBoundary.printChooseOption();
			String inputInternship = ConsoleBoundary.promptUserInput();
			if ("0".equals(inputInternship)) {
				browsing = false;
				continue;
			} 
			
			try {
				int selectedInternshipOption = Integer.parseInt(inputInternship);
				if (selectedInternshipOption < 1 || selectedInternshipOption > createdInternships.size()) {
					ConsoleBoundary.printInvalidSelection();
					continue;
				}
				
				Internship chosen = createdInternships.get(selectedInternshipOption - 1);
				InternshipBoundary.printInternshipDetails(chosen);
				
				while (true) {
					boolean showUpdateOption = false;
					if (chosen.getInternshipStatus() == InternshipStatus.PENDING) // can only update before approval
					{
						showUpdateOption = true;
					}
					
					int selectedOption = InternshipBoundary.displaySubMenu(showUpdateOption);
					if (selectedOption == 0) {
						break;
					}
					else if (selectedOption == 1) {
						ConsoleBoundary.printWIP();
					}
					else if (selectedOption == 2) {
						if (showUpdateOption) {
							// update
							updateInternship(chosen, rep, selectedInternshipOption);
						}
						else {
							// delete
							deleteInternship();
						}
					}
					else if (showUpdateOption && selectedOption == 3) {
						// delete
						deleteInternship();
					}
					else {
						ConsoleBoundary.printInvalidSelection();
						continue;
					}
				}
			} 
			catch (NumberFormatException ex) {
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
			boolean success = FileBoundary.writeInternshipToCSV(App.internshipList);
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
	
	
}
