package team5;

import java.util.ArrayList;
import team5.enums.UserAccountStatus;
import team5.enums.UserType;

/**
 * Represents a company representative user in the internship placement management system.
 * This class extends User and includes company-specific attributes such as company name,
 * department, position, and account status. Company representatives can create internship
 * opportunities (up to 5), review student applications, and manage their postings.
 * Account must be approved by Career Center Staff before login is allowed.
 */
public class CompanyRep extends User{
	/** Maximum number of internships a company representative can create */
	public static final int MAX_INTERNSHIPS_PER_REP = 5;

	private String companyName;
	private String department;
	private String position;
	private UserAccountStatus accountStatus;
	private ArrayList<Internship> createdInternships = new ArrayList<Internship>();
	
	/**
	 * Constructs a new CompanyRep with the specified details.
	 * The user type is automatically set to COMREP. Account status determines
	 * whether the representative can log in to the system.
	 *
	 * @param userID the unique identifier for the company representative (company email)
	 * @param name the full name of the representative
	 * @param email the email address of the representative
	 * @param password the password for the representative account
	 * @param companyName the name of the company the representative works for
	 * @param department the department within the company
	 * @param position the job position of the representative
	 * @param status the account approval status (PENDING, APPROVED, or REJECTED)
	 * @param createdInternships the list of internships created by this representative
	 */
	public CompanyRep(String userID, String name, String email, String password,
			String companyName, String department, String position,
			UserAccountStatus status, ArrayList<Internship> createdInternships) {
		super(userID, name, email, password);
		this.companyName = companyName;
		this.department = department;
		this.position = position;
		this.accountStatus = status;
		this.createdInternships = createdInternships;
		super.setUserType(UserType.COMREP);
	}

	public void setCompanyName(String compName)
	{
		this.companyName = compName;
	}
	
	public String getCompanyName()
	{
		return this.companyName;
	}
	
	public void setDepartment(String department)
	{
		this.department = department;
	}
	
	public String getDepartment()
	{
		return this.department;
	}
	
	public void setPosition(String position)
	{
		this.position = position;
	}
	
	public String getPosition()
	{
		return this.position;
	}
	
	public void setAccountStatus(UserAccountStatus accountStatus)
	{
		this.accountStatus = accountStatus;
	}
	
	public UserAccountStatus getAccountStatus()
	{
		return this.accountStatus;
	}

	/**
	 * Adds a new internship to the list of internships created by this representative.
	 * Checks if the maximum number of internships (5) has been reached before adding.
	 *
	 * @param internship the internship to be added
	 * @return true if the internship is successfully added, false if the maximum limit is reached
	 */
	public boolean addInternship(Internship internship)
	{
		boolean maximumReached = this.isMaxInternshipReached();
		if (maximumReached) {
			return false;
		}

		this.createdInternships.add(internship);
		return true;
	}

	/**
	 * Checks whether the company representative has reached the maximum number of internships.
	 * The maximum allowed is defined by MAX_INTERNSHIPS_PER_REP constant.
	 *
	 * @return true if the maximum number of internships has been reached, false otherwise
	 */
	public boolean isMaxInternshipReached() {
		if(this.createdInternships.size() == MAX_INTERNSHIPS_PER_REP) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Internship> getInternships()
	{
		return this.createdInternships;
	}
	
	/**
	 * Updates an existing internship with new details.
	 * Searches for the internship by ID and updates all editable fields.
	 *
	 * @param internship the internship object containing updated information
	 * @return true if the internship is found and updated, false otherwise
	 */
	public boolean updateInternship(Internship internship) {
		boolean isUpdated = false;
		for (int i = 0; i < this.createdInternships.size(); i++) {
		    if (this.createdInternships.get(i).getInternshipId() == internship.getInternshipId()) {		    	
		    	this.createdInternships.get(i).setTitle(internship.getTitle());
		    	this.createdInternships.get(i).setDescription(internship.getDescription());
		    	this.createdInternships.get(i).setInternshipLevel(internship.getInternshipLevel()); 
		    	this.createdInternships.get(i).setPreferredMajor(internship.getPreferredMajor());
		    	this.createdInternships.get(i).setApplicationOpenDate(internship.getApplicationOpenDate());
		    	this.createdInternships.get(i).setApplicationCloseDate(internship.getApplicationCloseDate());
				this.createdInternships.get(i).setNumOfSlots(internship.getNumOfSlots());
		    	isUpdated = true;
		        break;
		    }
		}
		return isUpdated;
	}
	
	/**
	 * Removes an internship from the list of created internships.
	 * Searches for the internship by ID and removes it from the list.
	 *
	 * @param internship the internship to be removed
	 * @return true if the internship is found and removed, false otherwise
	 */
	public boolean removeInternship(Internship internship) {
	    boolean isRemoved = false;
		for (int i = 0; i < this.createdInternships.size(); i++) {
		    if (this.createdInternships.get(i).getInternshipId() == internship.getInternshipId()) {
		    	this.createdInternships.remove(i);
		    	isRemoved = true;
		        break;
		    }
		}
		return isRemoved;
	}
	
	public void viewInternshipApproval() {
		if(this.createdInternships.isEmpty()) {
			System.out.println("No internship opportunities has been created.");
			return;
		}
		System.out.println("Internship ID \t Title \t Status");
		for(Internship internship: this.createdInternships) {
			System.out.println(internship.getInternshipId() + "\t" + internship.getTitle() + "\t" + internship.getInternshipStatus());
		}
	}
	
	public void viewInternshipApplicationFromStudent() {
		
	}
}
