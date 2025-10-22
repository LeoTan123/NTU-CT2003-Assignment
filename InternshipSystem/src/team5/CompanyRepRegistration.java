package team5;

import team5.enums.UserAccountStatus;

public class CompanyRepRegistration {
	private String companyRepId;
	private String name;
	private String companyName;
	private String department;
	private String position;
	private String email;
	private UserAccountStatus status;
	
	public CompanyRepRegistration(String companyRepId, String name, String companyName, String department,
			String position, String email, UserAccountStatus status) {
		this.companyRepId = companyRepId;
		this.name = name;
		this.companyName = companyName;
		this.department = department;
		this.position = position;
		this.email = email;
		this.status = status;
	}
	
	public String getCompanyRepId() {
		return companyRepId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public String getPosition() {
		return position;
	}
	
	public String getEmail() {
		return email;
	}
	
	public UserAccountStatus getStatus() {
		return status;
	}
	
	public void setStatus(UserAccountStatus status) {
		this.status = status;
	}
}
