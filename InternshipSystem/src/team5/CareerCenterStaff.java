package team5;

public class CareerCenterStaff extends User {
	private String role;
	private String department;
	
	public CareerCenterStaff() {
		
	}
	
	
	public String getRole() {
		return this.role;
	}



	public void setRole(String role) {
		this.role = role;
	}



	public String getDepartment() {
		return department;
	}



	public void setDepartment(String department) {
		this.department = department;
	}



	public CompanyRep[] viewCompanyRepRegistrations() {
		return new CompanyRep[0];
	}
	
	public Internship[] viewPendingApprovalInternships() {
		return new Internship[0];
	}
	
	
}
