package team5;

import java.util.ArrayList;
import team5.enums.StudentMajor;
import team5.enums.UserType;

/**
 * Student class
 */
public class Student extends User {
	private StudentMajor major;
	private int yearOfStudy;
	private boolean employedStatus;
	private ArrayList<InternshipApplication> appliedInternships = new ArrayList<>();
	
	/**
	 * Student constructor
	 * @param userID
	 * @param name
	 * @param email
	 * @param password
	 * @param major
	 * @param yearOfStudy
	 */
	public Student(String userID, String name, String email, String password, 
			StudentMajor major, int yearOfStudy)
	{
		super(userID, name, email, password);
		this.major = major;
		this.yearOfStudy = yearOfStudy;
		this.employedStatus = false;
		super.setUserType(UserType.STUDENT);
	}
	
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
}
