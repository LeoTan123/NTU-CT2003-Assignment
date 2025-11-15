package team5.enums;

/**
 * Student majors
 */
public enum StudentMajor {
	CS("Computer Science"),
	DSAI("Data Science & AI"),
	CE("Computer Engineering"),
	IEM("Information Engineering & Media"),
	COMP("Computing");
	
	private final String fullName;

    StudentMajor(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
    // Usage:
    // System.out.println(major);               // CS
    // System.out.println(major.getFullName()); // Computer Science
    
    public static StudentMajor fromFullName(String fullName) {
    	if (fullName == null) {
    		return StudentMajor.COMP;
    	}

    	String trimmed = fullName.trim();

        for (StudentMajor major : StudentMajor.values()) {
        	if (major.name().equalsIgnoreCase(trimmed) || major.getFullName().equalsIgnoreCase(trimmed)) {
                return major;
            }
        }
        System.out.println("Warning: Unknown major '" + fullName + "', defaulting to COMP");
        return StudentMajor.COMP;
    }
}
