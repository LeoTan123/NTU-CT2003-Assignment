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
		// Initialize stuff
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
	    if (userType == UserType.COMREP) {
	    	CompanyRep rep = (CompanyRep) foundUser;
	    	UserAccountStatus status = rep.getAccountStatus();
	    	if (status == UserAccountStatus.PENDING) {
	    		System.out.println("Your account is pending approval. Please wait for the career center staff to review it.");
	    		return false;
	    	}
	    	if (status == UserAccountStatus.REJECTED) {
	    		System.out.println("Your registration was rejected. Please submit a new registration request for review.");
	    		return false;
	    	}
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
                if(userType == UserType.STUDENT && values.length == 6)
                {
                   String id = values[0].trim();
 		    	   String name = values[1].trim();
 		    	   String prefferedMajor = values[2].trim();
 		    	   int year = Integer.parseInt(values[3].trim());
 		    	   String email = values[4].trim();
 		    	   
 		    	   String pwd = (values.length >= 6) ? values[5].trim(): "password";
 		    	   
 		    	   StudentMajor major = StudentMajor.fromFullName(prefferedMajor);
                  
 		    	   Student student = new Student(id, name, email, pwd, major, year);
	               studentList.add(student);
                }
                else if(userType == UserType.CCSTAFF && values.length == 5 || values.length == 6)
                {
                   String id = values[0].trim();
  		    	   String name = values[1].trim();
  		    	   String role = values[2].trim();
  		    	   String department = values[3].trim();
  		    	   String email = values[4].trim();
  		    	   
  		    	   String pwd = (values.length >= 6) ? values[5].trim(): "password";
  		    	   
  		    	 
  		    	   CareerCenterStaff staff = new CareerCenterStaff(id, name, email, pwd, role, department);
  		    	   staffList.add(staff);
                }
                else if(userType == UserType.COMREP && values.length == 7 || values.length == 8)
                {
                   String repId = values[0].trim();
                   String name = values[1].trim();
                   String companyName = values[2].trim();
                   String department = values[3].trim();
                   String position = values[4].trim();
                   String email = values[5].trim();
                   String statusValue = values[6].trim().toUpperCase();
                   
                   String pwd = (values.length >=8) ? values[7].trim(): "password";
                   
                   UserAccountStatus status = UserAccountStatus.fromString(statusValue);
                  
                   ArrayList<Internship> comrepInternships = App.internshipList.stream()
                		   .filter(i -> i.getCompanyRep().toLowerCase().equals(repId.toLowerCase()))
                		   .collect(Collectors.toCollection(ArrayList::new));
                   
                   CompanyRep registration = new CompanyRep(repId, name, email, pwd, companyName, department, position, status, comrepInternships);
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
                writer.append("StudentID,Name,Major,Year,Email,Password\n");
                // Need to include old data
                for (Student student : studentList) {
                	writer.append(student.getUserID()).append(",")
                    .append(student.getName()).append(",")
                    .append(student.getMajor().getFullName()).append(",")
                    .append(String.valueOf(student.getYear())).append(",")
                    .append(student.getEmail()).append(",")
                    .append(student.getPassword()).append("\n");
                	
                }
        	}
        	else if(userType == UserType.CCSTAFF)
        	{
        		// Header
                writer.append("StaffID,Name,Role,Department,Email\n");
                for (CareerCenterStaff staff : staffList) {
                	writer.append(staff.getUserID()).append(",")
					.append(staff.getName()).append(",")
					.append(staff.getRole()).append(",")
					.append(staff.getDepartment()).append(",")
					.append(staff.getEmail()).append(",")
					.append(staff.getPassword()).append("\n");
					
                }
        	}
        	else if(userType == UserType.COMREP)
        		// Header
        		writer.append("repID, Name, Company, Department, Position, Email, Status, Password\n");
        		for (CompanyRep companyRep : compRepList) {
        			writer.append(companyRep.getUserID()).append(",")
					.append(companyRep.getName()).append(",")
					.append(companyRep.getCompanyName()).append(",")
					.append(companyRep.getDepartment()).append(",")
					.append(companyRep.getPosition()).append(",")
					.append(companyRep.getEmail()).append(",")
					.append(companyRep.getAccountStatus().name()).append(",")
					.append(companyRep.getPassword()).append("\n");
        			
        		}
            writer.flush();
            System.out.println("CSV file written successfully!");
        } 
        catch (FileNotFoundException fe) {
        	System.out.println("CSV file not found!");
        }
        catch (IOException e) {
        	System.out.println("Failed to save to file: " + e.getMessage());
        	System.out.println("Stack trace:");
        	e.printStackTrace();
        }
	}
	public static boolean updatePasswordAndPersist(UserType type, String userId, String newPassword) {
	    User target = null;

	    switch (type) {
	        case STUDENT:
	            for (Student student : studentList)
	                if (student.getUserID().equals(userId)) { target = student; break; }
	            break;

	        case CCSTAFF:
	            for (CareerCenterStaff staff : staffList)
	                if (staff.getUserID().equals(userId)) { target = staff; break; }
	            break;

	        case COMREP:
	            for (CompanyRep companyRep : compRepList)
	                if (companyRep.getUserID().equalsIgnoreCase(userId)) { target = companyRep; break; }
	            break;
	    }

	    if (target == null) return false;

	    // ✅ update in memory
	    target.setPassword(newPassword);

	    // ✅ save to correct CSV
	    String path = (type == UserType.STUDENT) ? envFilePathStudent :
	                  (type == UserType.CCSTAFF) ? envFilePathStaff :
	                  (type == UserType.COMREP) ? envFilePathRep : null;

	    if (path == null) return false;

	    WriteToCSV(path, type);

	    return true;
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
