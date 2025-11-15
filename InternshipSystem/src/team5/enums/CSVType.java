package team5.enums;

/**
 * Enum for the type of data the files represent
 */
public enum CSVType {
	Student,
	CCStaff,
	CompanyRep,
	Internship,
	InternshipApplication;
	
	public static CSVType fromString(String value) {
	    for (CSVType s : CSVType.values()) {
	        if (s.name().equalsIgnoreCase(value.trim())) {
	            return s;
	        }
	    }
	    System.out.println("Warning: Unknown status '" + value + "', defaulting to Student");
	    return CSVType.Student;
	}
}
