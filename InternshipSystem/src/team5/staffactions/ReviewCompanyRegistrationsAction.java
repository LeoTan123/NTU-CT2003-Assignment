package team5.staffactions;

import team5.App;
import team5.CareerCenterStaff;
import team5.CompanyRep;
import team5.enums.UserAccountStatus;

public class ReviewCompanyRegistrationsAction implements StaffAction {

	@Override
	public void run(CareerCenterStaff staff) {
		App.printSectionTitle("Pending Company Registrations");
		if (App.compRepList.isEmpty()) {
			System.out.println("No company representative registrations found.");
			return;
		} 
		else {
			for (int i = 0; i < App.compRepList.size(); i++) {
				CompanyRep reg = App.compRepList.get(i);
				if(reg.getAccountStatus() != UserAccountStatus.PENDING)
					continue;
				System.out.printf("%d. Rep ID: %s | Name: %s | Company: %s | Status: %s%n",
						i + 1,
						reg.getUserID(),
						reg.getName(),
						reg.getCompanyName(),
						reg.getAccountStatus());
			}
		}
		boolean awaiting = true;
		while (awaiting) {
			System.out.println("Enter 0 to return to the staff menu:");
			String input = App.sc.nextLine();
			if ("0".equals(input)) {
				awaiting = false;
			} else {
				System.out.println("TBD.");
			}
		}
	}
}
