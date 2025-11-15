package team5.enums;

/**
 * Decide by company representative to approve this application or not
 */
public enum InternshipApplicationStatus {
	PENDING,
	SUCCESSFUL,
	UNSUCCESSFUL,
	ACCEPTED;
	
	public static InternshipApplicationStatus fromString(String value) {
	    for (InternshipApplicationStatus s : InternshipApplicationStatus.values()) {
	        if (s.name().equalsIgnoreCase(value.trim())) {
	            return s;
	        }
	    }
	    System.out.println("Warning: Unknown status '" + value + "', defaulting to PENDING");
	    return InternshipApplicationStatus.PENDING;
	}
}


