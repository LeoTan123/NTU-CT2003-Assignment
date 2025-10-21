package team5;

import java.util.ArrayList;
import java.util.Date;

import team5.enums.InternshipApplicationStatus;
import team5.enums.InternshipLevel;
import team5.enums.UserType;

public class Student extends User {
	private String major;
	private int yearOfStudy;
	private boolean employedStatus;
	//private InternshipApplication[] appliedInternships;
	private ArrayList<InternshipApplication> appliedInternships = new ArrayList<>();
	
	public Student()
	{
		
	}
	
	public Student(String userID, String name, String email, String password, String major, int yearOfStudy)
	{
		super(userID, name, email, password);
		this.major = major;
		this.yearOfStudy = yearOfStudy;
		super.setUserType(UserType.STUDENT);
	}
	
	// Setter and getter
	public void setMajor(String major)
	{
		this.major = major;
	}
	
	public String getMajor()
	{
		return this.major;
	}
	
	public void setYear(int year)
	{
		this.yearOfStudy = year;
	}
	
	public int getYear()
	{
		return this.yearOfStudy;
	}
	
	public void setEmployedStatus(boolean employedStatus)
	{
		this.employedStatus = employedStatus;
	}
	
	public boolean getEmployedStatus()
	{
		return this.employedStatus;
	}
	
	public void addInternshipApplications(InternshipApplication internshipApplication)
	{
		appliedInternships.add(internshipApplication);
	}
	
	public ArrayList<InternshipApplication> getInternshipApplications()
	{
		return this.appliedInternships;
	}
	
	// Behaviors
	@Override
	public String toString()
	{
		return super.getUserID() + " " + super.getName() + " " + super.getEmail() 
		+ " " + super.getPassword() + " " + this.major + " " + this.yearOfStudy;
	}
	
	
	public void viewInternshipOpportunities() {
		System.out.println("===== Internship Opportunities =====");
	    for (Internship internship : App.internshipList) {
	    	if((this.yearOfStudy == 1 || this.yearOfStudy == 2))
	    	{
	    		if(internship.getInternshipLevel() != InternshipLevel.BASIC){
	    			continue;
	    		}
	    		System.out.println(internship);
	    	}
	    	else
	    	{
	    		System.out.println(internship);
	    	}
	    }
	}
	
	public void applyInternship(int internshipId) {
		if(appliedInternships.size() == 3)
		{
			System.out.println("You have applied for 3 internships. Current application is unsuccessful.");
			return;
		}
		for (Internship internship : App.internshipList) {
			if(internship.getInternshipId() == internshipId) {
				InternshipApplication internshipApplication = new InternshipApplication();
				internshipApplication.setApplicationId(internshipId);
				internshipApplication.setAppliedDate(new Date());
				internshipApplication.setStudentUsername(this.getName());
				internshipApplication.setStatus(InternshipApplicationStatus.PENDING);
				addInternshipApplications(internshipApplication);
			}
		}
	}
	
	
	/*public InternshipApplication[] viewInternshipApplications() {
		return new InternshipApplication[0];
	}*/
	
	public boolean acceptOffer(int applicationId) {
		/*for (InternshipApplication app : appliedInternships) {
	        if (app.getApplicationId() == applicationId) {
	            app.setOfferAccepted(true);
	            return true;
	        }
	    }*/
		return false;
	}
	
}
