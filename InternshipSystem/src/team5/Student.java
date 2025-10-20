package team5;

public class Student extends User {
	private String major;
	private int yearOfStudy;
	private boolean employedStatus;
	private InternshipApplication[] appliedInternships;
	
	public void viewInternshipOpportunities() {
		
	}
	
	public void applyInternship(int internshipId) {
		
	}
	
	public InternshipApplication[] viewInternshipApplications() {
		return new InternshipApplication[0];
	}
	
	public boolean acceptOffer(int applicationId) {
		return false;
	}
}
