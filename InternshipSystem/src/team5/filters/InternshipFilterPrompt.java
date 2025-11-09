package team5.filters;

import java.time.LocalDate;
import java.util.List;

import team5.App;
import team5.boundaries.ConsoleBoundary;
import team5.enums.InternshipFilterOption;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

public final class InternshipFilterPrompt {

	private InternshipFilterPrompt() {
	}

	public static InternshipFilterCriteria prompt(List<InternshipFilterOption> options) {
		if (options == null || options.isEmpty()) {
			return new InternshipFilterCriteria();
		}

		while (true) {
			System.out.println("Would you like to filter internships?");
			for (int i = 0; i < options.size(); i++) {
				System.out.println((i + 1) + ". " + labelFor(options.get(i)));
			}
			System.out.println((options.size() + 1) + ". Show all internships");
			System.out.println("0. Return to menu");
			System.out.println("Enter your choice:");

			String input = ConsoleBoundary.promptUserInput().trim();
			if ("0".equals(input)) {
				return null;
			}

			int option;
			try {
				option = Integer.parseInt(input);
			} catch (NumberFormatException ex) {
				ConsoleBoundary.printInvalidSelection();
				continue;
			}

			if (option == options.size() + 1) {
				return new InternshipFilterCriteria();
			}
			if (option < 1 || option > options.size()) {
				ConsoleBoundary.printInvalidSelection();
				continue;
			}

			InternshipFilterOption selected = options.get(option - 1);
			InternshipFilterCriteria criteria = new InternshipFilterCriteria();
			switch (selected) {
				case PREFERRED_MAJOR:
					StudentMajor major = promptMajor();
					if (major == null) {
						continue;
					}
					criteria.setPreferredMajor(major);
					return criteria;
				case INTERNSHIP_LEVEL:
					InternshipLevel level = promptLevel();
					if (level == null) {
						continue;
					}
					criteria.setInternshipLevel(level);
					return criteria;
				case INTERNSHIP_STATUS:
					InternshipStatus status = promptStatus();
					if (status == null) {
						continue;
					}
					criteria.setInternshipStatus(status);
					return criteria;
				case APPLICATION_OPEN_FROM:
					LocalDate openDate = promptDate("Enter minimum open date (yyyy-MM-dd)");
					if (openDate == null) {
						continue;
					}
					criteria.setApplicationOpenFrom(openDate);
					return criteria;
				case APPLICATION_CLOSE_TO:
					LocalDate closeDate = promptDate("Enter maximum close date (yyyy-MM-dd)");
					if (closeDate == null) {
						continue;
					}
					criteria.setApplicationCloseTo(closeDate);
					return criteria;
				default:
					throw new IllegalStateException("Unexpected value: " + selected);
			}
		}
	}

	private static String labelFor(InternshipFilterOption option) {
		switch (option) {
			case PREFERRED_MAJOR:
				return "Filter by Preferred Major";
			case INTERNSHIP_LEVEL:
				return "Filter by Internship Level";
			case INTERNSHIP_STATUS:
				return "Filter by Internship Status";
			case APPLICATION_OPEN_FROM:
				return "Filter by Application Open Date";
			case APPLICATION_CLOSE_TO:
				return "Filter by Application Close Date";
			default:
				throw new IllegalStateException("Unexpected value: " + option);
		}
	}

	private static StudentMajor promptMajor() {
		StudentMajor[] majors = StudentMajor.values();
		while (true) {
			System.out.println("Select preferred major:");
			for (int i = 0; i < majors.length; i++) {
				System.out.println((i + 1) + ". " + majors[i].getFullName());
			}
			System.out.println("0. Cancel");
			String input = ConsoleBoundary.promptUserInput().trim();
			if ("0".equals(input)) {
				return null;
			}
			try {
				int index = Integer.parseInt(input) - 1;
				if (index >= 0 && index < majors.length) {
					return majors[index];
				}
			} catch (NumberFormatException ignored) {
			}
			ConsoleBoundary.printInvalidSelection();
		}
	}

	private static InternshipLevel promptLevel() {
		while (true) {
			System.out.println("Select internship level:");
			System.out.println("1. BASIC");
			System.out.println("2. INTERMEDIATE");
			System.out.println("3. ADVANCED");
			System.out.println("0. Cancel");
			String input = ConsoleBoundary.promptUserInput().trim();
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
					ConsoleBoundary.printInvalidSelection();
			}
		}
	}

	private static InternshipStatus promptStatus() {
		while (true) {
			System.out.println("Select internship status:");
			System.out.println("1. PENDING");
			System.out.println("2. APPROVED");
			System.out.println("3. REJECTED");
			System.out.println("0. Cancel");
			String input = ConsoleBoundary.promptUserInput().trim();
			switch (input) {
				case "0":
					return null;
				case "1":
					return InternshipStatus.PENDING;
				case "2":
					return InternshipStatus.APPROVED;
				case "3":
					return InternshipStatus.REJECTED;
				default:
					ConsoleBoundary.printInvalidSelection();
			}
		}
	}

	private static LocalDate promptDate(String label) {
		while (true) {
			System.out.println(label + " or 0 to cancel:");
			String input = ConsoleBoundary.promptUserInput().trim();
			if ("0".equals(input)) {
				return null;
			}
			try {
				return ConsoleBoundary.parseDate(input);
			} catch (IllegalArgumentException ex) {
				ConsoleBoundary.printInvalidSelection();
			}
		}
	}
}
