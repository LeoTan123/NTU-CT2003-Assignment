package team5.staffactions;

import team5.App;
import team5.CareerCenterStaff;
import team5.CompanyRep;

public class ReviewCompanyRegistrationsAction implements StaffAction {

	@Override
	public void run(CareerCenterStaff staff) {
		System.out.println("===== Pending Company Registrations =====");
		if (App.compRepList.isEmpty()) {
			System.out.println("No company representatives awaiting approval.");
		} else {
			for (int i = 0; i < App.compRepList.size(); i++) {
				CompanyRep rep = App.compRepList.get(i);
				System.out.printf("%d. Rep ID: %s | Name: %s | Company: %s | Status: %s%n",
						i + 1,
						rep.getUserID(),
						rep.getName(),
						safeValue(rep.getCompanyName()),
						rep.getAccountStatus());
			}
		}
		System.out.println("Approval workflow is under construction. Press Enter to return to the staff menu.");
		App.sc.nextLine();
	}

	private String safeValue(String value) {
		return value != null && !value.isEmpty() ? value : "N/A";
	}
}
