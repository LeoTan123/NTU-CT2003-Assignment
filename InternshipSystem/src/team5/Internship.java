package team5;

import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipLevel;

public class Internship {
	private int internshipId;
	private String title;
	private String description;
	private Enum<InternshipLevel> internshipLevel;
	private String preferredMajors;
	private String applicationOpenDate;
	private String applicationCloseDate;
	private Enum<InternshipApplicationStatus> internshipStatus;
	private int numOfSlots;
	
	public Internship() {
		
	}

	public int getInternshipId() {
		return internshipId;
	}
// uncomment if needed
//	public void setInternshipId(int internshipId) {
//		this.internshipId = internshipId;
//	}

	public String getTitle() {
		return title;
	}
	// uncomment if needed
//	public void setTitle(String title) {
//		this.title = title;
//	}

	public String getDescription() {
		return description;
	}
	// uncomment if needed
//	public void setDescription(String description) {
//		this.description = description;
//	}

	public Enum<InternshipLevel> getInternshipLevel() {
		return internshipLevel;
	}
// uncomment if needed
//	public void setInternshipLevel(Enum<InternshipLevel> internshipLevel) {
//		this.internshipLevel = internshipLevel;
//	}

	public String getPreferredMajors() {
		return preferredMajors;
	}
// uncomment if needed
//	public void setPreferredMajors(String preferredMajors) {
//		this.preferredMajors = preferredMajors;
//	}

	public String getApplicationOpenDate() {
		return applicationOpenDate;
	}
// uncomment if needed
//	public void setApplicationOpenDate(String applicationOpenDate) {
//		this.applicationOpenDate = applicationOpenDate;
//	}

	public String getApplicationCloseDate() {
		return applicationCloseDate;
	}
// uncomment if needed
//	public void setApplicationCloseDate(String applicationCloseDate) {
//		this.applicationCloseDate = applicationCloseDate;
//	}

	public Enum<InternshipApplicationStatus> getInternshipStatus() {
		return internshipStatus;
	}
// uncomment if needed
//	public void setInternshipStatus(Enum<InternshipApplicationStatus> internshipStatus) {
//		this.internshipStatus = internshipStatus;
//	}

	public int getNumOfSlots() {
		return numOfSlots;
	}
// uncomment if needed
//	public void setNumOfSlots(int numOfSlots) {
//		this.numOfSlots = numOfSlots;
//	}
	
	
}
