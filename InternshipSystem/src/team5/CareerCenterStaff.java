package team5;

import team5.enums.UserType;

/**
 * Represents a career center staff member in the internship placement management system.
 * This class extends User and includes staff-specific attributes such as role and department.
 * Career Center Staff are responsible for approving company representative registrations
 * and reviewing internship submissions before they become visible to students.
 */
public class CareerCenterStaff extends User {
	private String role;
	private String department;
	
	/**
	 * Constructs a new CareerCenterStaff with the specified details.
	 * The user type is automatically set to CCSTAFF.
	 *
	 * @param userID the unique identifier for the staff member (NTU account)
	 * @param name the full name of the staff member
	 * @param email the email address of the staff member
	 * @param password the password for the staff account
	 * @param role the role of the staff member in the career center
	 * @param department the department where the staff member works
	 */
	public CareerCenterStaff(String userID, String name, String email, String password,
			String role, String department) {
		super(userID, name, email, password);
		this.role = role;
		this.department = department;
		super.setUserType(UserType.CCSTAFF);
	}
	
	/**
	 * Sets the role of the career center staff member.
	 *
	 * @param role the new role designation
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Gets the role of the career center staff member.
	 *
	 * @return the role designation
	 */
	public String getRole() {
		return this.role;
	}

	/**
	 * Sets the department of the career center staff member.
	 *
	 * @param department the new department name
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * Gets the department of the career center staff member.
	 *
	 * @return the department name
	 */
	public String getDepartment() {
		return this.department;
	}

	/**
	 * Returns a string representation of the career center staff member.
	 *
	 * @return a string containing the staff member's details
	 */
	@Override
	public String toString()
	{
		return super.getUserID() + " " + super.getName() + " " + super.getEmail() 
		+ " " + super.getPassword() + " " + this.role + " " + this.department;
	}
}
