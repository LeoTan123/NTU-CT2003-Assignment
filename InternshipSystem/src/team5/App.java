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
import team5.boundaries.ConsoleBoundary;
import team5.controllers.CareerCenterStaffController;
import team5.controllers.CompanyRepController;
import team5.controllers.StudentController;
import team5.enums.CSVType;
import team5.enums.InternshipApplicationStatus;
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
	// Internship application List
	public static ArrayList<InternshipApplication> internshipApplicationList = new ArrayList<>();
	// Current user
	public static User currentUser = null;
	
	// System variables
	public static String envFilePathStudent;
	public static String envFilePathStaff;
	public static String envFilePathRep;
	public static String envFilePathInternship;
	public static String envFilePathInternshipApplication;
	public static DateTimeFormatter DATE_DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static DateTimeFormatter DATE_DB_FORMATTER = DateTimeFormatter.ofPattern("dd MM yyyy");
	
	/**
	 * 	Main function to run the whole program
	 * @param args
	 */
	public static void main(String[] args) {
		// Initialize stuff
		loadEnvironmentVariables();
		
		ConsoleBoundary.printSectionTitle("Welcome to Internship System");
        
        boolean exitProgram = false;
        while (!exitProgram) {
        	// Read from CSV files
    		studentList.clear();
    		staffList.clear();
    		compRepList.clear();
    		internshipList.clear();
    		internshipApplicationList.clear();
    		
    		// Load internship before CompanyRep so that CompanyRep can use the data
    		ReadFromCSV(envFilePathInternship, CSVType.Internship); 
    		ReadFromCSV(envFilePathStudent, CSVType.Student);
    		ReadFromCSV(envFilePathStaff, CSVType.CCStaff);
    		ReadFromCSV(envFilePathRep, CSVType.CompanyRep);
    		ReadFromCSV(envFilePathInternshipApplication, CSVType.InternshipApplication);
    		
        	ConsoleBoundary.printSectionTitle("Login", true);
	        try 
	        {
				// Choose login type
				System.out.println("Please choose your user type:");
				System.out.println("1: Student");	
				System.out.println("2: Career Center Staff");	
				System.out.println("3: Company Representatives");	
				System.out.println("4: Register Company Representative");
				System.out.println("0: Exit");
				
				int choice = Integer.parseInt(ConsoleBoundary.promptUserInput()); // consume the newline
				if(choice == 0) {
					System.out.println("Exitting system...");
					exitProgram = true;
					break;
				}
				if(choice < 0 || choice > 4) {
					ConsoleBoundary.printInvalidSelection();	
					continue;
				}

				if(choice == 4) {
					CompanyRepRegistrationHandler registrationHandler = new CompanyRepRegistrationHandler();
					registrationHandler.startRegistration();
					continue;
				}
				
				System.out.println("Please enter your user ID:");	
				String userID = ConsoleBoundary.promptUserInput();
				
				System.out.println("Please enter your password:");	
				String password = ConsoleBoundary.promptUserInput();
				
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
						ConsoleBoundary.printInvalidSelection();
				}

				boolean foundUser = verifyUserFromList(userType, userID, password);
				if(!foundUser || currentUser == null){
					continue;
				}

				System.out.println("Login successful. Welcome " + currentUser.getName() + ".");
				
				// Display menu based on UserType
				if (userType == UserType.STUDENT && currentUser instanceof Student) {
					StudentController studentController = new StudentController();
					studentController.showMenu((Student)currentUser);
				} 
				else if (userType == UserType.CCSTAFF && currentUser instanceof CareerCenterStaff) {
					CareerCenterStaffController staffController = new CareerCenterStaffController();
					staffController.showMenu((CareerCenterStaff)currentUser);
				} 
				else if (userType == UserType.COMREP && currentUser instanceof CompanyRep) {
					CompanyRepController companyRepController = new CompanyRepController();
					companyRepController.showMenu((CompanyRep)currentUser);
				}
	        } 
	        catch (NumberFormatException e) {
	            ConsoleBoundary.printInvalidInput();
	        } 
	        catch (Exception e) {
	        	ConsoleBoundary.printErrorMessage();
	            e.printStackTrace();
	        }
        }
	}
	
	/**
	 * To verify user type using userID and password
	 * @param userType: User type from user input
	 * @param userID: User ID
	 * @param password: User password
	 * @return true for found user and login successfully, else false
	 */
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
				System.out.println("Invalid User Type: " + userType);
	            return false;	
		}
		
		// User not found in either lists
		if (foundUser == null) {
			System.out.println("User ID not found in system: " + userID);
	        return false;
	    }
		
		// Special check for COMREP account status
	    if (userType == UserType.COMREP) {
	    	CompanyRep rep = (CompanyRep)foundUser;
	    	UserAccountStatus status = rep.getAccountStatus();
	    	if (status == UserAccountStatus.PENDING) {
	    		System.out.println("Your account is pending approval. Please wait for the career center staff to review.");
	    		return false;
	    	}
	    	else if (status == UserAccountStatus.REJECTED) {
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
	                password = ConsoleBoundary.promptUserInput(); // update password for next attempt
	            } 
	            else {
	                System.out.println("You have entered wrong password for 3 times. Login failed.");
	            }
	        }
	    }
	    // Failed after 3 attempts
	    return false;
	}
	
	/**
	 * To read database from CSV file
	 * @param fileName: CSV file path, written in application.properties
	 * @param type: CSV file type
	 */
	public static void ReadFromCSV(String fileName, CSVType type)
	{
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        	// Skip header
        	br.readLine();
        	
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if(type == CSVType.Student && values.length == 6)
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
                else if(type == CSVType.CCStaff && values.length == 6)
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
                else if(type == CSVType.CompanyRep && values.length == 8)
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
                else if(type == CSVType.Internship && values.length == 11)
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
                    String companyName = values[8].trim();
                    String companyRep = values[9].trim();
                    String slots = values[10].trim();
                    
                    InternshipLevel internshipLevel = InternshipLevel.fromString(level);
                    
                    StudentMajor major = StudentMajor.fromFullName(preferredMajor);
                    
                    LocalDate appOpenDate = ConsoleBoundary.parseDate(openDate);
                    LocalDate appCloseDate = ConsoleBoundary.parseDate(closeDate);
                    
                    InternshipStatus appStatus = InternshipStatus.fromString(status);
                    int numOfSlots = Integer.parseInt(slots);
                    
                    Internship internship = new Internship(internshipId, title, description, 
                    		internshipLevel, major, appOpenDate, appCloseDate,
                    		appStatus, companyName, companyRep, numOfSlots);
                    internshipList.add(internship);
                }
                else if(type == CSVType.InternshipApplication && values.length == 5)
                {
                	String applicationId = values[0].trim();
                    String internshipId = values[1].trim();
                    String studentName = values[2].trim();
                    String appliedDate = values[3].trim();
                    String status = values[4].trim();
                    
                    LocalDate appDate = ConsoleBoundary.parseDate(appliedDate);
                    InternshipApplicationStatus appStatus = InternshipApplicationStatus.fromString(status);
                    
                    Student selectedStudent = null;
                    for(Student s: studentList)
                    {
                    	if(s.getName().equals(studentName)) {
                    		selectedStudent = s;
                    		break;
                    	}
                    }
                    if(selectedStudent == null) {
                    	return;
                    }
                    
                    Internship selectedInternship = null;
                    for(Internship internship: internshipList)
                    {
                    	if(internship.getInternshipId().equals(internshipId)) {
                    		selectedInternship = internship;
                    		break;
                    	}
                    }
                    if(selectedInternship == null) {
                    	return;
                    }
                    
                    InternshipApplication internshipApp = new InternshipApplication(applicationId, selectedInternship, selectedStudent, appDate, appStatus);
                    // Add to global internship application list
                    internshipApplicationList.add(internshipApp);
                    // Add to specific student's internship application
                    selectedStudent.addInternshipApplications(internshipApp);
                }
            }
        }
        catch (FileNotFoundException fe) {
        	System.out.println("CSV file path not found!");
        }
        catch (IOException e) {
        	ConsoleBoundary.printErrorMessage();
            e.printStackTrace();
        }
	}
	
	/**
	 * Update CSV file database
	 * @param fileName
	 * @param userType
	 */
	public static void WriteToCSV(String fileName, UserType userType) {
        try (FileWriter writer = new FileWriter(fileName)) {
        	if(userType == UserType.STUDENT)
        	{
        		// Header
                writer.append("StudentID,Name,Major,Year,Email,Password\n");
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
			{
        		// Header
        		writer.append("repID,Name,Company,Department,Position,Email,Status,Password\n");
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
			}
            writer.flush();
            //System.out.println("CSV file written successfully!");
        } 
        catch (FileNotFoundException fe) {
        	System.out.println("CSV file path not found!");
        }
        catch (IOException e) {
        	ConsoleBoundary.printErrorMessage();
        	e.printStackTrace();
        }
	}
	
	/**
	 * Update password and write to CSV
	 * @param type
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	public static boolean updatePasswordAndPersist(UserType type, String userId, String newPassword) {
	    User targetUser = null;
	    String path = null;
	    
	    switch (type) {
	        case STUDENT:
	        	path = envFilePathStudent;
	            for (Student student : studentList) {
	                if (student.getUserID().equals(userId)) { 
	                	targetUser = student; 
	                	break; 
	                }
	            }
	            break;
	        case CCSTAFF:
	        	path = envFilePathStaff;
	            for (CareerCenterStaff staff : staffList) {
	                if (staff.getUserID().equals(userId)) { 
	                	targetUser = staff; 
	                	break; 
	                }
	            }
	            break;
	        case COMREP:
	        	path = envFilePathRep;
	            for (CompanyRep companyRep : compRepList) {
	                if (companyRep.getUserID().equalsIgnoreCase(userId)) { 
	                	targetUser = companyRep; 
	                	break; 
	                }
	            }
	            break;
	        default:
	        	System.out.println("Invalid User Type: " + type);
	        	return false;
	    }

	    if (targetUser == null) {
	    	System.out.println("User ID not found in system: " + userId);
	    	return false;
	    }
	    
	    if (path == null) {
	    	System.out.println("CSV file path not found.");
	    	return false;
	    }
	    
	    targetUser.setPassword(newPassword);
	    WriteToCSV(path, type);
	    return true;
	}
	
	/**
	 * To load CSV file in application.properties
	 */
	public static void loadEnvironmentVariables() {
		 Properties prop = new Properties();
         try (InputStream input = App.class.getClassLoader().getResourceAsStream("application.properties")) {
             if (input == null) {
                 ConsoleBoundary.printText("application.properties not found!");
                 return;
             }
             prop.load(input);
             
             envFilePathStudent = prop.getProperty("filepath.student");
             envFilePathStaff = prop.getProperty("filepath.staff");
             envFilePathRep = prop.getProperty("filepath.rep");
             envFilePathInternship = prop.getProperty("filepath.internship");
             envFilePathInternshipApplication = prop.getProperty("filepath.internshipApplication");
         } 
         catch (IOException e) {
        	 ConsoleBoundary.printErrorMessage();
             e.printStackTrace();
         }
	}
	
	
	/**
	 * To generate random ID for ApplicationID and InternshipID
	 * @param prefix: Prefix to differentiate between ApplicationID and InternshipID
	 * @param ids: To check whether the ID already exists
	 * @return UniqueID string
	 */
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

	/**
	 * To check whether ID already exists
	 * @param candidate: current ID 
	 * @param ids: ID lists
	 * @return true if already exists, else false
	 */
	private static boolean idExists(String candidate, String[] ids) {
		return Arrays.stream(ids).anyMatch(id -> id.equalsIgnoreCase(candidate));
	}
}
