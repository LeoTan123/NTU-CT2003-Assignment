package team5.studentactions;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import team5.App;
import team5.Internship;
import team5.InternshipApplication;
import team5.Student;
import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

public class ViewInternshipsAction implements StudentAction {
	private final static String ErrorMessage = "Invalid input. Try again.";
	@Override
	public void run(Student student) {
		boolean browsing = true;
		while (browsing) {
			if (App.internshipList.isEmpty()) {
				System.out.println("No internships available at the moment.");
				return;
			} 
			
			// Filter option
			List<Internship> filteredInternships = filterInternships(App.internshipList);
			if (filteredInternships == null)
				return;
			
			// To store after filtered internships (only APPROVED internship)
			List<Internship> displayedInternships = new ArrayList<>();
			
			while(true)
			{
				App.printSectionTitle("Available Internship Opportunities");
				
				boolean hasInternship = false;
				int internshipIndex = 1;
				displayedInternships.clear();
				for(Internship internship: filteredInternships)
				{
					if(internship.getInternshipStatus() == InternshipStatus.APPROVED) // TODO: to only show based on student's major
					{
						hasInternship = true;
						displayedInternships.add(internship);
						System.out.printf("%d. Internship ID: %s | Title: %s | Level: %s | Preferred Major: %s | "
								+ "Application Opening Date: %s | Application Closing Date: %s%n",
								internshipIndex,
								internship.getInternshipId(),
								safeValue(internship.getTitle()),
								valueOrNA(internship.getInternshipLevel()),
								safeValue(internship.getPreferredMajor().getFullName()),
								safeValue(internship.getApplicationOpenDate().format(App.DATE_DISPLAY_FORMATTER)),
								safeValue(internship.getApplicationCloseDate().format(App.DATE_DISPLAY_FORMATTER))
								);
						
						internshipIndex++;
					}
				}
				if (!hasInternship) {
					System.out.println("No internships available at the moment.");
					browsing = false;
					break;
			    }
				
				System.out.println("Select internship number (or 0 to return):");
		        String inputStr = App.sc.nextLine();
		        int input = 0;

		        try 
		        {
		            input = Integer.parseInt(inputStr.trim());

		            if (input == 0) {
		            	browsing = false;
		                break;
		            }

		            if (input < 1 || input > displayedInternships.size()) {
		                System.out.println(ErrorMessage);
		                continue;
		            }
		            
		            Internship chosen = displayedInternships.get(input - 1);
		         
		            // Show action options for the chosen internship
		            boolean choosingAction = true;
		            while (choosingAction) {
		            	System.out.println("You have chosen Internship ID: " + chosen.getInternshipId() + " Title: " + safeValue(chosen.getTitle()));
		                System.out.println("1. View Internship Details");
		                System.out.println("2. Apply for this Internship");
		                System.out.println("0. Return to internship list");

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
		                        choosingAction = false; // exit this internship's actions
		                        break;
		                    case 1:
		                        displayInternshipDetails(chosen);
		                        break;
		                    case 2:
		                        applyForInternship(student, chosen);
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
	}

	private void displayInternshipDetails(Internship internship) {
		App.printSectionTitle("Internship Details");		
		System.out.println("Internship ID: " + internship.getInternshipId());
		System.out.println("Title: " + safeValue(internship.getTitle()));
		System.out.println("Description: " + safeValue(internship.getDescription()));
		System.out.println("Level: " + valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Majors: " + safeValue(internship.getPreferredMajor().getFullName()));
		System.out.println("Application Open Date: " + safeValue(internship.getApplicationOpenDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Application Close Date: " + safeValue(internship.getApplicationCloseDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Number of Slots: " + internship.getNumOfSlots());

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
	    
	    // Generate new random application ID
		String[] idsArray = App.internshipApplicationList.stream().map(i -> i.getApplicationId()).toArray(String[]::new);
		String generatedId = App.generateUniqueId("A", idsArray);
		
	    InternshipApplication internApp = new InternshipApplication(generatedId, chosen, student, today, InternshipApplicationStatus.PENDING);

	    // Add to student's own internship application list
	    student.addInternshipApplications(internApp);
	    // Add to the global list for database saving
	    App.internshipApplicationList.add(internApp);
	    appendInternshipApplicationToCsv(internApp);
	    System.out.println("Successfully applied for internship: " + chosen.getTitle());
	}

	
	// Filter
	private List<Internship> filterInternships(List<Internship> internships) {
		
		List<Internship> filteredList = new ArrayList<>(internships);
		boolean validInput = false;
		
		boolean majorValidInput = false;
		boolean levelValidInput = false;
		
		while (!validInput) {
		    System.out.println("Would you like to filter internships?");
		    System.out.println("1. Filter by Preferred Major"); // TODO: student should not have this option
		    System.out.println("2. Filter by Internship Level"); // TODO: only year 3 and year 4 should have this option as year 1 and 2 can only view basic level internships
		    System.out.println("3. Filter by Application Open Date");
		    System.out.println("4. Filter by Application Close Date");
		    System.out.println("5. Show all internships");
		    System.out.println("0. Return to menu");
	
		    System.out.println("Enter your choice: ");
		    String filterInput = App.sc.nextLine().trim();
		    
		    if ("0".equals(filterInput)) {
	            return null;
	        }
		    
		    switch (filterInput) {
		        case "1":
		        	validInput = true;
		        	
		        	StudentMajor selectedMajor = null;
		        	while(!majorValidInput)
		        	{
		        		System.out.println("Select preferred major:");
			            System.out.println("1. Computer Science");
			            System.out.println("2. Data Science & AI");
			            System.out.println("3. Computer Engineering");
			            System.out.println("4. Information Engineering & Media");
			            System.out.println("5. Computing");
			            
			            System.out.println("Enter your choice: ");
			            String majorChoice = App.sc.nextLine().trim();
			            switch (majorChoice) {
			                case "1": 
			                	selectedMajor = StudentMajor.CS; 
			                	majorValidInput = true;
			                	break;
			                case "2": 
			                	selectedMajor = StudentMajor.DSAI; 
			                	majorValidInput = true;
			                	break;
			                case "3": 
			                	selectedMajor = StudentMajor.CE; 
			                	majorValidInput = true;
			                	break;
			                case "4": 
			                	selectedMajor = StudentMajor.IEM; 
			                	majorValidInput = true;
			                	break;
			                case "5": 
			                	selectedMajor = StudentMajor.COMP; 
			                	majorValidInput = true;
			                	break;
			                default:
			                    System.out.println(ErrorMessage);
			            }
		        	}
		            if (selectedMajor != null) {
		            	final StudentMajor finalMajor = selectedMajor;
		                filteredList = filteredList.stream()
		                    .filter(i -> i.getPreferredMajor() != null
		                              && i.getPreferredMajor() == finalMajor)
		                    .toList();
		            }
		            break;
	
		        case "2":
		        	validInput = true;
		        	InternshipLevel selectedLevel = null;
		        	
		        	while(!levelValidInput)
		        	{
		        		System.out.println("Select internship level:");
			            System.out.println("1. BASIC");
			            System.out.println("2. INTERMEDIATE");
			            System.out.println("3. ADVANCED");
			            System.out.println("Enter your choice: ");
			            
			            String levelChoice = App.sc.nextLine().trim();
			            switch (levelChoice) {
			                case "1":
			                	selectedLevel = InternshipLevel.BASIC;
			                	levelValidInput = true;
			                    break;
			                case "2":
			                	selectedLevel = InternshipLevel.INTERMEDIATE;
			                	levelValidInput = true;
			                    break;
			                case "3":
			                	selectedLevel = InternshipLevel.ADVANCED;
			                	levelValidInput = true;
			                    break;
			                default:
			                    System.out.println(ErrorMessage);
			                    break;
			            }
		        	}
		            if (selectedLevel != null) {
		            	final InternshipLevel finalLevel = selectedLevel;
		                filteredList = filteredList.stream()
		                    .filter(i -> i.getInternshipLevel() != null
		                              && i.getInternshipLevel() == finalLevel)
		                    .toList();
		            }
		            break;
	
		        case "3":
		        	validInput = true;
		            System.out.println("Enter minimum open date (yyyy-MM-dd): ");
		            String openStr = App.sc.nextLine().trim();
		            try {
		                LocalDate openDate = LocalDate.parse(openStr);
		                filteredList = filteredList.stream()
		                    .filter(i -> i.getApplicationOpenDate() != null 
		                              && !i.getApplicationOpenDate().isBefore(openDate))
		                    .toList();
		            } 
		            catch (Exception e) {
		                System.out.println("Invalid date format. Showing all internships.");
		            }
		            break;
	
		        case "4":
		        	validInput = true;
		            System.out.println("Enter maximum close date (yyyy-MM-dd): ");
		            String closeStr = App.sc.nextLine().trim();
		            try {
		                LocalDate closeDate = LocalDate.parse(closeStr);
		                filteredList = filteredList.stream()
		                    .filter(i -> i.getApplicationCloseDate() != null 
		                              && !i.getApplicationCloseDate().isAfter(closeDate))
		                    .toList();
		            } 
		            catch (Exception e) {
		                System.out.println("Invalid date format. Showing all internships.");
		            }
		            break;
		        case "5":
		        	validInput = true;
		        	break;
		        default:
		        	System.out.println(ErrorMessage);
		    }
	    }

	    if (filteredList.isEmpty()) {
	        System.out.println("No internships match your filter. Showing all internships.");
	        return new ArrayList<>(internships);
	    }

	    return filteredList;
	}
	
	// Write to CSV
	private static boolean appendInternshipApplicationToCsv(InternshipApplication internship) {
		try (FileWriter writer = new FileWriter(App.envFilePathInternshipApplication, true)) {
			writer.append(buildInternshipString(internship).toString());
			writer.flush();
			return true;
		} 
		catch (IOException e) {
			System.out.println("Failed to save to file: " + e.getMessage());
        	System.out.println("Stack trace:");
        	e.printStackTrace();
			return false;
		}
	}
	
	private static StringBuilder buildInternshipString(InternshipApplication internshipApp) {
		StringBuilder sb = new StringBuilder();
		sb.append(internshipApp.getApplicationId()).append(",")
		  .append(internshipApp.getInternshipInfo().getInternshipId()).append(",")
		  .append(internshipApp.getStudentInfo().getName()).append(",")
		  .append(internshipApp.getAppliedDate().format(App.DATE_DB_FORMATTER)).append(",")
		  .append(internshipApp.getStatus()).append("\n");
		return sb;
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
