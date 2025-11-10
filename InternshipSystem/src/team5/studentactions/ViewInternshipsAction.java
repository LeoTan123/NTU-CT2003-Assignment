package team5.studentactions;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import team5.App;
import team5.Internship;
import team5.InternshipApplication;
import team5.Student;
import team5.boundaries.ConsoleBoundary;
import team5.boundaries.InternshipBoundary;
import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipFilterOption;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;
import team5.filters.InternshipFilter;
import team5.filters.InternshipFilterCriteria;
import team5.filters.InternshipFilterPrompt;

public class ViewInternshipsAction implements StudentAction {

	private static final int PAGE_SIZE = 5;
	private static final List<InternshipFilterOption> FILTER_OPTIONS = List.of(
			InternshipFilterOption.PREFERRED_MAJOR,
			InternshipFilterOption.INTERNSHIP_LEVEL,
			InternshipFilterOption.APPLICATION_OPEN_FROM,
			InternshipFilterOption.APPLICATION_CLOSE_TO);

	@Override
	public void run(Student student) {
		ConsoleBoundary.printSectionTitle("Available Internship Opportunities");
		List<Internship> baseList = getApprovedInternships();
		if (baseList.isEmpty()) {
			System.out.println("No internships available at the moment.");
			return;
		}

		List<Internship> workingList = new ArrayList<>(baseList);
		int pageIndex = 0;
		boolean browsing = true;

		while (browsing) {
			if (workingList.isEmpty()) {
				System.out.println("No internships match your current filter.");
				System.out.println("Enter 'a' to show all internships or 0 to return.");
				String emptyInput = App.sc.nextLine().trim();
				if ("0".equals(emptyInput)) {
					return;
				}
				if ("a".equalsIgnoreCase(emptyInput)) {
					baseList = getApprovedInternships();
					workingList = new ArrayList<>(baseList);
					pageIndex = 0;
					continue;
				}
				ConsoleBoundary.printInvalidInput();
				continue;
			}
			int start = pageIndex * PAGE_SIZE;
			int end = Math.min(start + PAGE_SIZE, workingList.size());
			if (start >= workingList.size()) {
				pageIndex = 0;
				continue;
			}

			List<Internship> displayedInternships = new ArrayList<>();
			for (int i = start; i < end; i++) {
				Internship internship = workingList.get(i);
				displayedInternships.add(internship);
				int displayIndex = (i - start) + 1;
				System.out.printf("%d. %s%n", displayIndex, InternshipBoundary.buildSummaryLine(internship, false));
			}

			printNavigationPrompt(pageIndex > 0, end < workingList.size());
			String input = App.sc.nextLine().trim();

			if ("0".equals(input)) {
				return;
			} else if ("f".equalsIgnoreCase(input)) {
				baseList = getApprovedInternships();
				InternshipFilterCriteria criteria = InternshipFilterPrompt.prompt(FILTER_OPTIONS);
				if (criteria == null) {
					return;
				}
				workingList = InternshipFilter.apply(baseList, criteria);
				pageIndex = 0;
			} else if ("a".equalsIgnoreCase(input)) {
				baseList = getApprovedInternships();
				workingList = new ArrayList<>(baseList);
				pageIndex = 0;
			} else if ("n".equalsIgnoreCase(input)) {
				if (end < workingList.size()) {
					pageIndex++;
				} else {
					ConsoleBoundary.printInvalidInput();
				}
			} else if ("p".equalsIgnoreCase(input)) {
				if (pageIndex > 0) {
					pageIndex--;
				} else {
					ConsoleBoundary.printInvalidInput();
				}
			} else {
				try {
					int selection = Integer.parseInt(input);
					if (selection < 1 || selection > displayedInternships.size()) {
						ConsoleBoundary.printInvalidInput();
						continue;
					}

					Internship chosen = displayedInternships.get(selection - 1);
					handleInternshipActions(student, chosen);
				} catch (NumberFormatException ex) {
					ConsoleBoundary.printInvalidInput();
				}
			}
		}
	}
	
	private List<Internship> getApprovedInternships() {
		return App.internshipList.stream()
				.filter(i -> i.getInternshipStatus() == InternshipStatus.APPROVED)
				.collect(Collectors.toList());
	}
	
	private void printNavigationPrompt(boolean hasPrev, boolean hasNext) {
		System.out.print("Enter a number to view/apply");
		if (hasPrev) {
			System.out.print(", 'p' for previous page");
		}
		if (hasNext) {
			System.out.print(", 'n' for next page");
		}
		System.out.println(", 'f' to filter, 'a' to show all, or 0 to return:");
	}
	
	private void handleInternshipActions(Student student, Internship chosen) {
		boolean choosingAction = true;
		while (choosingAction) {
			System.out.println("You have chosen Internship ID: " + ConsoleBoundary.safeValue(chosen.getInternshipId())
					+ " Title: " + ConsoleBoundary.safeValue(chosen.getTitle()));
			System.out.println("1. View Internship Details");
			System.out.println("2. Apply for this Internship");
			System.out.println("0. Return to internship list");

			String actionStr = App.sc.nextLine();
			int action;
			try {
				action = Integer.parseInt(actionStr.trim());
			} catch (NumberFormatException ex) {
				ConsoleBoundary.printInvalidInput();
				continue;
			}

			switch (action) {
				case 0:
					choosingAction = false;
					break;
				case 1:
					displayInternshipDetails(chosen);
					break;
				case 2:
					applyForInternship(student, chosen);
					break;
				default:
					ConsoleBoundary.printInvalidInput();
			}
		}
	}

	private void displayInternshipDetails(Internship internship) {
		ConsoleBoundary.printSectionTitle("Internship Details");		
		System.out.println("Internship ID: " + ConsoleBoundary.safeValue(internship.getInternshipId()));
		System.out.println("Company Name: " + ConsoleBoundary.safeValue(internship.getCompanyName()));
		System.out.println("Title: " + ConsoleBoundary.safeValue(internship.getTitle()));
		System.out.println("Description: " + ConsoleBoundary.safeValue(internship.getDescription()));
		System.out.println("Level: " + ConsoleBoundary.valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Majors: " + ConsoleBoundary.safeValue(internship.getPreferredMajor().getFullName()));
		System.out.println("Application Open Date: " + ConsoleBoundary.safeValue(internship.getApplicationOpenDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Application Close Date: " + ConsoleBoundary.safeValue(internship.getApplicationCloseDate().format(App.DATE_DISPLAY_FORMATTER)));
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
}
