package team5;

import team5.enums.InternshipApplicationStatus;
import team5.enums.UserAccountStatus;

public class CompanyRep {
	private String companyName;
	private String department;
	private String position;
	private Enum<UserAccountStatus> accountStatus;
	private int numInternshipCreated;
	private Internship[] createdInternships;
	
	public CompanyRep() {
		
	}
	
	public void registerAccount() {
		
	}
	
	public void createInternship() {
		
	}
	
	public void editInternship() {
		
	}
	
	public void removeInternship() {
		
	}
	
	public Enum<InternshipApplicationStatus> viewInternshipApproval() {
		return InternshipApplicationStatus.PENDING;
	}
	
	public void viewInternshipApplicationFromStudent() {
		
	}
}
