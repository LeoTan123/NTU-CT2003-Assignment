package team5.companyrepactions;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import team5.App;
import team5.CompanyRep;
import team5.Internship;
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
			App.printSectionTitle("Create internship");
			System.out.println("Enter the details below (press 0 at any time to cancel):");

			String title = App.promptFormInput("Title");
			if (title == null) {
				return;
			}
			
			String description = App.promptFormInput("Description");
			if (description == null) {
				return;
			}
			
			InternshipLevel internshipLevel = promptInternshipLevel();
			if (internshipLevel == null) {
				return;
			}
			
			StudentMajor preferredMajor = promptPreferredMajor();
			if (preferredMajor == null) {
				return;
			}
			
			LocalDate startDate = promptDate("Enter the internship start date:");
			if (startDate == null) {
				return;
			}
			
			LocalDate endDate = promptDate("Enter the internship end date:");
			if (endDate == null) {
				return;
			}
			if (endDate.isBefore(startDate)) {
				System.out.println("End date cannot be before the start date. Please re-enter the internship details.");
				continue;
			}
			
			App.printSectionTitle("Internship Summary");
			System.out.println("Title: " + title);
			System.out.println("Description: " + description);
			System.out.println("Internship Level: " + internshipLevel);
			System.out.println("Preferred Major: " + preferredMajor.getFullName());
			System.out.println("Start Date: " + startDate.format(App.DATE_DISPLAY_FORMATTER));
			System.out.println("End Date: " + endDate.format(App.DATE_DISPLAY_FORMATTER));
			
			boolean awaitingDecision = true;
			while (awaitingDecision) {
				System.out.println("Confirm create internship?");
				System.out.println("1. Confirm and submit");
				System.out.println("2. Start over");
				System.out.println("0. Cancel");

				String choice = App.sc.nextLine();
				switch (choice) {
					case "1":
						String[] idsArray = App.internshipList.stream().map(i -> i.getInternshipId()).toArray(String[]::new);
						String generatedId = App.generateUniqueId("I", idsArray);
						
						Internship internship = new Internship(generatedId, title, description, internshipLevel, 
								preferredMajor, startDate, endDate, 
								InternshipStatus.PENDING, App.currentUser.getEmail(), MAX_SLOTS_NUM);
						
						// Save to file
						boolean isSuccessful = appendInternshipToCsv(internship, rep.getCompanyName());
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
	
	private InternshipLevel promptInternshipLevel() {
		while (true) {
			System.out.println("Choose internship level:");
			System.out.println("1: Basic");
			System.out.println("2: Intermediate");
			System.out.println("3: Advanced");
			String input = App.sc.nextLine().trim();
			switch (input) {
				case "1":
					return InternshipLevel.BASIC;
				case "2":
					return InternshipLevel.INTERMEDIATE;
				case "3":
					return InternshipLevel.ADVANCED;
				default:
					continue;
			}
		}
		
	}
	
	private StudentMajor promptPreferredMajor() {
		StudentMajor[] majorList = StudentMajor.values();
		while (true) {
			System.out.println("Choose preferred major:");
			
	        for (int i = 0; i < majorList.length; i++) {
	        	System.out.println((i+1) + ": "  + majorList[i].getFullName());
	        }

			String input = App.sc.nextLine().trim();
			if ("0".equals(input)) {
				return null;
			}
			
			StudentMajor preferredMajor;
			
			try {
				preferredMajor = majorList[Integer.parseInt(input)-1];
			} 
			catch (IndexOutOfBoundsException e) {
				continue;
			}
			catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			return preferredMajor;
		}
	}
	
	private LocalDate promptDate(String prompt) {
		while (true) {
			System.out.println(prompt + " (format DD/MM/YYYY):");
		    String input = App.sc.nextLine().trim();
		    if ("0".equals(input)) {
				return null;
			}

		    try {
		        return LocalDate.parse(input, App.DATE_DISPLAY_FORMATTER);
		    } catch (DateTimeParseException e) {
		        System.out.println("Invalid date format! Please use DD/MM/YYYY.");
		        continue;
		    }
		}
	}

	private boolean appendInternshipToCsv(Internship internship, String companyName) {
		try (FileWriter writer = new FileWriter(App.envFilePathInternship, true)) {
			StringBuilder sb = new StringBuilder();
			sb.append(internship.getInternshipId()).append(",")
			  .append(internship.getTitle()).append(",")
			  .append(internship.getDescription()).append(",")
			  .append(internship.getInternshipLevel()).append(",")
			  .append(internship.getPreferredMajor().getFullName()).append(",")
			  .append(internship.getApplicationOpenDate().format(App.DATE_DB_FORMATTER)).append(",")
			  .append(internship.getApplicationCloseDate().format(App.DATE_DB_FORMATTER)).append(",")
			  .append(internship.getInternshipStatus()).append(",")
			  .append(companyName).append(",")
			  .append(internship.getCompanyRep()).append(",")
			  .append(internship.getNumOfSlots()).append("\n");
			writer.append(sb.toString());
			writer.flush();
			return true;
		} catch (IOException e) {
			System.out.println("Failed to save to file: " + e.getMessage());
			return false;
		}
	}
}
