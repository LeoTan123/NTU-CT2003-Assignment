package team5.enums;


/**
 * User account type
 */
public enum UserType {
	NONE,
	STUDENT,
	CCSTAFF,
	COMREP;
	
	public static UserType fromString(String value) {
	    for (UserType s : UserType.values()) {
	        if (s.name().equalsIgnoreCase(value.trim())) {
	            return s;
	        }
	    }
	    System.out.println("Warning: Unknown status '" + value + "', defaulting to NONE");
	    return UserType.NONE;
	}
}
