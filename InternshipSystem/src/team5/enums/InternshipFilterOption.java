package team5.enums;

/**
 * List the available filtering options for internship listings.
 */
public enum InternshipFilterOption {
	PREFERRED_MAJOR,
	INTERNSHIP_LEVEL,
	INTERNSHIP_STATUS,
	APPLICATION_OPEN_FROM,
	APPLICATION_CLOSE_TO;
	
	public static InternshipFilterOption fromString(String value) {
	    for (InternshipFilterOption s : InternshipFilterOption.values()) {
	        if (s.name().equalsIgnoreCase(value.trim())) {
	            return s;
	        }
	    }
	    System.out.println("Warning: Unknown status '" + value + "', defaulting to PREFERRED_MAJOR");
	    return InternshipFilterOption.PREFERRED_MAJOR;
	}
}
