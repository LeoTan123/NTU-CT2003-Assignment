package team5.companyrepactions;

import java.util.ArrayList;
import java.util.Optional;

import team5.App;
import team5.CompanyRep;
import team5.Internship;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.FileBoundary;
import team5.boundaries.InternshipBoundary;
import team5.enums.InternshipStatus;

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
	
	private void updateInternship(Internship internship, CompanyRep rep, int selectedInternshipOption) {
		int selectedOption = InternshipBoundary.displayUpdateInternshipMenu();
		switch(selectedOption) {
			case 0: // return
				break;
			case 1: // Title
				ConsoleBoundary.printText("Current title: " + internship.getTitle());
				String newTitle = ConsoleBoundary.promptFormInput("New title");
				if (newTitle == null) {
					return;
				}
				
				// update company rep createdInternships
				internship.setTitle(newTitle);
				
				// update global internshipList
				Optional<Internship> currentInternshipOptional = App.internshipList.stream().filter(i -> 
					(i.getInternshipId().equalsIgnoreCase(internship.getInternshipId())) 
					&& (i.getCompanyRep().equalsIgnoreCase(rep.getUserID()))
				).findFirst();
				
				try {
					Internship currentInternship = currentInternshipOptional.get();
					currentInternship.setTitle(newTitle);
					
					// Save to file
					boolean success = FileBoundary.writeInternshipToCSV(App.internshipList);
					if (success) {
						System.out.println("Title updated successfully.");
					}
					else {
						ConsoleBoundary.printErrorMessage();
					}
					
				}
				catch (Exception e) {
					ConsoleBoundary.printErrorMessage();
				}
				break;
			case 2: // Description
				break;
			case 3: // Level
				break;
			case 4: // Preferred Majors
				break;
			case 5: // Application Open Date
				break;
			case 6: // Application Close Date
				break;
			case 7: // Application Close Date
				break;
		}
	}
	
	private void deleteInternship() {
		ConsoleBoundary.printWIP();
	}
	
	
}
