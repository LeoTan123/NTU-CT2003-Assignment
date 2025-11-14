package team5.controllers;

import team5.App;
import team5.CareerCenterStaff;
import team5.CompanyRep;
import team5.Student;
import team5.User;
import team5.boundaries.ConsoleBoundary;
import team5.enums.UserAccountStatus;
import team5.enums.UserType;

/**
 * Abstract base controller for all user types in the system.
 * This class provides common functionality for user authentication and verification,
 * while allowing subclasses to define their own role-specific menu behaviors.
 * Subclasses include StudentController, CompanyRepController, and CareerCenterStaffController.
 */
public abstract class UserController {

    /**
     * Displays the role-specific menu for the user.
     * Each subclass must implement this method to show appropriate menu options
     * and handle user interactions for their specific role.
     */
    public abstract void showMenu();
    
    /**
	 * Verifies user credentials against the stored user lists and performs login.
	 * This method searches through the appropriate user list based on the user type,
	 * matches the userID and password, and performs necessary validation checks
	 * (such as account approval status for company representatives).
	 *
	 * @param userType the type of user attempting to log in (STUDENT, CCSTAFF, or COMREP)
	 * @param userID the unique identifier for the user
	 * @param password the password provided by the user
	 * @return the User object if authentication is successful, null otherwise
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
