package team5;

import team5.boundaries.ConsoleBoundary;
import team5.enums.UserType;

/**
 * Represents a base user in the internship placement management system.
 * This class serves as the parent class for all user types including Student,
 * CompanyRep, and CareerCenterStaff. It provides common user attributes and
 * functionalities such as login, logout, and password management.
 */
public class User {
	/** Maximum number of password change attempts allowed per stage */
	public static final int MAX_PASSWORD_ATTEMPTS = 2;

	private String userID;
	private String name;
	private String email;
	private String password;
	private UserType userType;
	private boolean loginState;
	
	/**
	 * Constructs a new User with the specified details.
	 * Initializes the user with default values for userType (NONE) and loginState (false).
	 *
	 * @param userID the unique identifier for the user
	 * @param name the full name of the user
	 * @param email the email address of the user
	 * @param password the password for the user account
	 */
	public User(String userID, String name, String email, String password) {
		this.userID = userID;
		this.name = name;
		this.email = email;
		this.password = password;
		this.userType = UserType.NONE;
		this.loginState = false;
	}
	
	public void setUserID(String userID)
	{
		this.userID = userID;
	}
	
	public String getUserID()
	{
		return this.userID;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public void setPassword(String newPassword) 
	{
		this.password = newPassword;
	}
	
	public String getPassword()
	{
		return this.password;
	}

	public void setUserType(UserType userType)
	{
		this.userType = userType;
	}
	
	public UserType getUserType()
	{
		return this.userType;
	}
	
	public void setLoginState(boolean loginState)
	{
		this.loginState = loginState;
	}
	
	public boolean getLoginState()
	{
		return this.loginState;
	}
	
	// Behaviors
	@Override
	public String toString() {
	    return userID + " " + name + " " + email + " " + userType + " " + loginState;
	}
	
	/**
	 * Logs the user into the system by setting the login state to true.
	 * If the user is already logged in, displays an appropriate message.
	 */
	public void login() {
		if(this.loginState)
		{
			System.out.println("You have already logged in.");
			return;
		}
		this.loginState = true;
	}
	
	/**
	 * Logs the user out of the system by setting the login state to false.
	 * If the user is already logged out, displays an appropriate message.
	 */
	public void logout() {
		if(!this.loginState)
		{
			System.out.println("You have already logged out.");
			return;
		}
		this.loginState = false;
		System.out.println("You have logged out successfully.");
	}
	
	/**
	 * Allows the user to change their password through a three-stage verification process.
	 * The process includes verifying the old password, entering a new password (which must
	 * differ from the old one), and confirming the new password. Each stage allows up to
	 * two attempts before returning to the main menu.
	 *
	 * @return true if the password change is successful, false if the user exits or
	 *         exceeds the maximum number of attempts at any stage
	 */
	public boolean changePassword() {
		ConsoleBoundary.printSectionTitle("Change Password");
		// Old password
	    for (int attempt = 1; attempt <= MAX_PASSWORD_ATTEMPTS; attempt++) {
	        System.out.println("Please enter your old password (or 0 to exit):");
	        String oldPassword = ConsoleBoundary.promptUserInput();

	        if (oldPassword == null || oldPassword.isEmpty()) {
	            System.out.println("Password cannot be empty.");
	            continue;
	        }
	        if (oldPassword.equals("0")) {
	            return false;
	        }
	        if (!oldPassword.equals(this.password)) {
	            if (attempt == 1) {
	                System.out.println("Old password does not match. Please try again.");
	            }
	            else {
	                System.out.println("Too many failed attempts. Back to menu.");
	                return false;
	            }
	        } 
	        else {
	        	// Correct old password break the loop
	            break;
	        }
	    }

	    // New password
	    String newPassword = null;
	    for (int attempt = 1; attempt <= MAX_PASSWORD_ATTEMPTS; attempt++) {
	        System.out.println("Please enter your new password:");
	        newPassword = ConsoleBoundary.promptUserInput();

	        if (newPassword == null || newPassword.isEmpty()) {
	            System.out.println("Password cannot be empty.");
	            continue;
	        }
	        if (newPassword.equals(this.password)) {
	            if (attempt == 1) {
	                System.out.println("New password must be different from the old one. Please try again.");
	            }
	            else {
	                System.out.println("Too many failed attempts. Back to menu.");
	                return false;
	            }
	        } 
	        else {
	        	// Valid new password break the loop
	            break;
	        }
	    }

	    // Confirm new password
	    for (int attempt = 1; attempt <= MAX_PASSWORD_ATTEMPTS; attempt++) {
	        System.out.println("Please confirm your new password:");
	        String confirmPassword = ConsoleBoundary.promptUserInput();

	        if (confirmPassword == null || confirmPassword.isEmpty()) {
	            System.out.println("Password cannot be empty.");
	            continue;
	        }
	        if (!newPassword.equals(confirmPassword)) {
	            if (attempt == 1) {
	                System.out.println("New password and confirmation do not match. Please try again.");
	            }
	            else {
	                System.out.println("Too many failed attempts. Back to menu.");
	                return false;
	            }
	        } 
	        else {
	        	// Confirmation matches new password break the loop
	            break;
	        }
	    }

	    // Set new password 
	    this.password = newPassword;
	    System.out.println("Your password has been successfully changed.");
	    return true;
	}
}
