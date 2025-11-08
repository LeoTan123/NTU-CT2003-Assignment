package team5;

import team5.enums.UserAccountStatus;

/**
 * Company Representative Registration class
 */
public class CompanyRepRegistration {
	private String companyRepId;
	private String name;
	private String companyName;
	private String department;
	private String position;
	private String email;
	private UserAccountStatus status;
	
	/**
	 * Company Representative constructor
	 * @param companyRepId
	 * @param name
	 * @param companyName
	 * @param department
	 * @param position
	 * @param email
	 * @param status
	 */
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
		return this.companyRepId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCompanyName() {
		return this.companyName;
	}
	
	public String getDepartment() {
		return this.department;
	}
	
	public String getPosition() {
		return this.position;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public UserAccountStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(UserAccountStatus status) {
		this.status = status;
	}
}
