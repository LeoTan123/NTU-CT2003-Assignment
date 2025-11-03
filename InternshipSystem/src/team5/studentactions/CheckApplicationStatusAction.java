package team5.studentactions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import team5.App;
import team5.Internship;
import team5.InternshipApplication;
import team5.Student;
import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;

public class CheckApplicationStatusAction implements StudentAction {

	@Override
	public void run(Student student) {
        List<InternshipApplication> applications = student.getInternshipApplications();
        boolean reviewing = true;
        while (reviewing) {
        	App.printSectionTitle("Internship Applications");
            if (applications.isEmpty()) {
                System.out.println("No internship applications found.");
                return;
            } 
            else
            {
            	boolean hasInternship = false;
				for (int i = 0; i < applications.size(); i++) {
					InternshipApplication internship = applications.get(i);
					hasInternship = true;
					System.out.printf("%d. Title: %s | Application Date: %s | Application Status: %s%n",
							i + 1,
							safeValue(internship.getInternshipInfo().getTitle()),
							formatDate(internship.getAppliedDate()),
							valueOrNA(internship.getStatus()));
				}
				if (!hasInternship) {
					System.out.println("No internships available at the moment.");
					return;
			    }
				System.out.println("\nSelect internship number (or 0 to return):");
		        String inputStr = App.sc.nextLine();
		        int input = 0;

		        try 
		        {
		            input = Integer.parseInt(inputStr.trim());

		            if (input == 0) {
		                return;
		            }

		            if (input < 1 || input > applications.size()) {
		                System.out.println("Invalid selection. Please try again.");
		                continue;
		            }

		            // display the chosen internship
		            InternshipApplication chosen = applications.get(input - 1);
		         
		            // Show action options for the chosen internship
		            boolean choosingAction = true;
		            while (choosingAction) {
		                System.out.println("1. View Details");
		                System.out.println("2. Accept offer");
		                System.out.println("0. Return to list");

		                String actionStr = App.sc.nextLine();
		                int action = -1;
		                try {
		                    action = Integer.parseInt(actionStr.trim());
		                } catch (NumberFormatException ex) {
		                    System.out.println("Please enter a valid number.");
		                    continue;
		                }

		                switch (action) {
		                    case 0:
		                        choosingAction = false; // exit this internship's actions
		                        break;
		                    case 1:
		                        displayApplicationDetails(chosen); // waits for Enter inside
		                        break;
		                    case 2:
		                        acceptOffer(student, chosen);
		                        break;
		                    default:
		                        System.out.println("Invalid option. Try again.");
		                }
		            }

		        } catch (NumberFormatException ex) {
		            System.out.println("Please enter a valid number.");
		        }
            }
        }
	}
	
	private void displayApplicationDetails(InternshipApplication chosen) {
		App.printSectionTitle("Internship Application Details");		
		System.out.println("Application ID: " + chosen.getApplicationId());
		System.out.println("Title: " + safeValue(chosen.getInternshipInfo().getTitle()));
		System.out.println("Description: " + safeValue(chosen.getInternshipInfo().getDescription()));
		System.out.println("Application Date: " + chosen.getAppliedDate().format(App.DATE_DISPLAY_FORMATTER));
		System.out.println("Application Status: " + valueOrNA(chosen.getStatus()));
		System.out.println("Press Enter to return to the application list.");
		App.sc.nextLine();
	}
	
	private void acceptOffer(Student student, InternshipApplication chosen)
	{
		// If internship is fully booked
		if(chosen.getInternshipInfo().getInternshipStatus() == InternshipStatus.FILLED)
		{
			System.out.println("Internship is fully booked.");
			return;
		}
		
		// If internship application is not successful
		if(chosen.getStatus() != InternshipApplicationStatus.SUCCESSFUL)
		{
			System.out.println("Internship application status is " + chosen.getStatus().toString()+ ".");
			return;
		}

		// If student is employed already
		if(student.getEmployedStatus())
		{
			System.out.println("You have already accepted another internship. You are not allowed to accept this internship.");
			return;
		}
		
		System.out.println("You have accepted offer for internship " 
		+ chosen.getInternshipInfo().getTitle() + ". Please contact " 
		+ chosen.getInternshipInfo().getCompanyRep() + " for more details.");
		
		student.setEmployedStatus(true);
		chosen.setHasStudentAccepted(true);
		return;
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
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private String formatDate(LocalDate date) {
	    return (date == null) ? "N/A" : date.format(DATE_FORMATTER);
	}
}
