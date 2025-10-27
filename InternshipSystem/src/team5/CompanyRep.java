package team5;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import team5.enums.InternshipLevel;
import team5.enums.UserAccountStatus;
import team5.enums.UserType;

public class CompanyRep extends User{
	private String companyName;
	private String department;
	private String position;
	private UserAccountStatus accountStatus;
	// Can just use createdInternships.size()
	//private int numInternshipCreated; 
	//private Internship[] createdInternships;
	private ArrayList<Internship> createdInternships = new ArrayList<Internship>();
	
	public CompanyRep() {
		
	}
	
	public CompanyRep(String userID, String name, String email, String password, 
			String companyName, String department, String position, UserAccountStatus status) {
		super(userID, name, email, password);
		super.setUserType(UserType.COMREP);
		this.companyName = companyName;
		this.department = department;
		this.position = position;
		this.accountStatus = status;
		this.createdInternships.clear();
	}

	// Setter and getter
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
	
	/*public void addNumOfInternship()
	{
		this.numInternshipCreated += 1;
	}
	
	public int getNumOfInternship()
	{
		return this.numInternshipCreated;
	}*/
	
	public boolean addInternship(Internship internship)
	{
		if(this.createdInternships.size() == 5) {
			System.out.println("You have reached the maximum of 5 internship opportunities.");
			return false;
		}
		this.createdInternships.add(internship);
		return true;
	}
	
	public ArrayList<Internship> getInternships()
	{
		return this.createdInternships;
	}
	
	public CompanyRep registerAccount() {
		System.out.println("====== Register Account ======");
		System.out.println("Please enter your company name (or Q to quit):");
		String compName = App.sc.nextLine();
		if (compName.equalsIgnoreCase("Q")) 
			return null;
		System.out.println("Please enter your name (or Q to quit):");
		String name = App.sc.nextLine();
		if (name.equalsIgnoreCase("Q")) 
			return null;
		System.out.println("Please enter your department (or Q to quit):");
		String department = App.sc.nextLine();
		if (department.equalsIgnoreCase("Q")) 
			return null;
		System.out.println("Please enter your position (or Q to quit):");
		String position = App.sc.nextLine();
		if (position.equalsIgnoreCase("Q")) 
			return null;
		System.out.println("Please enter your email (or Q to quit):");
		String email = App.sc.nextLine();
		if (email.equalsIgnoreCase("Q")) 
			return null;
		
		// Use email as UserID
		CompanyRep rep = new CompanyRep(email, name, email, "password", compName, department, position, UserAccountStatus.PENDING);
		System.out.println("Registration successfully. Account is pending approval. Your User ID will be your email and password will be 'password'.");
		return rep;
	}
	/*
	public Internship createInternship() {
		System.out.println("====== Create Internship Opportunities ======");
		
		// 5 digit random InternshipID 
		int internshipID = 10000 + new Random().nextInt(90000);
		System.out.println("Please enter the internship title (or Q to quit):");
		String title = App.sc.nextLine();
		if (title.equalsIgnoreCase("Q")) 
			return null;
		System.out.println("Please enter the internship description (or Q to quit):");
		String description = App.sc.nextLine();
		if (description.equalsIgnoreCase("Q")) 
			return null;
		System.out.println("Please choose the internship level (or Q to quit):");
		System.out.println("1: Basic");
		System.out.println("2: Intermediate");
		System.out.println("3: Advanced");
		String level = App.sc.nextLine();
		if (level.equalsIgnoreCase("Q")) 
			return null;
		// If not equal to this 3 levels, return null
		if(!level.equals("1") && !level.equals("2") && !level.equals("3")) {
			System.out.println("Invalid input of level.");
			return null;
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
		System.out.println("Please enter the internship preferred major (or Q to quit):");
		String preferredMajor = App.sc.nextLine();
		if (preferredMajor.equalsIgnoreCase("Q")) 
			return null;
		LocalDate startDate = readDate("Please enter the internship start date");
		LocalDate endDate = readDate("Please enter the internship end date");
		
		Internship internship = new Internship(internshipID, title, description, internshipLevel, preferredMajor, startDate, endDate, "CompanyRep");
		System.out.println("Internship opportunities created successfully. Please wait for approval.");
		return internship;
	}
	*/
	
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
	
	// Helper
	private LocalDate readDate(String prompt) {
	    System.out.println(prompt + " (format DD/MM/YYYY or Q to quit):");
	    String input = App.sc.nextLine();
	    if (input.equalsIgnoreCase("Q")) 
	    	return null;

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    try {
	        return LocalDate.parse(input, formatter);
	    } catch (DateTimeParseException e) {
	        System.out.println("Invalid date format! Please use DD/MM/YYYY.");
	        return null;
	    }
	}
}
