package team5.staffactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import team5.App;
import team5.CareerCenterStaff;
import team5.Internship;
import team5.boundaries.ConsoleBoundary;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

public class ViewInternshipsAction implements StaffAction {

	private static final int PAGE_SIZE = 5;

	@Override
	public void run(CareerCenterStaff staff) {
		if (App.internshipList.isEmpty()) {
			ConsoleBoundary.printSectionTitle("All Internship Details");
			System.out.println("No internships available at the moment.");
			return;
		}

		List<Internship> baseList = new ArrayList<>(App.internshipList);
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
					workingList = new ArrayList<>(baseList);
					pageIndex = 0;
					continue;
				}
				System.out.println("Invalid option.");
				continue;
			}

			ConsoleBoundary.printSectionTitle("All Internship Details");
			int start = pageIndex * PAGE_SIZE;
			int end = Math.min(start + PAGE_SIZE, workingList.size());

			for (int i = start; i < end; i++) {
				Internship internship = workingList.get(i);
				System.out.printf("%d. Internship ID: %s | Title: %s | Level: %s | Status: %s%n",
						i + 1,
						internship.getInternshipId(),
						safeValue(internship.getTitle()),
						valueOrNA(internship.getInternshipLevel()),
						valueOrNA(internship.getInternshipStatus()));
			}

			boolean hasNext = end < workingList.size();
			boolean hasPrev = pageIndex > 0;
			printNavigationPrompt(hasPrev, hasNext);

			String input = App.sc.nextLine().trim();
			if ("0".equals(input)) {
				return;
			} else if ("f".equalsIgnoreCase(input)) {
				List<Internship> filtered = filterInternships(baseList);
				if (filtered == null) {
					return;
				}
				workingList = filtered;
				pageIndex = 0;
			} else if ("a".equalsIgnoreCase(input)) {
				workingList = new ArrayList<>(baseList);
				pageIndex = 0;
			} else if (hasNext && "n".equalsIgnoreCase(input)) {
				pageIndex++;
			} else if (hasPrev && "p".equalsIgnoreCase(input)) {
				pageIndex = Math.max(pageIndex - 1, 0);
			} else {
				try {
					int selection = Integer.parseInt(input);
					if (selection < 1 || selection > workingList.size()) {
						System.out.println("Invalid selection. Please try again.");
						continue;
					}
					Internship chosen = workingList.get(selection - 1);
					displayInternshipDetails(chosen);
				} catch (NumberFormatException ex) {
					System.out.println("Please enter a valid number, 'n', 'p', 'f', 'a', or 0.");
				}
			}
		}
	}

	private void displayInternshipDetails(Internship internship) {
		ConsoleBoundary.printSectionTitle("Internship Details");
		System.out.println("Internship ID: " + internship.getInternshipId());
		System.out.println("Title: " + safeValue(internship.getTitle()));
		System.out.println("Description: " + safeValue(internship.getDescription()));
		System.out.println("Level: " + valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Major: " + safeValue(internship.getPreferredMajor()));
		System.out.println("Application Open Date: " + safeValue(internship.getApplicationOpenDate()));
		System.out.println("Application Close Date: " + safeValue(internship.getApplicationCloseDate()));
		System.out.println("Status: " + valueOrNA(internship.getInternshipStatus()));
		System.out.println("Number of Slots: " + internship.getNumOfSlots());
		System.out.println("Press Enter to return to the internship list.");
		App.sc.nextLine();
	}

	// Code leverage from Wei Jie filter
	private List<Internship> filterInternships(List<Internship> internships) {
		List<Internship> filteredList = new ArrayList<>(internships);
		boolean validInput = false;

		while (!validInput) {
			System.out.println("Would you like to filter internships?");
			System.out.println("1. Filter by Preferred Major");
			System.out.println("2. Filter by Internship Level");
			System.out.println("3. Filter by Internship Status");
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
					StudentMajor selectedMajor = promptMajor();
					if (selectedMajor != null) {
						filteredList = filteredList.stream()
								.filter(i -> i.getPreferredMajor() != null && i.getPreferredMajor() == selectedMajor)
								.toList();
					}
					break;
				case "2":
					validInput = true;
					InternshipLevel selectedLevel = promptLevel();
					if (selectedLevel != null) {
						filteredList = filteredList.stream()
								.filter(i -> i.getInternshipLevel() != null && i.getInternshipLevel() == selectedLevel)
								.toList();
					}
					break;
				case "3":
					validInput = true;
					InternshipStatus selectedStatus = promptStatus();
					if (selectedStatus != null) {
						filteredList = filteredList.stream()
								.filter(i -> i.getInternshipStatus() != null
										&& i.getInternshipStatus() == selectedStatus)
								.toList();
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
					} catch (Exception e) {
						System.out.println("Invalid date format. Showing all internships.");
					}
					break;
				case "5":
					validInput = true;
					filteredList = new ArrayList<>(internships);
					break;
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}

		if (filteredList.isEmpty()) {
			System.out.println("No internships match your filter.");
		}
		return filteredList;
	}

	private StudentMajor promptMajor() {
		while (true) {
			System.out.println("Select preferred major:");
			System.out.println("1. Computer Science");
			System.out.println("2. Data Science & AI");
			System.out.println("3. Computer Engineering");
			System.out.println("4. Information Engineering & Media");
			System.out.println("5. Computing");
			System.out.println("Enter your choice: ");
			String choice = App.sc.nextLine().trim();
			switch (choice) {
				case "1":
					return StudentMajor.CS;
				case "2":
					return StudentMajor.DSAI;
				case "3":
					return StudentMajor.CE;
				case "4":
					return StudentMajor.IEM;
				case "5":
					return StudentMajor.COMP;
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private InternshipLevel promptLevel() {
		while (true) {
			System.out.println("Select internship level:");
			System.out.println("1. BASIC");
			System.out.println("2. INTERMEDIATE");
			System.out.println("3. ADVANCED");
			System.out.println("Enter your choice: ");
			String choice = App.sc.nextLine().trim();
			switch (choice) {
				case "1":
					return InternshipLevel.BASIC;
				case "2":
					return InternshipLevel.INTERMEDIATE;
				case "3":
					return InternshipLevel.ADVANCED;
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private InternshipStatus promptStatus() {
		while (true) {
			System.out.println("Select internship status:");
			System.out.println("1. PENDING");
			System.out.println("2. APPROVED");
			System.out.println("3. REJECTED");
			System.out.println("Enter your choice: ");
			String choice = App.sc.nextLine().trim();
			switch (choice) {
				case "1":
					return InternshipStatus.PENDING;
				case "2":
					return InternshipStatus.APPROVED;
				case "3":
					return InternshipStatus.REJECTED;
				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void printNavigationPrompt(boolean hasPrev, boolean hasNext) {
		System.out.print("Enter a number to view details");
		if (hasPrev) {
			System.out.print(", 'p' for previous page");
		}
		if (hasNext) {
			System.out.print(", 'n' for next page");
		}
		System.out.println(", 'f' to filter, 'a' to show all, or 0 to return:");
	}

	private String safeValue(Object value) {
		if (value == null) {
			return "N/A";
		}
		if (value instanceof String) {
			String str = (String) value;
			return str.isEmpty() ? "N/A" : str;
		}
		if (value instanceof StudentMajor) {
			return ((StudentMajor) value).getFullName();
		}
		if (value instanceof LocalDate) {
			return ((LocalDate) value).format(App.DATE_DISPLAY_FORMATTER);
		}
		return value.toString();
	}

	private String valueOrNA(Enum<?> value) {
		if (value == null) {
			return "N/A";
		}
		if (value instanceof StudentMajor) {
			return ((StudentMajor) value).getFullName();
		}
		return value.name();
	}
}
