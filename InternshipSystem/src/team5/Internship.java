package team5;

import java.time.LocalDate;

import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

/**
 * Represents an internship opportunity in the system.
 * This class encapsulates all details about an internship posting including title,
 * description, level, preferred major, application dates, status, company information,
 * and available slots. Internships must be approved by Career Center Staff before
 * becoming visible to eligible students.
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
	 * Constructs a new Internship with the specified details.
	 *
	 * @param internshipId the unique identifier for the internship
	 * @param title the title of the internship position
	 * @param description a detailed description of the internship
	 * @param internshipLevel the difficulty level (BASIC, INTERMEDIATE, or ADVANCED)
	 * @param preferredMajor the preferred major for applicants
	 * @param applicationOpenDate the date when applications open
	 * @param applicationCloseDate the date when applications close
	 * @param status the approval status (PENDING, APPROVED, REJECTED, or FILLED)
	 * @param companyName the name of the company offering the internship
	 * @param companyRep the ID/email of the company representative managing this internship
	 * @param numOfSlots the number of available positions (maximum 10)
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
