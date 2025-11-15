package team5.studentactions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import team5.App;
import team5.Internship;
import team5.InternshipApplication;
import team5.Student;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.CsvFileBoundary;
import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipStatus;
import team5.interfaces.ApplicationCsvRepository;

/**
 * Sub-controller class for student to check internship application status
 */
public class CheckApplicationStatusAction implements StudentAction {
	
	private final ApplicationCsvRepository applicationRepository;
	
	public CheckApplicationStatusAction() {
		this(new CsvFileBoundary());
	}
	
	public CheckApplicationStatusAction(ApplicationCsvRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}
	
	@Override
	public void run(Student student) {
		ConsoleBoundary.printSectionTitle("Your Internship Applications");
        List<InternshipApplication> applications = student.getInternshipApplications();
        boolean reviewing = true;
        while (reviewing) {
            if (applications.isEmpty()) {
                System.out.println("No internship applications found.");
                return;
            }
        	boolean hasInternship = false;
			for (int i = 0; i < applications.size(); i++) {
				InternshipApplication internship = applications.get(i);
				hasInternship = true;
				System.out.printf("%d. Application ID %s | Title: %s | Application Date: %s | Status: %s%n",
						i + 1,
						ConsoleBoundary.safeValue(internship.getApplicationId()),
						ConsoleBoundary.safeValue(internship.getInternshipInfo().getTitle()),
						ConsoleBoundary.safeValue(internship.getAppliedDate().format(App.DATE_DISPLAY_FORMATTER)),
						ConsoleBoundary.valueOrNA(internship.getStatus()));
			}
			if (!hasInternship) {
				System.out.println("No internships available at the moment.");
				reviewing = false;
				break;
		    }
			
			String inputStr = ConsoleBoundary.promptUserInput(true);
	        int input = 0;
	        try 
	        {
	            input = Integer.parseInt(inputStr);

	            if (input == 0) {
	            	reviewing = false;
	                break;
	            }

	            if (input < 1 || input > applications.size()) {
	            	ConsoleBoundary.printInvalidInput();
	                continue;
	            }
	            
	            InternshipApplication chosen = applications.get(input - 1);
	         
	            // Show action options for the chosen internship
	            boolean choosingAction = true;
	            while (choosingAction) {
	            	System.out.println("You have chosen Application ID: " + ConsoleBoundary.safeValue(chosen.getApplicationId()) + " Title: " + ConsoleBoundary.safeValue(chosen.getInternshipInfo().getTitle()));
	                System.out.println("1. View application details");
	                System.out.println("2. Accept this internship offer");
	                System.out.println("0. Return to internship list");

	                String actionStr = ConsoleBoundary.promptUserInput(true);
	                int action = -1;
	                try {
	                    action = Integer.parseInt(actionStr);
	                } 
	                catch (NumberFormatException ex) {
	                	ConsoleBoundary.printInvalidInput();
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
	                    boolean accepted = acceptOffer(student, chosen);
	                    if (accepted) {
	                    	choosingAction = false;
	                    	reviewing = false;
	                    }
	                    break;
	                    default:
	                    	ConsoleBoundary.printInvalidInput();
	                }
	            }
	        } 
	        catch (NumberFormatException ex) {
	        	ConsoleBoundary.printInvalidInput();
	        }
        }
	}
	
	private void displayApplicationDetails(InternshipApplication chosen) {
		ConsoleBoundary.printSectionTitle("Application Details");		
		System.out.println("Application ID: " + ConsoleBoundary.safeValue(chosen.getApplicationId()));
		System.out.println("Internship ID: " + ConsoleBoundary.safeValue(chosen.getInternshipInfo().getInternshipId()));
		System.out.println("Company Name: " + ConsoleBoundary.safeValue(chosen.getInternshipInfo().getCompanyName()));
		System.out.println("Title: " + ConsoleBoundary.safeValue(chosen.getInternshipInfo().getTitle()));
		System.out.println("Description: " + ConsoleBoundary.safeValue(chosen.getInternshipInfo().getDescription()));
		System.out.println("Application Date: " + chosen.getAppliedDate().format(App.DATE_DISPLAY_FORMATTER));
		System.out.println("Status: " + ConsoleBoundary.valueOrNA(chosen.getStatus()));
		
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
	
	private boolean acceptOffer(Student student, InternshipApplication chosen)
	{
		// If internship is fully booked
		if(chosen.getInternshipInfo().getInternshipStatus() == InternshipStatus.FILLED 
		|| chosen.getInternshipInfo().getNumOfSlots() == 0){
			System.out.println("This internship is fully booked.");
			if (chosen.getStatus() != InternshipApplicationStatus.UNSUCCESSFUL) {
				chosen.setStatus(InternshipApplicationStatus.UNSUCCESSFUL);
				persistApplications();
			}
			return false;
		}
		
		// Prevent re-acceptance
		if (chosen.getStatus() == InternshipApplicationStatus.ACCEPTED) {
			System.out.println("You have already accepted this internship offer.");
			return false;
		}
		
		// If internship application is not successful
		if(chosen.getStatus() != InternshipApplicationStatus.SUCCESSFUL){
			System.out.println("Internship application status is " + chosen.getStatus().toString()+ ". Please wait for approval.");
			return false;
		}

		// If student is employed already
		if(student.getEmployedStatus()){
			System.out.println("You have already accepted another internship opportunity. You are not allowed to accept this internship.");
			return false;
		}
		
		System.out.println("You have accepted offer for internship " + chosen.getInternshipInfo().getTitle() + ".");
		System.out.println("Please email to " + chosen.getInternshipInfo().getCompanyRep() + " or visit " + chosen.getInternshipInfo().getCompanyName() + " for more details.");
		
		student.setEmployedStatus(true); // Set Student as employed
		
		// Keep only the accepted application for the student
		student.getInternshipApplications().removeIf(app -> !app.getApplicationId().equals(chosen.getApplicationId()));
		
		// Update global list: keep only the accepted record for this student
		App.internshipApplicationList.removeIf(app -> app.getStudentInfo().getUserID().equals(student.getUserID()) &&
			!app.getApplicationId().equals(chosen.getApplicationId()));
		
		// Update application status
		chosen.setStatus(InternshipApplicationStatus.ACCEPTED);
		
		// Update the NumOfSlots of the specific internship
		Internship internshipInfo = chosen.getInternshipInfo();
		int remainingSlots = internshipInfo.getNumOfSlots();
		if (remainingSlots > 0) {
			internshipInfo.setNumOfSlots(remainingSlots - 1);
		    if (internshipInfo.getNumOfSlots() == 0) {
		    	internshipInfo.setInternshipStatus(InternshipStatus.FILLED);
		    }
		    // Update internship record in CSV
	        updateInternshipCsv(internshipInfo);
		}
		// Save updated internship application list to CSV
		persistApplications();
		return true;
	}
	
	private void updateInternshipCsv(Internship updatedInternship) {
	    String filePath = App.envFilePathInternship;
	    List<String> lines = new ArrayList<>(); // Placeholder for all lines in CSV

	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String header = br.readLine();
	        if (header != null) 
	        	lines.add(header); // Add CSV header

	        String line;
	        while ((line = br.readLine()) != null) { // Read every line in CSV
	            String[] values = line.split(",");
	            // Find the internshipId
	            if (values[0].equals(updatedInternship.getInternshipId())) {
	            	// Replace it with updated InternshipStatus (for FILLED)
	            	values[7] = ConsoleBoundary.valueOrNA(updatedInternship.getInternshipStatus()); 
	            	// Replace it with updated numOfSlots
	            	values[10] = ConsoleBoundary.safeValue(String.valueOf(updatedInternship.getNumOfSlots())); 
	                line = String.join(",", values); // Rebuild the line
	            }
	            lines.add(line); // Add the line to placeholder
	        }
	    } 
	    catch (IOException e) {
	    	ConsoleBoundary.printErrorMessage();
	        e.printStackTrace();
	    }

	    // Rewrite the CSV
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
	        for (String l : lines) {
	            bw.write(l);
	            bw.newLine(); // Line separator \n
	        }
	    } 
	    catch (IOException e) {
	    	ConsoleBoundary.printErrorMessage();
	        e.printStackTrace();
	    }
	}
	
	private void persistApplications() {
		if (applicationRepository == null) {
			return;
		}
		boolean success = applicationRepository.writeInternshipApplications(App.internshipApplicationList);
		if (!success) {
			System.out.println("Warning: Failed to save application updates. Please try again later.");
		}
	}
}
