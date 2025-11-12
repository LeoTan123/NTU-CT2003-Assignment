package team5.controllers;

import team5.App;
import team5.CareerCenterStaff;
import team5.CompanyRep;
import team5.Student;
import team5.User;
import team5.boundaries.ConsoleBoundary;
import team5.enums.UserAccountStatus;
import team5.enums.UserType;

public abstract class UserController {

    // Each subclass will define its own menu behavior
    public abstract void showMenu();
    
    /**
	 * To verify user type using userID and password
	 * @param userType: User type from user input
	 * @param userID: User ID
	 * @param password: User password
	 * @return true for found user and login successfully, else false
	 */
	public static User verifyUserFromList(UserType userType, String userID, String password)
	{ 
		User foundUser = null;
		switch(userType)
		{
			case STUDENT:
				for (Student student : App.studentList) {
	                if (student.getUserID().equals(userID)) {
	                    foundUser = student;
	                    break;
	                }
	            }
				break;
			case CCSTAFF:
				for (CareerCenterStaff staff : App.staffList) {
	                if (staff.getUserID().equals(userID)) {
	                    foundUser = staff;
	                    break;
	                }
	            }
				break;
			case COMREP:
				for (CompanyRep rep : App.compRepList) {
	                if (rep.getUserID().equalsIgnoreCase(userID)){
	                    foundUser = rep;
	                    break;
	                }
	            }
				break;
			default:
				System.out.println("Invalid User Type: " + userType);
	            return null;	
		}
		
		// User not found in either lists
		if (foundUser == null) {
			System.out.println("User ID not found in system: " + userID);
	        return null;
	    }
		
		// Special check for COMREP account status
	    if (userType == UserType.COMREP) {
	    	CompanyRep rep = (CompanyRep)foundUser;
	    	UserAccountStatus status = rep.getAccountStatus();
	    	if (status == UserAccountStatus.PENDING) {
	    		System.out.println("Your account is pending approval. Please wait for the career center staff to review.");
	    		return null;
	    	}
	    	else if (status == UserAccountStatus.REJECTED) {
	    		System.out.println("Your registration was rejected. Please submit a new registration request for review.");
	    		return null;
	    	}
	    }
	    
	    // Check password
	    int maxAttempts = 3;
	    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
	        if (foundUser.getPassword().equals(password)) {
	            User currentUser = foundUser;
	            currentUser.setUserType(userType);
	            return currentUser;
	        } 
	        else {
	            if (attempt < maxAttempts) {
	                System.out.println("Password wrong, please enter again:");
	                password = ConsoleBoundary.promptUserInput(); // update password for next attempt
	            } 
	            else {
	                System.out.println("You have entered wrong password for 3 times. Login failed.");
	            }
	        }
	    }
	    // Failed after 3 attempts
	    return null;
	}
}
