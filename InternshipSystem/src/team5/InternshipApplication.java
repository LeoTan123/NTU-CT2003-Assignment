package team5;

import java.util.Date;

import team5.enums.InternshipApplicationStatus;

public class InternshipApplication {
	private int applicationId;
	private int internshipId;
	private String studentUsername;
	private Date appliedDate;
	private InternshipApplicationStatus status;
	private boolean hasStudentAccepted;
	
	public InternshipApplication() {
		
	}
	
	public int getApplicationId() {
		return this.applicationId;
	}
	
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	
	public int getInternshipId() {
		return this.internshipId;
	}
	
	public void setInternshipId(int internshipId) {
		this.internshipId = internshipId;
	}
	
	public String getStudentUsername() {
		return this.studentUsername;
	}
	
	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}
	
	public Date getAppliedDate() {
		return this.appliedDate;
	}
	
	public void setAppliedDate(Date appliedDate) {
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
		return this.applicationId + " " + this.internshipId + " " + this.studentUsername + " " + this.appliedDate 
				+ " " + this.status + " " + this.hasStudentAccepted;
	}
	
	public boolean hasStudentAccepted() {
		return this.hasStudentAccepted;
	}
	
	public void setHasStudentAccepted(boolean hasStudentAccepted) {
		this.hasStudentAccepted = hasStudentAccepted;
	}
	
	public void apply(int internshipId, String studentUserName) {
	}
}
