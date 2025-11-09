package team5.boundaries;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import team5.App;

public abstract class ConsoleBoundary {

	public static void clearScreen() {
    	System.out.print("\033[H\033[2J");
        System.out.flush();
    }
	
	public static String safeValue(Object value) {
		if (value == null) {
			return "N/A";
		}
		if (value instanceof String) {
			String str = (String) value;
			return str.isEmpty() ? "N/A" : str;
		}
		return value.toString();
	}

	public static String valueOrNA(Enum<?> value) {
		return value != null ? value.name() : "N/A";
	}
	
	public static LocalDate parseDate(String text) {
	    DateTimeFormatter[] formatters = new DateTimeFormatter[] {
	        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
	        DateTimeFormatter.ofPattern("dd MM yyyy"),
	        DateTimeFormatter.ofPattern("d M yyyy") // handles single-digit days/months
	    };
	    for (DateTimeFormatter f : formatters) {
	        try {
	            return LocalDate.parse(text.trim(), f);
	        } 
	        catch (Exception ignored) 
	        {
	        }
	    }
	    throw new IllegalArgumentException("Unrecognized date format: " + text);
	}
	
	/**** Prompt ****/
	public static String promptUserInput() {
		return App.sc.nextLine();
	}
	
	public static LocalDate promptDate(String prompt) {
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
	
	public static String promptFormInput(String label) {
		while (true) {
			System.out.println(label + ": ");
			String input = App.sc.nextLine().trim();
			if ("0".equals(input)) {
				System.out.println("Cancelled.");
				return null;
			}
			if (!input.isEmpty()) {
				return input;
			}
			System.out.println("Input cannot be empty. Please try again.");
		}
	}
	
	public static boolean promptTryAgain() {
		while (true) {
			System.out.println("Something went wrong. Would you like to try again? (y/n)");
			String input = App.sc.nextLine().trim().toLowerCase();
			
			if (input.equalsIgnoreCase("y")) {
				return true;
			}
			else if (input.equalsIgnoreCase("n")) {
				return false;
			}
			else {
				printInvalidInput();
				continue;
			}
		}
		
	}
	
	/**** Print ****/
	public static void printSectionTitle(String title) {
		App.printSectionTitle(title);
	}

	public static void printSectionTitle(String title, boolean marginTop) {
		App.printSectionTitle(title, marginTop);
	}

	public static void printText(String text) {
		System.out.println(text);
	}
	public static void printChooseOption() {
		System.out.println("Choose option: (or 0 to return)");
	}
	
	public static void printInvalidSelection() {
		System.out.println("Invalid selection. Please try again.");
	}
	
	public static void printInvalidInput() {
		System.out.println("Invalid input. Please try again.");
	}
	
	public static void printErrorMessage() {
		System.out.println("Something went wrong. Please try again later.");
	}
	
	public static void printWIP() {
		System.out.println("WIP");
	}
	
	public static void printSectionTitle(String title) {
		System.out.println("===== " + title + " =====");
	}
	
	public static void printSectionTitle(String title, boolean marginTop) {
		if (marginTop == true)
		{
			System.out.println();
		}
		System.out.println("===== " + title + " =====");
	}
}
