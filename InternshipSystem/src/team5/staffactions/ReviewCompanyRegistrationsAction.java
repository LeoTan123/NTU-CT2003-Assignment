package team5.staffactions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import team5.App;
import team5.CareerCenterStaff;
import team5.CompanyRep;
import team5.enums.UserAccountStatus;

public class ReviewCompanyRegistrationsAction implements StaffAction {

	private static final int PAGE_SIZE = 5;

	@Override
	public void run(CareerCenterStaff staff) {
		App.printSectionTitle("Pending Company Registrations");

		List<CompanyRep> pending = App.compRepList.stream()
				.filter(rep -> rep.getAccountStatus() == UserAccountStatus.PENDING)
				.collect(Collectors.toList());

		if (pending.isEmpty()) {
			System.out.println("No company representative registrations found.");
			waitForReturn();
			return;
		}

		boolean reviewing = true;
		int pageIndex = 0;

		while (reviewing) {
			int start = pageIndex * PAGE_SIZE;
			int end = Math.min(start + PAGE_SIZE, pending.size());

			if (start >= pending.size()) {
				System.out.println("No more records to display.");
				pageIndex = 0;
				continue;
			}

			System.out.printf("Showing registrations %d to %d of %d%n",
					start + 1, end, pending.size());

			for (int i = start; i < end; i++) {
				CompanyRep reg = pending.get(i);
				System.out.printf("%d. Rep ID: %s | Name: %s | Company: %s | Status: %s%n",
						i + 1,
						reg.getUserID(),
						reg.getName(),
						reg.getCompanyName(),
						reg.getAccountStatus());
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
						System.out.println("Invalid selection. Please try again.");
						continue;
					}

					CompanyRep chosen = pending.get(selection - 1);
					handleReview(chosen);
					writeCompanyRepsToCsv();

					pending = App.compRepList.stream()
							.filter(rep -> rep.getAccountStatus() == UserAccountStatus.PENDING)
							.collect(Collectors.toList());

					if (pending.isEmpty()) {
						System.out.println("No pending registrations remaining.");
						reviewing = false;
					} else {
						pageIndex = 0;
					}
				} catch (NumberFormatException ex) {
					System.out.println("Please enter a valid number, 'n', 'p', or 0.");
				}
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
			System.out.println("Enter a number to review a registration, or 0 to return:");
		}
	}

	private void handleReview(CompanyRep registration) {
		System.out.println("===== Review Registration =====");
		System.out.println("Representative ID: " + registration.getUserID());
		System.out.println("Name: " + registration.getName());
		System.out.println("Company: " + registration.getCompanyName());
		System.out.println("Department: " + registration.getDepartment());
		System.out.println("Position: " + registration.getPosition());
		System.out.println("Email: " + registration.getEmail());
		System.out.println("Status: " + registration.getAccountStatus());

		boolean deciding = true;
		while (deciding) {
			System.out.println("Choose an action:");
			System.out.println("1. Approve");
			System.out.println("2. Reject");
			System.out.println("0. Back to list");

			String decision = App.sc.nextLine().trim();
			switch (decision) {
				case "1":
					registration.setAccountStatus(UserAccountStatus.APPROVED);
					System.out.println("Registration approved.");
					deciding = false;
					break;
				case "2":
					registration.setAccountStatus(UserAccountStatus.REJECTED);
					System.out.println("Registration rejected.");
					deciding = false;
					break;
				case "0":
					deciding = false;
					break;
				default:
					System.out.println("Invalid option. Please choose 1, 2, or 0.");
			}
		}
	}

	private void writeCompanyRepsToCsv() {
		try (FileWriter writer = new FileWriter(App.envFilePathRep)) {
			writer.append("CompanyRepID,Name,CompanyName,Department,Position,Email,Status\n");
			for (CompanyRep rep : App.compRepList) {
				writer.append(rep.getUserID()).append(",")
						.append(rep.getName()).append(",")
						.append(rep.getCompanyName()).append(",")
						.append(rep.getDepartment()).append(",")
						.append(rep.getPosition()).append(",")
						.append(rep.getEmail()).append(",")
						.append(rep.getAccountStatus().name()).append("\n");
			}
			writer.flush();
		} catch (IOException e) {
			System.out.println("Failed to update company representative file: " + e.getMessage());
		}
	}

	private void waitForReturn() {
		while (true) {
			System.out.println("Enter 0 to return to the staff menu:");
			if ("0".equals(App.sc.nextLine().trim())) {
				return;
			}
			System.out.println("No pending registration. Please enter 0 to go back.");
		}
	}
}
