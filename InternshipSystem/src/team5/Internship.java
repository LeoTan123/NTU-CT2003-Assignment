package team5;

import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipLevel;

public class Internship {
	private int internshipId;
	private String title;
	private String description;
	private InternshipLevel internshipLevel;
	private String preferredMajors;
	private String applicationOpenDate;
	private String applicationCloseDate;
	private InternshipApplicationStatus internshipStatus;
	private int numOfSlots;
	
	public Internship() {
		
	}

	public int getInternshipId() {
		return this.internshipId;
	}
	
	// uncomment if needed
//	public void setInternshipId(int internshipId) {
//		this.internshipId = internshipId;
//	}

	public String getTitle() {
		return this.title;
	}
	
	// uncomment if needed
//	public void setTitle(String title) {
//		this.title = title;
//	}

	public String getDescription() {
		return this.description;
	}
	
	// uncomment if needed
//	public void setDescription(String description) {
//		this.description = description;
//	}

	public InternshipLevel getInternshipLevel() {
		return this.internshipLevel;
	}
// uncomment if needed
//	public void setInternshipLevel(Enum<InternshipLevel> internshipLevel) {
//		this.internshipLevel = internshipLevel;
//	}

	public String getPreferredMajors() {
		return this.preferredMajors;
	}
// uncomment if needed
//	public void setPreferredMajors(String preferredMajors) {
//		this.preferredMajors = preferredMajors;
//	}

	public String getApplicationOpenDate() {
		return this.applicationOpenDate;
	}
// uncomment if needed
//	public void setApplicationOpenDate(String applicationOpenDate) {
//		this.applicationOpenDate = applicationOpenDate;
//	}

	public String getApplicationCloseDate() {
		return this.applicationCloseDate;
	}
// uncomment if needed
//	public void setApplicationCloseDate(String applicationCloseDate) {
//		this.applicationCloseDate = applicationCloseDate;
//	}

	public InternshipApplicationStatus getInternshipStatus() {
		return this.internshipStatus;
	}
// uncomment if needed
//	public void setInternshipStatus(Enum<InternshipApplicationStatus> internshipStatus) {
//		this.internshipStatus = internshipStatus;
//	}

	public int getNumOfSlots() {
		return this.numOfSlots;
	}
// uncomment if needed
//	public void setNumOfSlots(int numOfSlots) {
//		this.numOfSlots = numOfSlots;
//	}
	
	@Override
	public String toString()
	{
		return this.internshipId + " " + this.title + " " + this.description + " " + this.internshipLevel 
				+ " " + this.preferredMajors + " " + this.applicationOpenDate + " " + this.applicationCloseDate 
				+ " " + this.internshipStatus + " " + this.numOfSlots;
	}
}
