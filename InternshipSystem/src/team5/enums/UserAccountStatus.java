package team5.enums;


/**
 * Decide by career center staff to approve this company rep or not
 */
public enum UserAccountStatus {
	PENDING,
	APPROVED,
	REJECTED;
	
	public static UserAccountStatus fromString(String value) {
	    for (UserAccountStatus s : UserAccountStatus.values()) {
	        if (s.name().equalsIgnoreCase(value.trim())) {
	            return s;
	        }
	    }
	    System.out.println("Warning: Unknown status '" + value + "', defaulting to PENDING");
	    return UserAccountStatus.PENDING;
	}
}
