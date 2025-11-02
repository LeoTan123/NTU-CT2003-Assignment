package team5.studentactions;

import java.time.LocalDate;

import team5.App;
import team5.Internship;
import team5.InternshipApplication;
import team5.Student;
import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

public class ViewInternshipsAction implements StudentAction {

	@Override
	public void run(Student student) {
		boolean browsing = true;
		while (browsing) {
			App.printSectionTitle("Internship Opportunities");
			if (App.internshipList.isEmpty()) {
				System.out.println("No internships available at the moment.");
				return;
			} 
			else 
			{
				boolean hasInternship = false;
				for (int i = 0; i < App.internshipList.size(); i++) {
					Internship internship = App.internshipList.get(i);
					if(internship.getInternshipStatus() == InternshipStatus.PENDING 
					|| internship.getInternshipStatus() == InternshipStatus.REJECTED
					|| internship.getInternshipStatus() == InternshipStatus.FILLED)
						continue;
					
					if(student.getYear() <= 2 && internship.getInternshipLevel() != InternshipLevel.BASIC)
						continue;
					
					hasInternship = true;
					System.out.printf("%d. Internship ID: %s | Title: %s | Level: %s | Preffered Major: %s%n",
							i + 1,
							internship.getInternshipId(),
							safeValue(internship.getTitle()),
							valueOrNA(internship.getInternshipLevel()),
							safeValue(internship.getPreferredMajor().getFullName()));
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

		            if (input < 1 || input > App.internshipList.size()) {
		                System.out.println("Invalid selection. Please try again.");
		                continue;
		            }

		            // display the chosen internship
		            Internship chosen = App.internshipList.get(input - 1);
		         
		            // Show action options for the chosen internship
		            boolean choosingAction = true;
		            while (choosingAction) {
		                System.out.println("1. View Details");
		                System.out.println("2. Apply for Internship");
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
		                        displayInternshipDetails(chosen); // waits for Enter inside
		                        break;
		                    case 2:
		                        applyForInternship(student, chosen);
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

	private void displayInternshipDetails(Internship internship) {
		App.printSectionTitle("Internship Details");		
		System.out.println("Internship ID: " + internship.getInternshipId());
		System.out.println("Title: " + safeValue(internship.getTitle()));
		System.out.println("Description: " + safeValue(internship.getDescription()));
		System.out.println("Level: " + valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Majors: " + safeValue(internship.getPreferredMajor()));
		System.out.println("Application Open Date: " + safeValue(internship.getApplicationOpenDate()));
		System.out.println("Application Close Date: " + safeValue(internship.getApplicationCloseDate()));
		System.out.println("Number of Slots: " + internship.getNumOfSlots());
		System.out.println("Press Enter to return to the internship list.");
		App.sc.nextLine();
	}
	
	private void applyForInternship(Student student, Internship chosen)
	{
		LocalDate today = LocalDate.now();

	    // Check application period
	    if (today.isBefore(chosen.getApplicationOpenDate()) || today.isAfter(chosen.getApplicationCloseDate())) {
	        System.out.println("The application period for this internship is closed.");
	        return;
	    }
	    
	    // Check student number of application
	    if (student.getInternshipApplications().size() >= 3) {
	        System.out.println("You have already applied for 3 internships. You are not allowed to apply this internship.");
	        return;
	    }
		
	    // Check student accepted offer or not
		if(student.getEmployedStatus())
		{
			System.out.println("You have already accepted another internship. You are not allowed to apply this internship.");
			return;
		}
		
		// Check student applied this offer before or not
		for (InternshipApplication app : student.getInternshipApplications()) {
		    if (app.getInternshipInfo().equals(chosen)) {
		        System.out.println("You have already applied for this internship before.");
		        return;
		    }
		}
		
		// Check if the student is eligible by year
	    if (student.getYear() <= 2 && chosen.getInternshipLevel() != InternshipLevel.BASIC) {
	        System.out.println("You are not eligible for this internship.");
	        return;
	    }

	    // Check preferred major 
	    StudentMajor preferred = chosen.getPreferredMajor();
	    if (preferred != null && student.getMajor() != preferred) {
	        System.out.println("This internship prefers students from " + preferred.getFullName() + ".");
	        return;
	    }

	    // Check if slots are available
	    if (chosen.getNumOfSlots() <= 0 || chosen.getInternshipStatus() == InternshipStatus.FILLED) {
	        System.out.println("This internship is fully booked.");
	        return;
	    }
	    
	    InternshipApplication internApp = new InternshipApplication(student.getInternshipApplications().size() + 1,
	    		chosen, student, LocalDate.now());
	    student.getInternshipApplications().add(internApp);
	    chosen.setNumOfSlots(chosen.getNumOfSlots() - 1);
	    
	    System.out.println("Successfully applied for internship: " + chosen.getTitle());
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
