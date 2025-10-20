package team5;

import team5.enums.UserType;

public class User {
	private String username;
	private String name;
	private String email;
	private String password;
	private Enum<UserType> userType;
	
	public User() {
		
	}
	
	public User(String username, String name, String password) {
		
	}
	
	public void login() {
		
	}
	
	public void logout() {
		
	}
	
	public boolean changePassword(String newPassword) {
		return false;
	}
}