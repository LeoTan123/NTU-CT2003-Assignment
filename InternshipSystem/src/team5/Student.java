package team5;

import java.util.ArrayList;
import team5.enums.StudentMajor;
import team5.enums.UserType;

/**
 * Represents a student user in the internship placement management system.
 * This class extends User and includes student-specific attributes such as major,
 * year of study, employment status, and a list of internship applications.
 * Students can view and apply for internships based on their profile and eligibility.
 */
public class Student extends User {
	/** Maximum number of concurrent internship applications a student can have */
	public static final int MAX_APPLICATIONS = 3;

	private StudentMajor major;
	private int yearOfStudy;
	private boolean employedStatus;
	private ArrayList<InternshipApplication> appliedInternships = new ArrayList<>();
	
	/**
	 * Constructs a new Student with the specified details.
	 * Initializes the student with employed status set to false and an empty list
	 * of internship applications. The user type is automatically set to STUDENT.
	 *
	 * @param userID the unique identifier for the student (format: U + 7 digits + letter)
	 * @param name the full name of the student
	 * @param email the email address of the student
	 * @param password the password for the student account
	 * @param major the academic major of the student
	 * @param yearOfStudy the current year of study (1-4)
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
