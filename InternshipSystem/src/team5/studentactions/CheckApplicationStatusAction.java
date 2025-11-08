package team5.studentactions;

import java.util.List;

import team5.App;
import team5.Internship;
import team5.InternshipApplication;
import team5.Student;
import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipStatus;

public class CheckApplicationStatusAction implements StudentAction {
	private final static String ErrorMessage = "Invalid input. Try again.";
	@Override
	public void run(Student student) {
        List<InternshipApplication> applications = student.getInternshipApplications();
        boolean reviewing = true;
        while (reviewing) {
            if (applications.isEmpty()) {
                System.out.println("No internship applications found.");
                return;
            } 
        	App.printSectionTitle("Your Internship Applications");
        	boolean hasInternship = false;
			for (int i = 0; i < applications.size(); i++) {
				InternshipApplication internship = applications.get(i);
				hasInternship = true;
				System.out.printf("%d. Title: %s | Application Date: %s | Application Status: %s%n",
						i + 1,
						safeValue(internship.getInternshipInfo().getTitle()),
						safeValue(internship.getAppliedDate().format(App.DATE_DISPLAY_FORMATTER)),
						valueOrNA(internship.getStatus()));
			}
			if (!hasInternship) {
				System.out.println("No internships available at the moment.");
				reviewing = false;
				break;
		    }
			System.out.println("Select internship number (or 0 to return):");
	        String inputStr = App.sc.nextLine();
	        int input = 0;

	        try 
	        {
	            input = Integer.parseInt(inputStr.trim());

	            if (input == 0) {
	            	reviewing = false;
	                break;
	            }

	            if (input < 1 || input > applications.size()) {
	                System.out.println(ErrorMessage);
	                continue;
	            }
	            
	            InternshipApplication chosen = applications.get(input - 1);
	         
	            // Show action options for the chosen internship
	            boolean choosingAction = true;
	            while (choosingAction) {
	            	System.out.println("You have chosen Application ID: " + chosen.getApplicationId() + " Title: " + safeValue(chosen.getInternshipInfo().getTitle()));
	                System.out.println("1. View Application Details");
	                System.out.println("2. Accept this offer");
	                System.out.println("0. Return to list");

	                String actionStr = App.sc.nextLine();
	                int action = -1;
	                try {
	                    action = Integer.parseInt(actionStr.trim());
	                } 
	                catch (NumberFormatException ex) {
	                	System.out.println(ErrorMessage);
	                    continue;
	                }

	                switch (action) {
	                    case 0:
	                        choosingAction = false;
	                        break;
	                    case 1:
	                        displayApplicationDetails(chosen);
	                        break;
	                    case 2:
	                        acceptOffer(student, chosen);
	                        break;
	                    default:
	                    	System.out.println(ErrorMessage);
	                }
	            }

	        } 
	        catch (NumberFormatException ex) {
	        	System.out.println(ErrorMessage);
	        }
        }
	}
	
	private void displayApplicationDetails(InternshipApplication chosen) {
		App.printSectionTitle("Application Details");		
		System.out.println("Application ID: " + safeValue(chosen.getApplicationId()));
		System.out.println("Internship ID: " + safeValue(chosen.getInternshipInfo().getInternshipId()));
		System.out.println("Title: " + safeValue(chosen.getInternshipInfo().getTitle()));
		System.out.println("Description: " + safeValue(chosen.getInternshipInfo().getDescription()));
		System.out.println("Application Date: " + chosen.getAppliedDate().format(App.DATE_DISPLAY_FORMATTER));
		System.out.println("Status: " + valueOrNA(chosen.getStatus()));
		
		while (true) {
	        System.out.println("Enter 0 to return to the internship action list:");
	        String input = App.sc.nextLine().trim();

	        if (input.equals("0")) {
	            return;
	        } else {
	            System.out.println("Invalid input. Please enter 0 to return.");
	        }
	    }
	}
	
	private void acceptOffer(Student student, InternshipApplication chosen)
	{
		// If internship is fully booked
		if(chosen.getInternshipInfo().getInternshipStatus() == InternshipStatus.FILLED 
		|| chosen.getInternshipInfo().getNumOfSlots() == 0)
		{
			System.out.println("Internship is fully booked.");
			return;
		}
		
		// If internship application is not successful
		if(chosen.getStatus() != InternshipApplicationStatus.SUCCESSFUL)
		{
			System.out.println("Internship application status is " + chosen.getStatus().toString()+ ". Please wait for approval.");
			return;
		}

		// If student is employed already
		if(student.getEmployedStatus())
		{
			System.out.println("You have already accepted another internship opportunity. You are not allowed to accept this internship.");
			return;
		}
		
		System.out.println("You have accepted offer for internship " 
		+ chosen.getInternshipInfo().getTitle() + ". Please contact " 
		+ chosen.getInternshipInfo().getCompanyRep() + " for more details.");
		
		student.setEmployedStatus(true); // Set Student as employed
		student.clearInternshipApplications(); // Clear application list after accepted offer
		
		// Update the NumOfSlots of the specific internship
		Internship internshipInfo = chosen.getInternshipInfo();
		int remainingSlots = internshipInfo.getNumOfSlots();
		if (remainingSlots > 0) {
			internshipInfo.setNumOfSlots(remainingSlots - 1);
		    if (internshipInfo.getNumOfSlots() == 0) {
		    	internshipInfo.setInternshipStatus(InternshipStatus.FILLED);
		    }
		}
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
}
