package team5.enums;

// Decide by Career Center Staff to approve this internship or not
public enum InternshipStatus {
	PENDING,
	APPROVED,
	REJECTED,
	FILLED;
	
	public static InternshipStatus fromString(String value) {
	    for (InternshipStatus s : InternshipStatus.values()) {
	        if (s.name().equalsIgnoreCase(value.trim())) {
	            return s;
	        }
	    }
	    System.out.println("Warning: Unknown status '" + value + "', defaulting to PENDING");
	    return InternshipStatus.PENDING;
	}
}
