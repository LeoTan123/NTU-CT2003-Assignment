package team5;

import java.time.LocalDate;

import team5.enums.InternshipApplicationStatus;

/**
 * Represents a student's application to an internship opportunity.
 * This class tracks the relationship between a student and an internship,
 * including the application date and current status. Application statuses
 * progress from PENDING to SUCCESSFUL/UNSUCCESSFUL, and finally to ACCEPTED
 * when a student confirms their placement.
 */
public class InternshipApplication {
	private String applicationID;
	private Internship internship;
	private Student student;
	private LocalDate appliedDate;
	private InternshipApplicationStatus status;
	
	/**
	 * Constructs a new InternshipApplication linking a student to an internship.
	 *
	 * @param applicationID the unique identifier for this application
	 * @param internship the internship being applied to
	 * @param student the student submitting the application
	 * @param appliedDate the date when the application was submitted
	 * @param status the current status of the application (PENDING, SUCCESSFUL, UNSUCCESSFUL, or ACCEPTED)
	 */
	public InternshipApplication(String applicationID, Internship internship,
			Student student, LocalDate appliedDate, InternshipApplicationStatus status) {
		this.applicationID = applicationID;
		this.internship = internship;
		this.student = student;
		this.appliedDate = appliedDate;
		this.status = status;
	}
	
	/**
	 * Gets the unique application ID.
	 *
	 * @return the application ID
	 */
	public String getApplicationId() {
		return this.applicationID;
	}
	
	public void setApplicationId(String applicationID) {
		this.applicationID = applicationID;
	}
	
	/**
	 * Gets the internship associated with this application.
	 *
	 * @return the internship object
	 */
	public Internship getInternshipInfo() {
		return this.internship;
	}
	
	public void setInternshipInfo(Internship internship) {
		this.internship = internship;
	}
	
	/**
	 * Gets the student who submitted this application.
	 *
	 * @return the student object
	 */
	public Student getStudentInfo()
	{
		return this.student;
	}
	
	public void setStudentInfo(Student student)
	{
		this.student = student;
	}
	
	public LocalDate getAppliedDate() {
		return this.appliedDate;
	}
	
	public void setAppliedDate(LocalDate appliedDate) {
		this.appliedDate = appliedDate;
	}
	
	/**
	 * Gets the current status of this application.
	 *
	 * @return the application status
	 */
	public InternshipApplicationStatus getStatus() {
		return this.status;
	}
	
	/**
	 * Sets the status of this application.
	 *
	 * @param status the new application status
	 */
	public void setStatus(InternshipApplicationStatus status) {
		this.status = status;
	}
	
	@Override
	public String toString()
	{
		return this.applicationID + " " + this.internship + " " + this.student + " " + this.appliedDate + " " + this.status + " ";
	}
}
