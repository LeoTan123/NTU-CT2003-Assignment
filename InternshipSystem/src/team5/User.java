package team5;

import team5.enums.UserType;

public class User {
	private String userID;
	private String name;
	private String email;
	private String password;
	private Enum<UserType> userType;
	private boolean loginState;
	
	public User() {
		this.userID = "";
		this.name = "";
		this.email = "";
		this.password = "password";
		this.userType = UserType.NONE;
	}
	
	public User(String userID, String name, String email, String password) {
		this.userID = userID;
		this.name = name;
		this.email = email;
		this.password = password;
		this.userType = UserType.NONE;
	}
	
	// Setter and getter
	public void setUserID(String name)
	{
		this.userID = name;
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
	
	public String getPassword()
	{
		return this.password;
	}

	public void setUserType(Enum<UserType> userType)
	{
		this.userType = userType;
	}
	
	public Enum<UserType> getUserType()
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
	public void login() {
		if(this.loginState)
		{
			System.out.println("You have already logged in.");
			return;
		}
		this.loginState = true;
	}
	
	public void logout() {
		if(!this.loginState)
		{
			System.out.println("You have already logged out.");
			return;
		}
		this.loginState = false;
	}
	
	public boolean changePassword() {
		System.out.println("Please enter your old password:");
		String oldPassword = App.sc.nextLine();
		if(!oldPassword.equals(this.password))
		{
			System.out.println("Old password does not match.");
			return false;
		}
		System.out.println("Please enter your new password:");
		String newPassword = App.sc.nextLine();
		if(newPassword.equals(this.password))
		{
			System.out.println("New password and old password is the same.");
			return false;
		}
		System.out.println("Please confirm your new password:");
		String confirmPassword = App.sc.nextLine();
		if(!newPassword.equals(confirmPassword))
		{
			System.out.println("New password and confirmation do not match.");
			return false;
		}
		this.password = newPassword;
		System.out.println("Password has been changed.");
		return true;
	}
}
