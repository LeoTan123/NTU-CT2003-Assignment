package team5.companyrepactions;

import java.time.LocalDate;
import team5.App;
import team5.CompanyRep;
import team5.Internship;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.FileBoundary;
import team5.boundaries.InternshipBoundary;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

public class CreateInternshipAction implements CompanyRepAction {
	
    private static final String MAXIMUM_MESSAGE = "You can create maximum of 5 internship opportunities.";
    public static final int MAX_SLOTS_NUM = 10;
    
	@Override
	public void run(CompanyRep rep) {
		if (rep.isMaxInternshipReached()) {
			System.out.println(MAXIMUM_MESSAGE);
			return;
		}
		
		boolean continueCreate = true;
		while (continueCreate) {
			App.printSectionTitle("Create internship", true);
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
			
			App.printSectionTitle("Internship Summary");
			System.out.println("Title: " + title);
			System.out.println("Description: " + description);
			System.out.println("Internship Level: " + internshipLevel);
			System.out.println("Preferred Major: " + preferredMajor.getFullName());
			System.out.println("Application Start Date: " + startDate.format(App.DATE_DISPLAY_FORMATTER));
			System.out.println("Application End Date: " + endDate.format(App.DATE_DISPLAY_FORMATTER));
			System.out.println("Number of slots: " + numOfSlots);
			
			boolean awaitingDecision = true;
			while (awaitingDecision) {
				System.out.println("Confirm create internship?");
				System.out.println("1. Confirm and submit");
				System.out.println("2. Start over");
				System.out.println("0. Cancel");

				String choice = App.sc.nextLine();
				switch (choice) {
					case "1":
						// Generate new id
						String[] idsArray = App.internshipList.stream().map(i -> i.getInternshipId()).toArray(String[]::new);
						String generatedId = App.generateUniqueId("I", idsArray);
						
						// Create a new internship instance
						Internship internship = new Internship(generatedId, title, description, internshipLevel, 
								preferredMajor, startDate, endDate, 
								InternshipStatus.PENDING, App.currentUser.getEmail(), numOfSlots);
						
						// Save to file
						boolean isSuccessful = FileBoundary.appendInternshipToCsv(internship, rep.getCompanyName());
						if (isSuccessful) {
							// only add to list after saving successfully to CSV
							boolean isSuccess = rep.addInternship(internship);
							// If full do not add again
							if(!isSuccess)
							{
								System.out.println(MAXIMUM_MESSAGE);
								awaitingDecision = false;
								continueCreate = false;
								break;
							}
							// Add to internship list for staff to review
							App.internshipList.add(internship);
							System.out.println("Internship opportunity created successfully. Please wait for approval.");
						}
						else {
							System.out.println(App.ERROR_MESSAGE);
						}
						
						awaitingDecision = false;
						continueCreate = false;
						break;
					case "2":
						awaitingDecision = false;
						break;
					case "0":
						System.out.println("Cancelled.");
						return;
					default:
						System.out.println("Invalid option. Please choose 1, 2, or 0.");
				}
			}
		}
		

	}

	
}
