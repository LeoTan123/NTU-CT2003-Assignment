package team5.enums;

/**
 * Internship level
 */
public enum InternshipLevel {
	NONE,
	BASIC,
	INTERMEDIATE,
	ADVANCED;
	
	public static InternshipLevel fromString(String value) {
	    for (InternshipLevel s : InternshipLevel.values()) {
	        if (s.name().equalsIgnoreCase(value.trim())) {
	            return s;
	        }
	    }
	    System.out.println("Warning: Unknown status '" + value + "', defaulting to NONE");
	    return InternshipLevel.NONE;
	}
}
