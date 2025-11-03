package team5.studentactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import team5.App;
import team5.Internship;
import team5.InternshipApplication;
import team5.Student;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

public class ViewInternshipsAction implements StudentAction {
	private final static String ErrorMessage = "Invalid input. Try again";
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
				// Filter option
				List<Internship> filteredInternships = filterInternships(App.internshipList);
				
				// Need to choose based on level
				boolean hasInternship = false;
				// To show normal indexing (1,2,3...)
				int internshipIndex = 1;
				// To store after filtered internships (only APPROVED internship)
				List<Internship> displayedInternships = new ArrayList<>();
				
				for(Internship internship: filteredInternships)
				{
					if(internship.getInternshipStatus() == InternshipStatus.APPROVED)
					{
						hasInternship = true;
						displayedInternships.add(internship);
						System.out.printf("%d. Internship ID: %s | Title: %s | Level: %s | Preffered Major: %s | "
								+ "Application Opening Date: %s | Application Closing Date: %s%n",
								internshipIndex,
								internship.getInternshipId(),
								safeValue(internship.getTitle()),
								valueOrNA(internship.getInternshipLevel()),
								safeValue(internship.getPreferredMajor().getFullName()),
								internship.getApplicationOpenDate().format(App.DATE_DISPLAY_FORMATTER),
								internship.getApplicationCloseDate().format(App.DATE_DISPLAY_FORMATTER)
								);
						
						internshipIndex++;
					}
				}
				if (!hasInternship) {
					System.out.println("No internships available at the moment.");
					return;
			    }
				
				while(true)
				{
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

			            // display the chosen internship
			            Internship chosen = displayedInternships.get(input - 1);
			         
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
			                        displayInternshipDetails(chosen); // waits for Enter inside
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
		System.out.println();

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
	    
	    InternshipApplication internApp = new InternshipApplication(student.getInternshipApplications().size() + 1,
	    		chosen, student, LocalDate.now());
	    student.getInternshipApplications().add(internApp);
	    chosen.setNumOfSlots(chosen.getNumOfSlots() - 1);
	    
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
		    System.out.println("1. Filter by Preferred Major");
		    System.out.println("2. Filter by Internship Level");
		    System.out.println("3. Filter by Application Open Date");
		    System.out.println("4. Filter by Application Close Date");
		    System.out.println("0. Show all internships");
	
		    System.out.println("Enter your choice: ");
		    String filterInput = App.sc.nextLine().trim();

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
		        case "0":
		        	validInput = true;
		        	System.out.println("Showing all internships.");
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
