package team5;

import java.time.LocalDate;

import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

/**
 * Internship class
 */
public class Internship {
	private String internshipId;
	private String title;
	private String description;
	private InternshipLevel internshipLevel;
	private StudentMajor preferredMajor;
	private LocalDate applicationOpenDate;
	private LocalDate applicationCloseDate;
	private InternshipStatus internshipStatus;
	private String companyName;
	private String companyRep;
	private int numOfSlots;
	
	/** 
	 * Internship constructor
	 * @param internshipId
	 * @param title
	 * @param description
	 * @param internshipLevel
	 * @param preferredMajor
	 * @param applicationOpenDate
	 * @param applicationCloseDate
	 * @param status
	 * @param companyName
	 * @param companyRep
	 * @param numOfSlots
	 */
	public Internship(String internshipId, String title, String description, InternshipLevel internshipLevel, 
			StudentMajor preferredMajor, LocalDate applicationOpenDate, LocalDate applicationCloseDate, InternshipStatus status,
			String companyName, String companyRep, int numOfSlots) {
		
		this.internshipId = internshipId;
		this.title = title;
		this.description = description;
		this.internshipLevel = internshipLevel;
		this.preferredMajor = preferredMajor;
		this.applicationOpenDate = applicationOpenDate;
		this.applicationCloseDate = applicationCloseDate;
		this.internshipStatus = status;
		this.companyName = companyName;
		this.companyRep = companyRep;
		this.numOfSlots = numOfSlots;
	}

	public String getInternshipId() {
		return this.internshipId;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public InternshipLevel getInternshipLevel() {
		return this.internshipLevel;
	}

	public void setInternshipLevel(InternshipLevel internshipLevel) {
		this.internshipLevel = internshipLevel;
	}

	public StudentMajor getPreferredMajor() {
		return this.preferredMajor;
	}
	
	public void setPreferredMajor(StudentMajor preferredMajor) {
		this.preferredMajor = preferredMajor;
	}

	public LocalDate getApplicationOpenDate() {
		return this.applicationOpenDate;
	}

	public void setApplicationOpenDate(LocalDate applicationOpenDate) {
		this.applicationOpenDate = applicationOpenDate;
	}

	public LocalDate getApplicationCloseDate() {
		return this.applicationCloseDate;
	}

	public void setApplicationCloseDate(LocalDate applicationCloseDate) {
		this.applicationCloseDate = applicationCloseDate;
	}

	public InternshipStatus getInternshipStatus() {
		return this.internshipStatus;
	}

	public void setInternshipStatus(InternshipStatus internshipStatus) {
		this.internshipStatus = internshipStatus;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getCompanyName() {
		return this.companyName;
	}
	
	public void setCompanyRep(String companyRep) {
		this.companyRep = companyRep;
	}
	
	public String getCompanyRep() {
		return this.companyRep;
	}

	public int getNumOfSlots() {
		return this.numOfSlots;
	}

	public void setNumOfSlots(int numOfSlots) {
		this.numOfSlots = numOfSlots;
	}
	
	@Override
	public String toString()
	{
		return this.internshipId + " " + this.title + " " + this.description + " " + this.internshipLevel 
				+ " " + this.preferredMajor + " " + this.applicationOpenDate + " " + this.applicationCloseDate 
				+ " " + this.companyName + " " + this.companyRep
				+ " " + this.internshipStatus + " " + this.numOfSlots;
	}
}
