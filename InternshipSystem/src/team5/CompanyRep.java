package team5;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import team5.enums.InternshipLevel;
import team5.enums.UserAccountStatus;
import team5.enums.UserType;

/**
 * Company representative class
 */
public class CompanyRep extends User{
	private String companyName;
	private String department;
	private String position;
	private UserAccountStatus accountStatus;
	private ArrayList<Internship> createdInternships = new ArrayList<Internship>();
	
	/**
	 * Company representative constructor
	 * @param userID
	 * @param name
	 * @param email
	 * @param password
	 * @param companyName
	 * @param department
	 * @param position
	 * @param status
	 * @param createdInternships
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

	public boolean addInternship(Internship internship)
	{
		boolean maximumReached = this.isMaxInternshipReached();
		if (maximumReached) {
			return false;
		}
		
		this.createdInternships.add(internship);
		return true;
	}
	
	public boolean isMaxInternshipReached() {
		if(this.createdInternships.size() == 5) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Internship> getInternships()
	{
		return this.createdInternships;
	}
	
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
