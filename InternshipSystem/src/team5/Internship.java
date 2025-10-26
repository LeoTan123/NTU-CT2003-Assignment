package team5;

import java.time.LocalDate;

import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipLevel;
import team5.enums.StudentMajor;

public class Internship {
	private String internshipId;
	private String title;
	private String description;
	private InternshipLevel internshipLevel;
	private StudentMajor preferredMajor;
	private LocalDate applicationOpenDate;
	private LocalDate applicationCloseDate;
	private InternshipApplicationStatus internshipStatus;
	private String companyRep;
	private int numOfSlots;
	
	public Internship() {
		
	}
	
	public Internship(String internshipId, String title, String description, InternshipLevel internshipLevel, 
			StudentMajor preferredMajor, LocalDate applicationOpenDate, LocalDate applicationCloseDate, String companyRep) {
		this.internshipId = internshipId;
		this.title = title;
		this.description = description;
		this.internshipLevel = internshipLevel;
		this.preferredMajor = preferredMajor;
		this.applicationOpenDate = applicationOpenDate;
		this.applicationCloseDate = applicationCloseDate;
		this.internshipStatus = InternshipApplicationStatus.PENDING;
		this.companyRep = companyRep;
		this.numOfSlots = 10;
	}

	public String getInternshipId() {
		return this.internshipId;
	}
	
	/*public void setInternshipId(int internshipId) {
		this.internshipId = internshipId;
	}*/

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

	public String getPreferredMajor() {
		return App.studentMajors.get(this.preferredMajor);
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

	public InternshipApplicationStatus getInternshipStatus() {
		return this.internshipStatus;
	}
	

	public void setInternshipStatus(InternshipApplicationStatus internshipStatus) {
		this.internshipStatus = internshipStatus;
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
				+ " " + this.internshipStatus + " " + this.numOfSlots;
	}
}
