package team5;

import java.util.ArrayList;

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
		
	}
	
	public void applyInternship(int internshipId) {
		
	}
	
	public InternshipApplication[] viewInternshipApplications() {
		return new InternshipApplication[0];
	}
	
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
