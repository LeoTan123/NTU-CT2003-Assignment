package team5;

import java.time.LocalDate;

import team5.enums.InternshipApplicationStatus;

/**
 * InternshipApplication class
 */
public class InternshipApplication {
	private String applicationID;
	private Internship internship;
	private Student student;
	private LocalDate appliedDate;
	private InternshipApplicationStatus status;
	
	/**
	 * InternshipApplication constructor
	 * @param applicationID
	 * @param internship
	 * @param student
	 * @param appliedDate
	 * @param status
	 */
	public InternshipApplication(String applicationID, Internship internship, 
			Student student, LocalDate appliedDate, InternshipApplicationStatus status) {
		this.applicationID = applicationID;
		this.internship = internship;
		this.student = student;
		this.appliedDate = appliedDate;
		this.status = status;
	}
	
	// Setter and getter
	public String getApplicationId() {
		return this.applicationID;
	}
	
	public void setApplicationId(String applicationID) {
		this.applicationID = applicationID;
	}
	
	public Internship getInternshipInfo() {
		return this.internship;
	}
	
	public void setInternshipInfo(Internship internship) {
		this.internship = internship;
	}
	
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
	
	public InternshipApplicationStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(InternshipApplicationStatus status) {
		this.status = status;
	}
	
	@Override
	public String toString()
	{
		return this.applicationID + " " + this.internship + " " + this.student + " " + this.appliedDate + " " + this.status + " ";
	}
}
