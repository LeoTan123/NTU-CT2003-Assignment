package team5;

import java.util.ArrayList;
import team5.enums.StudentMajor;
import team5.enums.UserType;

public class Student extends User {
	private StudentMajor major;
	private int yearOfStudy;
	private boolean employedStatus;
	private ArrayList<InternshipApplication> appliedInternships = new ArrayList<>();
	
	public Student(String userID, String name, String email, String password, StudentMajor major, int yearOfStudy)
	{
		super(userID, name, email, password);
		this.major = major;
		this.yearOfStudy = yearOfStudy;
		super.setUserType(UserType.STUDENT);
	}
	
	// Setter and getter
	public void setMajor(StudentMajor major)
	{
		this.major = major;
	}
	
	public StudentMajor getMajor()
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
	
	public void clearInternshipApplications()
	{
		appliedInternships.clear();
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
	
	/*
	public void viewInternshipOpportunities() {
		if(App.internshipList.isEmpty()) {
			System.out.println("No internship opportunities created.");
			return;
		}
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
	
	public void applyInternship(String internshipId) {
		if(appliedInternships.size() == 3){
			System.out.println("You have applied for 3 internships. Current application is unsuccessful.");
			return;
		}
		
		// Find the internship by ID
	    Internship selected = null;
	    for (Internship internship : App.internshipList) {
	        if (internship.getInternshipId() == internshipId) {
	        	selected = internship;
	            break;
	        }
	    }

	    // If internship not found
	    if (selected == null) {
	        System.out.println("Internship " + internshipId + " is not found.");
	        return;
	    }

	    // Check if already applied to the same internship
	    for (InternshipApplication app : appliedInternships) {
	        if (app.getInternshipInfo().getInternshipId() == internshipId) {
	            System.out.println("You have already applied for this internship.");
	            return;
	        }
	    }

	    // Create new application
	    InternshipApplication internshipApplication = new InternshipApplication();
	    int applicationID = 10000 + new Random().nextInt(90000); // Random 5-digit ID
	    internshipApplication.setApplicationId(applicationID);
	    internshipApplication.setAppliedDate(LocalDate.now());
	    internshipApplication.setInternshipInfo(selected);
	    internshipApplication.setStudentInfo(this);
	    internshipApplication.setStatus(InternshipApplicationStatus.PENDING);

	    // Add to student's applied internships
	    addInternshipApplications(internshipApplication);

	    System.out.println("You have successfully applied for internship " + selected.getInternshipId() + " Title: " + selected.getTitle());
	}
	
	
	public void viewInternshipApplications() {
		if(this.appliedInternships.isEmpty()) {
			System.out.println("No internship application has been made.");
			return;
		}
		System.out.println("Internship ID \t Internship Name \t Status");
		for(InternshipApplication internshipApplication: this.appliedInternships) {
			System.out.println(internshipApplication.getInternshipInfo().getInternshipId() + "\t" + internshipApplication.getInternshipInfo().getTitle() + "\t" + internshipApplication.getStatus());
		}
	}
	
	public boolean acceptOffer(int applicationId) {
		if(this.appliedInternships.isEmpty()) {
			System.out.println("No internship application has been made.");
			return false;
		}
		for (InternshipApplication internshipApplication : this.appliedInternships) {
			if(internshipApplication.getApplicationId() == applicationId) {
				internshipApplication.setHasStudentAccepted(true);
				System.out.println("Offer has been accepted.");
				return true;
			}
	    }
		System.out.println("Offer "+ applicationId +" is not found.");
		return false;
	}
	*/
}
