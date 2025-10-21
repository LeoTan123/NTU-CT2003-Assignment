package team5.staffactions;

import team5.App;
import team5.CareerCenterStaff;

public class ReviewCompanyRegistrationsAction implements StaffAction {

	@Override
	public void run(CareerCenterStaff staff) {
		System.out.println("===== Pending Company Registrations =====");
		System.out.printf("Current placeholder count: %d representative(s).%n", App.compRepList.size());
		App.sc.nextLine();
	}
}
