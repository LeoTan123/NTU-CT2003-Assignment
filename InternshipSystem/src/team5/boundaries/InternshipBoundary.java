package team5.boundaries;

import java.time.LocalDate;
import java.util.ArrayList;

import team5.App;
import team5.Internship;
import team5.companyrepactions.CreateInternshipAction;
import team5.enums.InternshipLevel;
import team5.enums.StudentMajor;

public class InternshipBoundary extends ConsoleBoundary {
	
	public InternshipBoundary() { }
	
	/*
	 * Convention:
	 * - print: To only display info
	 * - display: To display menu and ask for input
	 * - prompt: To prompt user input for form fields
	 * */
	
	public static void printInternshipList(ArrayList<Internship> internshipList) {
		printInternshipList(internshipList, false);
	}
	
	public static void printInternshipList(ArrayList<Internship> internshipList, boolean includeCompanyName) {
		for (int i = 0; i < internshipList.size(); i++) {
			Internship internship = internshipList.get(i);
			System.out.printf("%d. %s%n", i + 1, buildSummaryLine(internship, includeCompanyName));
		}
	}
	
	public static String buildSummaryLine(Internship internship, boolean includeCompanyName) {
		String preferredMajor = internship.getPreferredMajor() != null
				? internship.getPreferredMajor().getFullName()
				: "N/A";
		String openDate = formatDate(internship.getApplicationOpenDate());
		String closeDate = formatDate(internship.getApplicationCloseDate());
		
		StringBuilder sb = new StringBuilder();
		sb.append("Internship ID: ").append(internship.getInternshipId())
		  .append(" | Title: ").append(safeValue(internship.getTitle()))
		  .append(" | Level: ").append(valueOrNA(internship.getInternshipLevel()))
		  .append(" | Preferred Major: ").append(preferredMajor)
		  .append(" | Application Opening Date: ").append(openDate)
		  .append(" | Application Closing Date: ").append(closeDate);
		
		if (includeCompanyName) {
			sb.append(" | Company Name: ").append(safeValue(internship.getCompanyName()));
		}
		return sb.toString();
	}
	
	private static String formatDate(LocalDate date) {
		return date != null ? date.format(App.DATE_DISPLAY_FORMATTER) : "N/A";
	}
	
	public static void printInternshipDetails(Internship internship) {
		ConsoleBoundary.printSectionTitle("Internship Details", true);
		System.out.println("Internship ID: \t\t" + internship.getInternshipId());
		System.out.println("Title: \t\t\t" + safeValue(internship.getTitle()));
		System.out.println("Description: \t\t" + safeValue(internship.getDescription()));
		System.out.println("Level: \t\t\t" + valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Majors: \t" + safeValue(internship.getPreferredMajor()));
		System.out.println("Application Open Date: \t" + safeValue(internship.getApplicationOpenDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Application Close Date: " + safeValue(internship.getApplicationCloseDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Status: \t\t" + valueOrNA(internship.getInternshipStatus()));
		System.out.println("Number of Slots: \t" + internship.getNumOfSlots());
	}
	
	/**** Company Rep ****/
	public static void printNoInternshipCreated() {
		System.out.println("You have not created any internship yet.");
	}
	
	public static int displaySubMenu(boolean showUpdateOption) {
		while (true) {
			System.out.println();
			System.out.println("Choose option: (or 0 to return)");
			System.out.println("1. View student applications");
			if (showUpdateOption == true) 
			{
				System.out.println("2. Update internship");
				System.out.println("3. Delete internship");
			}
			else
			{
				System.out.println("2. Delete internship");
			}
			String input = App.sc.nextLine().trim();
			try {
				int selectedOption = Integer.parseInt(input);
				if (selectedOption < 0 || selectedOption > 3) {
					ConsoleBoundary.printInvalidSelection();
					continue;
				}
				
				return selectedOption;
			}
			catch (NumberFormatException ex) {
				ConsoleBoundary.printInvalidSelection();
			}
		}
		
	}
	
	public static int displayUpdateInternshipMenu() {
		while (true) {
			System.out.println();
			System.out.println("Choose field to edit: (or 0 to return)");
			System.out.println("1. Title");
			System.out.println("2. Description");
			System.out.println("3. Level");
			System.out.println("4. Preferred Major");
			System.out.println("5. Application Open Date");
			System.out.println("6. Application Close Date");
			System.out.println("7. Number of Slots");
			String input = App.sc.nextLine().trim();
			try {
				int selectedOption = Integer.parseInt(input);
				if (selectedOption < 0 || selectedOption > 7) {
					ConsoleBoundary.printInvalidSelection();
					continue;
				}
				
				return selectedOption;
			}
			catch (NumberFormatException ex) {
				ConsoleBoundary.printInvalidSelection();
			}
		}
	}
	
	public static InternshipLevel promptInternshipLevel() {
		while (true) {
			System.out.println("Choose internship level:");
			System.out.println("1: Basic");
			System.out.println("2: Intermediate");
			System.out.println("3: Advanced");
			String input = App.sc.nextLine().trim();
			switch (input) {
				case "0":
					return null;
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
	
	public static StudentMajor promptPreferredMajor() {
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
	
	public static int promptNumOfSlots(String promptText) {
		while (true) {
			System.out.println(promptText + ":");
			String input = App.sc.nextLine().trim();
			try {
				int slots = Integer.parseInt(input);
				if (slots < 0) {
					ConsoleBoundary.printInvalidInput();
					continue;
				} else if (slots > 10) {
					ConsoleBoundary.printText("You can have maximum of " + CreateInternshipAction.MAX_SLOTS_NUM + " slots.");
					continue;
				}
				
				return slots;
			}
			catch (NumberFormatException ex) {
				ConsoleBoundary.printInvalidInput();
			}
		}
	}
}
