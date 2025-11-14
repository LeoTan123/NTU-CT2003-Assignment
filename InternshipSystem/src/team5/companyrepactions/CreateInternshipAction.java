package team5.companyrepactions;

import java.time.LocalDate;
import team5.App;
import team5.CompanyRep;
import team5.Internship;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.InternshipBoundary;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;
import team5.interfaces.InternshipCsvRepository;

public class CreateInternshipAction implements CompanyRepAction {
	
    public static final int MAX_SLOTS_NUM = 10;
    
    private final InternshipCsvRepository fileBoundary;
    
    public CreateInternshipAction(InternshipCsvRepository fileBoundary) {
    	this.fileBoundary = fileBoundary;
    }
    
	@Override
	public void run(CompanyRep rep) {
		if (rep.isMaxInternshipReached()) {
			InternshipBoundary.printMaxInternshipsReached();
			return;
		}
		
		boolean continueCreate = true;
		while (continueCreate) {
			ConsoleBoundary.printSectionTitle("Create internship", true);
			System.out.println("Enter the details below (press 0 at any time to cancel):");

			String title = ConsoleBoundary.promptFormInput("Title");
			if (title == null) {
				return;
			}
			
			String description = ConsoleBoundary.promptFormInput("Description");
			if (description == null) {
				return;
			}
			
			InternshipLevel internshipLevel = InternshipBoundary.promptInternshipLevel();
			if (internshipLevel == null) {
				return;
			}
			
			StudentMajor preferredMajor = InternshipBoundary.promptPreferredMajor();
			if (preferredMajor == null) {
				return;
			}
			
			LocalDate startDate = ConsoleBoundary.promptDate("Enter the application start date");
			if (startDate == null) {
				return;
			}
			
			LocalDate endDate = ConsoleBoundary.promptDate("Enter the application end date");
			if (endDate == null) {
				return;
			}
			if (endDate.isBefore(startDate)) {
				System.out.println("End date cannot be before the start date. Please re-enter the internship details.");
				continue;
			}
			
			int numOfSlots = InternshipBoundary.promptNumOfSlots("Enter number of slots");
			if (numOfSlots == 0) {
				return;
			}
			
			// Create a new internship instance for summary
			Internship internship = new Internship(null, title, description, internshipLevel, 
					preferredMajor, startDate, endDate, 
					InternshipStatus.PENDING, rep.getCompanyName(), rep.getEmail(), numOfSlots);
			InternshipBoundary.printCreateInternshipSummary(internship);
			
			boolean awaitingDecision = true;
			while (awaitingDecision) {
				System.out.println("Confirm create internship?");
				System.out.println("1. Confirm and submit");
				System.out.println("2. Start over");
				System.out.println("0. Cancel");
				String choice = ConsoleBoundary.promptUserInput(true);
				switch (choice) {
					case "1":
						// Generate new id
						String[] idsArray = App.internshipList.stream().map(i -> i.getInternshipId()).toArray(String[]::new);
						String generatedId = App.generateUniqueId("I", idsArray);
						
						// Create a new internship instance with generatedId
						Internship newInternship = new Internship(generatedId, title, description, internshipLevel, 
								preferredMajor, startDate, endDate, 
								InternshipStatus.PENDING, rep.getCompanyName(), rep.getEmail(), numOfSlots);
						
						// Save to file
						boolean isSuccessful = fileBoundary.appendInternship(newInternship);
						if (isSuccessful) {
							// only add to list after saving successfully to CSV
							boolean isSuccess = rep.addInternship(newInternship);
							// If full do not add again
							if(!isSuccess){
								awaitingDecision = false;
								continueCreate = false;
								break;
							}
							// Add to internship list for staff to review
							App.internshipList.add(newInternship);
							System.out.println("Internship opportunity created successfully. Please wait for approval.");
						}
						else {
							ConsoleBoundary.printErrorMessage();
						}
						
						awaitingDecision = false;
						continueCreate = false;
						break;
					case "2":
						awaitingDecision = false;
						break;
					case "0":
						awaitingDecision = false;
						continueCreate = false;
						break;
					default:
						ConsoleBoundary.printInvalidSelection();
				}
			}
		}
	}
}
