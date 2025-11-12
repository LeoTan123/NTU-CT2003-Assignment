package team5;

import team5.boundaries.ConsoleBoundary;
import team5.enums.UserType;

/**
 * User class
 */
public class User {
	private String userID;
	private String name;
	private String email;
	private String password;
	private UserType userType;
	private boolean loginState;
	
	/**
	 * User constructor
	 * @param userID
	 * @param name
	 * @param email
	 * @param password
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
	 * Login method
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
	 * Logout method
	 */
	public void logout() {
		if(!this.loginState)
		{
			System.out.println("You have already logged out.");
			return;
		}
		this.loginState = false;
		if(App.currentUser != null) {
			App.currentUser = null;
		}
		System.out.println("You have already logged out successfully.");
	}
	
	/**
	 * Change password method
	 * Allow 1 retry for each stage
	 * @return true for change successfully, else false
	 */
	public boolean changePassword() {
		ConsoleBoundary.printSectionTitle("Change Password");
		// Old password
	    for (int attempt = 1; attempt <= 2; attempt++) {
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
	    for (int attempt = 1; attempt <= 2; attempt++) {
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
	    for (int attempt = 1; attempt <= 2; attempt++) {
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
