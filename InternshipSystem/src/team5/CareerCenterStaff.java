package team5;

import team5.enums.UserType;

public class CareerCenterStaff extends User {
	private String role;
	private String department;
	
	public CareerCenterStaff() {
		
	}
	
	public CareerCenterStaff(String userID, String name, String email, String password, String role, String department) {
		super(userID, name, email, password);
		this.role = role;
		this.department = department;
		super.setUserType(UserType.CCSTAFF);
	}

	// Setter and getter
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return this.role;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDepartment() {
		return this.department;
	}


	// Behaviors
	@Override
	public String toString()
	{
		return super.getUserID() + " " + super.getName() + " " + super.getEmail() 
		+ " " + super.getPassword() + " " + this.role + " " + this.department;
	}

	public CompanyRep[] viewCompanyRepRegistrations() {
		return new CompanyRep[0];
	}
	
	public Internship[] viewPendingApprovalInternships() {
		return new Internship[0];
	}
}
