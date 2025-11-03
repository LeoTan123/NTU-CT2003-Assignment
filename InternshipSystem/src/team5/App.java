package team5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import team5.controllers.CareerCenterStaffController;
import team5.controllers.CompanyRepController;
import team5.controllers.StudentController;
import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;
import team5.enums.UserAccountStatus;
import team5.enums.UserType;
import team5.registration.CompanyRepRegistrationHandler;

public class App {
	
	// Global Scanner for the whole program
    public static Scanner sc = new Scanner(System.in);
	// Student List
	public static ArrayList<Student> studentList = new ArrayList<>();
	// Staff List
	public static ArrayList<CareerCenterStaff> staffList = new ArrayList<>();
	// CompanyRepresentative List
	public static ArrayList<CompanyRep> compRepList = new ArrayList<>();
	// Internship List
	public static ArrayList<Internship> internshipList = new ArrayList<>();
	// Current user
	public static User currentUser = null;
	
	// System variables
	public static final String ERROR_MESSAGE = "Something went wrong. Please try again later.";
	public static String envFilePathStudent;
	public static String envFilePathStaff;
	public static String envFilePathRep;
	public static String envFilePathInternship;
	public static DateTimeFormatter DATE_DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static DateTimeFormatter DATE_DB_FORMATTER = DateTimeFormatter.ofPattern("dd MM yyyy");
	
	public static void main(String[] args) {
		// Initialise stuff
		LoadEnvironmentVariables();
		
		// Read from CSV files
		studentList.clear();
		staffList.clear();
		compRepList.clear();
		internshipList.clear();
		
		ReadFromCSV(envFilePathInternship, UserType.NONE); // Load internship before comrep so that comrep can use the data
		ReadFromCSV(envFilePathStudent, UserType.STUDENT);
		ReadFromCSV(envFilePathStaff, UserType.CCSTAFF);
		ReadFromCSV(envFilePathRep, UserType.COMREP);
		
		printSectionTitle("Welcome to Internship System");
        
        boolean exitProgram = false;
        while (!exitProgram) {
        	printSectionTitle("Login", true);
	        try 
	        {
				// Choose login type
				System.out.println("Please choose your user type:");
				System.out.println("1: Student");	
				System.out.println("2: Career Center Staff");	
				System.out.println("3: Company Representatives");	
				System.out.println("4: Register Company Representative");
				System.out.println("0: Exit");
				
				int choice = Integer.parseInt(App.sc.nextLine()); // consume the newline
				if(choice == 0){
					System.out.println("Exitting system...");
					exitProgram = true;
					break;
				}
				if(choice < 0 || choice > 4){
					System.out.println("Invalid user type. Please enter again.");	
					continue;
				}

				if(choice == 4){
					CompanyRepRegistrationHandler registrationHandler = new CompanyRepRegistrationHandler();
					registrationHandler.startRegistration();
					continue;
				}
				
				System.out.println("Please enter your user ID:");	
				String userID = App.sc.nextLine();
				
				System.out.println("Please enter your password:");	
				String password = App.sc.nextLine();
				
				boolean foundUser = false;
				
				UserType userType = UserType.NONE;
				switch(choice)
				{
					case 1:
						userType = UserType.STUDENT;
						break;
					case 2:
						userType = UserType.CCSTAFF;
						break;
					case 3:
						userType = UserType.COMREP;
						break;
					default:
						System.out.println("Invalid user type. Please enter again.");
				}

				foundUser = verifyUserFromList(userType, userID, password);
				if(!foundUser || currentUser == null){
					continue;
				}

				System.out.println("Login successful. Welcome " + currentUser.getName() + ".");
				
				// Display menu based on UserType
				if (userType == UserType.STUDENT && currentUser instanceof Student) {
					StudentController studentController = new StudentController();
					studentController.showMenu((Student)currentUser);
				} else if (userType == UserType.CCSTAFF && currentUser instanceof CareerCenterStaff) {
					CareerCenterStaffController staffController = new CareerCenterStaffController();
					staffController.showMenu((CareerCenterStaff)currentUser);
				} else if (userType == UserType.COMREP && currentUser instanceof CompanyRep) {
					CompanyRepController companyRepController = new CompanyRepController();
					companyRepController.showMenu((CompanyRep)currentUser);
				}
	        } 
	        catch (NumberFormatException e) {
	            System.out.println("Invalid input! Please enter a number for user type.");
	        } 
	        catch (Exception e) {
	            System.out.println("An error occurred during login: " + e.getMessage());
	            e.printStackTrace();
	        }
        }
	}
	
	// Helper
	public static boolean verifyUserFromList(UserType userType, String userID, String password)
	{ 
		User foundUser = null;
		switch(userType)
		{
			case STUDENT:
				for (Student student : studentList) {
	                if (student.getUserID().equals(userID)) {
	                    foundUser = student;
	                    break;
	                }
	            }
				break;
			case CCSTAFF:
				for (CareerCenterStaff staff : staffList) {
	                if (staff.getUserID().equals(userID)) {
	                    foundUser = staff;
	                    break;
	                }
	            }
				break;
			case COMREP:
				for (CompanyRep rep : compRepList) {
	                if (rep.getUserID().equalsIgnoreCase(userID)){
	                    foundUser = rep;
	                    break;
	                }
	            }
				break;
			default:
				System.out.println("Invalid User Type.");
	            return false;	
		}
		
		// User not found in either lists
		if (foundUser == null) {
			System.out.println("User ID not found in system.");
	        return false;
	    }
		
		// Special check for COMREP account status
	    if (userType == UserType.COMREP && ((CompanyRep)foundUser).getAccountStatus() != UserAccountStatus.APPROVED) {
	        System.out.println("Your account is not approved yet. Please contact the career center staff for approval.");
	        return false;
	    }
	    
	    // Check password
	    int maxAttempts = 3;
	    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
	        if (foundUser.getPassword().equals(password)) {
	            currentUser = foundUser;
	            currentUser.setUserType(userType);
	            foundUser.login();
	            return true;
	        } 
	        else {
	            if (attempt < maxAttempts) {
	                System.out.println("Password wrong, please enter again:");
	                password = App.sc.nextLine().trim(); // update password for next attempt
	            } 
	            else {
	                System.out.println("Wrong password 3 times. Login Failed.");
	            }
	        }
	    }
	    // Failed after 3 attempts
	    return false;
	}
	
	public static void ReadFromCSV(String fileName, UserType userType)
	{
		String csvFile = fileName;
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	// Skip header
        	br.readLine();
        	
            while ((line = br.readLine()) != null) {
                String[] values = line.split(csvSplitBy);
                if(userType == UserType.STUDENT && values.length == 5)
                {
                   String id = values[0].trim();
 		    	   String name = values[1].trim();
 		    	   String prefferedMajor = values[2].trim();
 		    	   int year = Integer.parseInt(values[3].trim());
 		    	   String email = values[4].trim();
 		    	   
 		    	   StudentMajor major = StudentMajor.fromFullName(prefferedMajor);
                  
 		    	   Student student = new Student(id, name, email, "password", major, year);
	               studentList.add(student);
                }
                else if(userType == UserType.CCSTAFF && values.length == 5)
                {
                   String id = values[0].trim();
  		    	   String name = values[1].trim();
  		    	   String role = values[2].trim();
  		    	   String department = values[3].trim();
  		    	   String email = values[4].trim();
  		    	   
  		    	   CareerCenterStaff staff = new CareerCenterStaff(id, name, email, "password", role, department);
  		    	   staffList.add(staff);
                }
                else if(userType == UserType.COMREP && values.length == 7)
                {
                   String repId = values[0].trim();
                   String name = values[1].trim();
                   String companyName = values[2].trim();
                   String department = values[3].trim();
                   String position = values[4].trim();
                   String email = values[5].trim();
                   String statusValue = values[6].trim().toUpperCase();
                   
                   UserAccountStatus status = UserAccountStatus.fromString(statusValue);
                  
                   ArrayList<Internship> comrepInternships = App.internshipList.stream()
                		   .filter(i -> i.getCompanyRep().toLowerCase().equals(repId.toLowerCase()))
                		   .collect(Collectors.toCollection(ArrayList::new));
                   
                   CompanyRep registration = new CompanyRep(repId, name, email, "password", companyName, department, position, status, comrepInternships);
                   compRepList.add(registration);
                }
                else
                {
                	if(values.length == 11)
                	{
                		// Internship list
                    	String internshipId = values[0].trim();
                        String title = values[1].trim();
                        String description = values[2].trim();
                        String level = values[3].trim();
                        String preferredMajor = values[4].trim();
                        String openDate = values[5].trim();
                        String closeDate = values[6].trim();
                        String status = values[7].trim();
                        //String companyName = values[8].trim(); // TODO: add this field in Check Internship Application Status
                        String companyRep = values[9].trim();
                        String slots = values[10].trim();
                        
                        InternshipLevel internshipLevel = InternshipLevel.fromString(level);
                        
                        StudentMajor major = StudentMajor.fromFullName(preferredMajor);
                        
                        LocalDate appOpenDate = parseDate(openDate);
                        LocalDate appCloseDate = parseDate(closeDate);
                        
                        InternshipStatus appStatus = InternshipStatus.fromString(status);
                        int numOfSlots = Integer.parseInt(slots);
                        
                        Internship internship = new Internship(internshipId, title, description, 
                        		internshipLevel, major, appOpenDate, appCloseDate,
                        		appStatus, companyRep, numOfSlots);
                        internshipList.add(internship);
                	}
                }
            }
        }
        catch (FileNotFoundException fe) {
        	System.out.println("CSV file not found!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void WriteToCSV(String fileName, UserType userType) {
        String csvFile = fileName;
        
        try (FileWriter writer = new FileWriter(csvFile)) {
        	
        	if(userType == UserType.STUDENT)
        	{
        		// Header
                writer.append("StudentID,Name,Major,Year,Email\n");
                // Need to include old data
                for (Student student : studentList) {
                	String userID = student.getUserID();
                	String name = student.getName();
                	String major = student.getMajor().getFullName();
                	int year = student.getYear();
                	String email = student.getEmail();
                	writer.append(userID + "," + name + "," + major + "," + year + "," + email + "\n");
                }
        	}
        	else if(userType == UserType.CCSTAFF)
        	{
        		// Header
                writer.append("StaffID,Name,Role,Department,Email\n");
                for (CareerCenterStaff staff : staffList) {
                	String userID = staff.getUserID();
                	String name = staff.getName();
                	String role = staff.getRole();
                	String department = staff.getDepartment();
                	String email = staff.getEmail();
                	writer.append(userID + "," + name + "," + role + "," + department + "," + email + "\n");
                }
        	}
            writer.flush();
            System.out.println("CSV file written successfully!");
        } 
        catch (FileNotFoundException fe) {
        	System.out.println("CSV file not found!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void LoadEnvironmentVariables() {
		 Properties prop = new Properties();
         try (InputStream input = App.class.getClassLoader().getResourceAsStream("application.properties")) {
             if (input == null) {
                 System.out.println("Unable to find application.properties file.");
                 return;
             }
             prop.load(input);
             
             envFilePathStudent = prop.getProperty("filepath.student");
             envFilePathStaff = prop.getProperty("filepath.staff");
             envFilePathRep = prop.getProperty("filepath.rep");
             envFilePathInternship = prop.getProperty("filepath.internship");
         } 
         catch (IOException ex) {
             ex.printStackTrace();
         }
	}
	
	public static void printSectionTitle(String title) {
		System.out.println("===== " + title + " =====");
	}
	
	public static void printSectionTitle(String title, boolean marginTop) {
		if (marginTop == true)
		{
			System.out.println();
		}
		System.out.println("===== " + title + " =====");
	}
	
	public static String promptFormInput(String label) {
		while (true) {
			System.out.println(label + ":");
			String input = App.sc.nextLine().trim();
			if ("0".equals(input)) {
				System.out.println("Cancelled.");
				return null;
			}
			if (!input.isEmpty()) {
				return input;
			}
			System.out.println("Input cannot be empty. Please try again.");
		}
	}
	
	public static String generateUniqueId(String prefix, String[] ids) {
		Random random = new Random();
		
		String base = prefix.toUpperCase().replaceAll("[^A-Z]", "");
		if (base.isEmpty()) {
			return null;
		}

		for (int attempt = 0; attempt < 200; attempt++) {
			int number = random.nextInt(9000) + 1000;
			String candidate = base + number;
			if (!idExists(candidate, ids)) {
				return candidate;
			}
		}

		// Fallback incremental approach
		int suffix = 1;
		String candidate = base + suffix;
		while (idExists(candidate, ids)) {
			suffix++;
			candidate = base + suffix;
		}
		return candidate;
	}

	private static boolean idExists(String candidate, String[] ids) {
		return Arrays.stream(ids).anyMatch(id -> id.equalsIgnoreCase(candidate));
	}
	
	private static LocalDate parseDate(String text) {
	    DateTimeFormatter[] formatters = new DateTimeFormatter[] {
	        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
	        DateTimeFormatter.ofPattern("dd MM yyyy"),
	        DateTimeFormatter.ofPattern("d M yyyy") // handles single-digit days/months
	    };
	    for (DateTimeFormatter f : formatters) {
	        try {
	            return LocalDate.parse(text.trim(), f);
	        } 
	        catch (Exception ignored) 
	        {
	        }
	    }
	    throw new IllegalArgumentException("Unrecognized date format: " + text);
	}
}
