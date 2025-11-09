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
	
	public void editInternship() {
		if(this.createdInternships.isEmpty()) {
			System.out.println("No internship opportunities has been created.");
			return;
		}
		System.out.println("Internship ID \t Title");
		for(Internship internship: this.createdInternships) {
			 System.out.println(internship.getInternshipId() + "\t" + internship.getTitle());
		}
		System.out.println("Please enter the internship ID that you would like to edit (or Q to quit):");
		String enterID = App.sc.nextLine();
		if (enterID.equalsIgnoreCase("Q")) 
			return;
		
		/*
		String enterID_string;
		
	    try {
	    	enterID_string = Integer.parseInt(enterID);
	    } catch (NumberFormatException e) {
	        System.out.println("Invalid input. Please enter a valid internship ID.");
	        return;
	    }*/
	    
	    Internship selected = null;
	    for (Internship internship : this.createdInternships) {
	        if (internship.getInternshipId() == enterID) {
	            selected = internship;
	            break;
	        }
	    }
	    
	    if (selected == null) {
	        System.out.println("Internship " + enterID + " is not found.");
	        return;
	    }
	    
	    // Edit Menu
	    boolean done = false;
	    while (!done) {
	        System.out.println("\nEditing Internship: " + selected.getInternshipId() + " " + selected.getTitle());
	        System.out.println("1. Edit Title");
	        System.out.println("2. Edit Description");
	        System.out.println("3. Edit Internship Level");
	        System.out.println("4. Edit Preferred Major");
	        System.out.println("5. Edit Application Open Date");
	        System.out.println("6. Edit Application Close Date");
	        System.out.println("7. Back to previous menu");
	        System.out.print("Enter your choice: ");
	        
	        String choice = App.sc.nextLine();

	        switch (choice) {
	            case "1":
	                System.out.print("Enter new title: ");
	                String newTitle = App.sc.nextLine();
	                selected.setTitle(newTitle);
	                System.out.println("Title updated successfully!");
	                break;

	            case "2":
	                System.out.print("Enter new description: ");
	                String newDesc = App.sc.nextLine();
	                selected.setDescription(newDesc);
	                System.out.println("Description updated successfully!");
	                break;
	                
	            case "3":
	            	System.out.print("Enter new internship level: ");
	            	System.out.print("1: Basic");
	            	System.out.print("2: Intermediate");
	            	System.out.print("3: Advanced");
	            	String level = App.sc.nextLine();
	            	if(!level.equals("1") && !level.equals("2") && !level.equals("3")) {
	        			System.out.println("Invalid input of level.");
	        			break;
	        		}
	        		InternshipLevel internshipLevel = InternshipLevel.NONE;
	        		switch(level) {
	        			case "1":
	        				internshipLevel = InternshipLevel.BASIC;
	        			case "2":
	        				internshipLevel = InternshipLevel.INTERMEDIATE;
	        			case "3":
	        				internshipLevel = InternshipLevel.ADVANCED;
	        		}
	        		selected.setInternshipLevel(internshipLevel);
	        		System.out.println("Internship level updated successfully!");
	            	break;
	            	
	            case "4":
	            	System.out.print("WIP");
	            	/*
	            	System.out.print("Enter new preferred major: ");
	            	String newPreferredMajor = App.sc.nextLine();
	            	
	            	selected.setPreferredMajor(newPreferredMajor);
		            System.out.println("Preferred major updated successfully!");
		            */
	            	break;
	            	
	            case "5":
	                System.out.print("Enter new start date (DD/MM/YYYY): ");
	                String startStr = App.sc.nextLine();
	                try {
	                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	                    LocalDate startDate = LocalDate.parse(startStr, fmt);
	                    selected.setApplicationOpenDate(startDate);
	                    System.out.println("Application open date updated successfully!");
	                } catch (Exception e) {
	                    System.out.println("Invalid date format.");
	                }
	                break;

	            case "6":
	                System.out.print("Enter new end date (DD/MM/YYYY): ");
	                String endStr = App.sc.nextLine();
	                try {
	                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	                    LocalDate endDate = LocalDate.parse(endStr, fmt);
	                    selected.setApplicationCloseDate(endDate);
	                    System.out.println("Application close date updated successfully!");
	                } catch (Exception e) {
	                    System.out.println("Invalid date format.");
	                }
	                break;

	            case "7":
	                done = true;
	                System.out.println("Returning to previous menu...");
	                break;

	            default:
	                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
	        }
	    }
	}
	
	public void removeInternship() {
		if(this.createdInternships.isEmpty()) {
			System.out.println("No internship opportunities has been created.");
			return;
		}
		System.out.println("Internship ID \t Title");
		for(Internship internship: this.createdInternships) {
			System.out.println(internship.getInternshipId() + "\t" + internship.getTitle());
		}
		System.out.println("Please enter the internship ID that you would like to remove (or Q to quit):");
		String enterID = App.sc.nextLine();
		if (enterID.equalsIgnoreCase("Q")) 
			return;
		
		/*
		int enterID_int;
	    try {
	        enterID_int = Integer.parseInt(enterID);
	    } catch (NumberFormatException e) {
	        System.out.println("Invalid input. Please enter a valid internship ID.");
	        return;
	    }*/
	    
	    boolean removed = false;
		for (int i = 0; i < this.createdInternships.size(); i++) {
		    if (this.createdInternships.get(i).getInternshipId() == enterID) {
		    	this.createdInternships.remove(i);
		    	System.out.println("Internship " + enterID + " has been removed.");
		    	removed = true;
		        break;
		    }
		}
		if(!removed){
			System.out.println("Internship " + enterID + " is not found.");
		}
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
