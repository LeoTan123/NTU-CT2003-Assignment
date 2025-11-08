package team5;

import team5.enums.UserType;

/**
 * Career Center Staff Class
 */
public class CareerCenterStaff extends User {
	private String role;
	private String department;
	
	/**
	 * Career Center Staff Constructor
	 * @param userID
	 * @param name
	 * @param email
	 * @param password
	 * @param role
	 * @param department
	 */
	public CareerCenterStaff(String userID, String name, String email, String password, 
			String role, String department) {
		super(userID, name, email, password);
		this.role = role;
		this.department = department;
		super.setUserType(UserType.CCSTAFF);
	}
	
	/**
	 * role setter
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * role getter
	 * @return role
	 */
	public String getRole() {
		return this.role;
	}

	/** department setter
	 * @param department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/** department getter
	 * @return department
	 */
	public String getDepartment() {
		return this.department;
	}

	/**
	 * ToString function
	 */
	@Override
	public String toString()
	{
		return super.getUserID() + " " + super.getName() + " " + super.getEmail() 
		+ " " + super.getPassword() + " " + this.role + " " + this.department;
	}
}
