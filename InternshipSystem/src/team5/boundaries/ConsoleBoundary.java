package team5.boundaries;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import team5.App;

/**
 * Abstract base class providing utility methods for console input/output operations.
 * This class contains helper methods for formatting output, parsing user input,
 * displaying prompts, and handling date formatting. All methods are static to allow
 * easy access throughout the application.
 */
public abstract class ConsoleBoundary {

	/** Number of items to display per page in paginated views */
	public static final int PAGE_SIZE = 5;

	/**
	 * Clears the console screen using ANSI escape codes.
	 * This method may not work on all terminal types.
	 */
	public static void clearScreen() {
    	System.out.print("\033[H\033[2J");
        System.out.flush();
    }

	/**
	 * Converts an object value to a string, returning "N/A" for null or empty values.
	 * This method provides safe display formatting for potentially null object values.
	 *
	 * @param value the object to convert to string
	 * @return the string representation of the value, or "N/A" if null or empty
	 */
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

	/**
	 * Converts an enum value to its string name, returning "N/A" for null values.
	 * This method provides safe display formatting for enum values.
	 *
	 * @param value the enum value to convert
	 * @return the name of the enum constant, or "N/A" if null
	 */
	public static String valueOrNA(Enum<?> value) {
		return value != null ? value.name() : "N/A";
	}

	/**
	 * Parses a date string using multiple supported formats.
	 * Attempts to parse the input using various date formats including yyyy-MM-dd,
	 * dd MM yyyy, and d M yyyy (for single-digit days/months).
	 *
	 * @param text the date string to parse
	 * @return the parsed LocalDate object
	 * @throws IllegalArgumentException if the date string cannot be parsed with any supported format
	 */
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

	/**
	 * Prompts the user for input and returns the trimmed input string.
	 * Reads a line from the console and removes leading/trailing whitespace.
	 *
	 * @return the trimmed user input string
	 */
	public static String promptUserInput() {
		return App.sc.nextLine().trim();
	}

	/**
	 * Prompts the user for input with an optional header message.
	 * If withHeader is true, displays a standard prompt message before reading input.
	 *
	 * @param withHeader if true, displays "Enter your choice or 0 to return:" before prompting
	 * @return the trimmed user input string
	 */
	public static String promptUserInput(boolean withHeader) {
		if(withHeader) {
			System.out.println("Enter your choice or 0 to return:");
		}
		return App.sc.nextLine().trim();
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
	
	public static boolean promptConfirmation() {
		while (true) {
			System.out.println("Are you sure you want to perform this action? (y/n)");
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
		System.out.println("===== " + title + " =====");
		
	}

	public static void printSectionTitle(String title, boolean marginTop) {
		if (marginTop) {
			System.out.println();
		}
		printSectionTitle(title);
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
}
