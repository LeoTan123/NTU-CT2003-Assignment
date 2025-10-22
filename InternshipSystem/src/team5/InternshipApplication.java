package team5;

import java.time.LocalDate;

import team5.enums.InternshipApplicationStatus;

public class InternshipApplication {
	private int applicationID;
	//private int internshipID;
	private Internship internship;
	//private String studentUsername;
	private Student student;
	private LocalDate appliedDate;
	private InternshipApplicationStatus status;
	private boolean hasStudentAccepted;
	
	public InternshipApplication() {
	}
	
	public InternshipApplication(int applicationID, Internship internship, Student student, LocalDate appliedDate) {
		this.applicationID = applicationID;
		//this.internshipID = internshipID;
		//this.studentUsername = studentUsername;
		this.internship = internship;
		this.student = student;
		this.appliedDate = appliedDate;
		this.status = InternshipApplicationStatus.PENDING;
		this.hasStudentAccepted = false;
	}
	
	// Setter and getter
	public int getApplicationId() {
		return this.applicationID;
	}
	
	public void setApplicationId(int applicationID) {
		this.applicationID = applicationID;
	}
	
	/*public int getInternshipId() {
		return this.internshipID;
	}
	
	public void setInternshipId(int internshipId) {
		this.internshipID = internshipId;
	}*/
	
	public Internship getInternshipInfo() {
		return this.internship;
	}
	
	public void setInternshipInfo(Internship internship) {
		this.internship = internship;
	}
	
	/*public String getStudentUsername() {
		return this.studentUsername;
	}
	
	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}*/
	
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
	
	public boolean hasStudentAccepted() {
		return this.hasStudentAccepted;
	}
	
	public void setHasStudentAccepted(boolean hasStudentAccepted) {
		this.hasStudentAccepted = hasStudentAccepted;
	}
	
	@Override
	public String toString()
	{
		return this.applicationID + " " + this.internship + " " + this.student + " " + this.appliedDate + " " + this.status + " " + this.hasStudentAccepted;
	}

	public void apply(int internshipId, String studentUserName) {
	}
}
