package team5.staffactions;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import team5.App;
import team5.CareerCenterStaff;
import team5.CompanyRep;
import team5.Internship;
import team5.boundaries.ConsoleBoundary;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

public class ReviewInternshipSubmissionsAction implements StaffAction {

	private static final int PAGE_SIZE = 5;

	@Override
	public void run(CareerCenterStaff staff) {
		ConsoleBoundary.printSectionTitle("Pending Internship Submissions");

		List<Internship> pending = App.internshipList.stream()
				.filter(internship -> internship.getInternshipStatus() == InternshipStatus.PENDING)
				.collect(Collectors.toList());

		if (pending.isEmpty()) {
			System.out.println("No internship submissions found.");
			waitForReturn();
			return;
		}

		int pageIndex = 0;
		boolean reviewing = true;

		while (reviewing) {
			int start = pageIndex * PAGE_SIZE;
			int end = Math.min(start + PAGE_SIZE, pending.size());

			if (start >= pending.size()) {
				System.out.println("No more records to display.");
				pageIndex = 0;
				continue;
			}

			System.out.printf("Showing submissions %d to %d of %d%n",
					start + 1, end, pending.size());

			for (int i = start; i < end; i++) {
				Internship internship = pending.get(i);
				System.out.printf("%d. Internship ID: %s | Title: %s | Level: %s | Status: %s%n",
						i + 1,
						ConsoleBoundary.safeValue(internship.getInternshipId()),
						ConsoleBoundary.safeValue(internship.getTitle()),
						ConsoleBoundary.valueOrNA(internship.getInternshipLevel()),
						ConsoleBoundary.valueOrNA(internship.getInternshipStatus()));
			}

			boolean hasNext = end < pending.size();
			boolean hasPrev = pageIndex > 0;
			printNavigationPrompt(hasPrev, hasNext);

			String input = App.sc.nextLine().trim();
			if ("0".equals(input)) {
				reviewing = false;
			} else if (hasNext && "n".equalsIgnoreCase(input)) {
				pageIndex++;
			} else if (hasPrev && "p".equalsIgnoreCase(input)) {
				pageIndex = Math.max(pageIndex - 1, 0);
			} else {
				try {
					int selection = Integer.parseInt(input);
					if (selection < 1 || selection > pending.size()) {
						ConsoleBoundary.printInvalidSelection();
						continue;
					}

					Internship chosen = pending.get(selection - 1);
					handleReview(chosen);
					writeInternshipsToCsv();

					pending = App.internshipList.stream()
							.filter(internship -> internship.getInternshipStatus() == InternshipStatus.PENDING)
							.collect(Collectors.toList());

					if (pending.isEmpty()) {
						System.out.println("No pending submissions remaining.");
						reviewing = false;
					} else {
						pageIndex = 0;
					}
				} catch (NumberFormatException ex) {
					ConsoleBoundary.printInvalidInput();
				}
			}
		}
	}

	private void handleReview(Internship internship) {
		ConsoleBoundary.printSectionTitle("Review Internship Submission", true);
		System.out.println("Internship ID: " +  ConsoleBoundary.safeValue(internship.getInternshipId()));
		System.out.println("Title: " +  ConsoleBoundary.safeValue(internship.getTitle()));
		System.out.println("Description: " +  ConsoleBoundary.safeValue(internship.getDescription()));
		System.out.println("Level: " +  ConsoleBoundary.valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Major: " +  ConsoleBoundary.safeValue(internship.getPreferredMajor()));
		System.out.println("Application Open Date: " + ConsoleBoundary.safeValue(internship.getApplicationOpenDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Application Close Date: " + ConsoleBoundary.safeValue(internship.getApplicationCloseDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Status: " +  ConsoleBoundary.valueOrNA(internship.getInternshipStatus()));
		System.out.println("Number of Slots: " + internship.getNumOfSlots());
		System.out.println("Company Rep: " +  ConsoleBoundary.safeValue(internship.getCompanyRep()));
		System.out.println("Company Name: " + ConsoleBoundary.safeValue(resolveCompanyName(internship)));

		boolean deciding = true;
		while (deciding) {
			System.out.println("Choose an action:");
			System.out.println("1. Approve internship");
			System.out.println("2. Reject internship");
			System.out.println("0. Back to list");

			String input = App.sc.nextLine().trim();
			switch (input) {
				case "1":
					internship.setInternshipStatus(InternshipStatus.APPROVED);
					System.out.println("Internship approved.");
					deciding = false;
					break;
				case "2":
					internship.setInternshipStatus(InternshipStatus.REJECTED);
					System.out.println("Internship rejected.");
					deciding = false;
					break;
				case "0":
					deciding = false;
					break;
				default:
					ConsoleBoundary.printInvalidSelection();
			}
		}
	}

	private void printNavigationPrompt(boolean hasPrev, boolean hasNext) {
		if (hasPrev || hasNext) {
			System.out.print("Enter ");
			if (hasPrev) {
				System.out.print("'p' for previous page");
				if (hasNext) {
					System.out.print(", ");
				}
			}
			if (hasNext) {
				System.out.print("'n' for next page");
			}
			System.out.println(", a number to review, or 0 to return:");
		} else {
			System.out.println("Enter a number to review a submission, or 0 to return:");
		}
	}

	private void waitForReturn() {
		while (true) {
			System.out.println("Enter 0 to return to the staff menu:");
			if ("0".equals(App.sc.nextLine().trim())) {
				return;
			}
			System.out.println("No pending submission. Please enter 0 to go back.");
		}
	}

	private void writeInternshipsToCsv() {
		try (FileWriter writer = new FileWriter(App.envFilePathInternship)) {
			writer.append("InternshipID,Title,Description,Level,PreferredMajor,ApplicationOpenDate,ApplicationCloseDate,Status,CompanyName,CompanyRep,NumSlots\n");
			for (Internship internship : App.internshipList) {
				writer.append(csvValue(internship.getInternshipId())).append(",")
						.append(csvValue(internship.getTitle())).append(",")
						.append(csvValue(internship.getDescription())).append(",")
						.append(csvValue(ConsoleBoundary.valueOrNA(internship.getInternshipLevel()))).append(",")
						.append(csvValue(ConsoleBoundary.valueOrNA(internship.getPreferredMajor()))).append(",")
						.append(csvValue(internship.getApplicationOpenDate().format(App.DATE_DB_FORMATTER))).append(",")
						.append(csvValue(internship.getApplicationCloseDate().format(App.DATE_DB_FORMATTER))).append(",")
						.append(csvValue(ConsoleBoundary.valueOrNA(internship.getInternshipStatus()))).append(",")
						.append(csvValue(resolveCompanyName(internship))).append(",")
						.append(csvValue(internship.getCompanyRep())).append(",")
						.append(String.valueOf(internship.getNumOfSlots())).append("\n");
			}
			writer.flush();
		} 
		catch (IOException e) {
			System.out.println("Failed to update internship file: " + e.getMessage());
		}
	}

	private String csvValue(Object value) {
		return value == null ? "" : value.toString();
	}

	private String resolveCompanyName(Internship internship) {
		return App.compRepList.stream()
				.filter(rep -> rep.getUserID().equalsIgnoreCase(internship.getCompanyRep())
						|| rep.getEmail().equalsIgnoreCase(internship.getCompanyRep()))
				.map(CompanyRep::getCompanyName)
				.findFirst()
				.orElse("");
	}
}
