package team5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import team5.controllers.CareerCenterStaffController;
import team5.controllers.CompanyRepController;
import team5.controllers.StudentController;
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
	public static Map<StudentMajor, String> studentMajors = new HashMap<>();
	
	public static void main(String[] args) {
		// Initialise stuff
		LoadEnvironmentVariables();
		InitialiseStudentMajors();
		
		// Read from CSV files
		studentList.clear();
		staffList.clear();
		compRepList.clear();
		ReadFromCSV(envFilePathStudent, UserType.STUDENT);
		ReadFromCSV(envFilePathStaff, UserType.CCSTAFF);
		ReadFromCSV(envFilePathRep, UserType.COMREP);
		
		printSectionTitle("Welcome to Internship System");
        
        boolean exitProgram = false;
        while (!exitProgram) {
        	printSectionTitle("Login");
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
				if(choice == 0)
				{
					System.out.println("EXIT");
					exitProgram = true;
					continue;
				}
				if(choice < 0 || choice > 4)
				{
					System.out.println("Invalid user type.");	
					continue;
				}

				if(choice == 4)
				{
					CompanyRepRegistrationHandler registrationHandler = new CompanyRepRegistrationHandler();
					registrationHandler.startRegistration();
					continue;
				}
				//currentCompanyRep = null;
				
				System.out.println("Please enter your user ID:");	
				String userID = App.sc.nextLine();
				
				System.out.println("Please enter your password:");	
				String password = App.sc.nextLine();
				
				boolean foundUser = false;
				UserType userType = UserType.NONE;
				if(choice == 1)
				{
					userType = UserType.STUDENT;
				}
				else if(choice == 2)
				{
					userType = UserType.CCSTAFF;
				}
				else 
				{
					userType = UserType.COMREP;
				}
				foundUser = verifyUserFromList(userType, userID, password);
				if(!foundUser)
				{
					System.out.println("User ID not found.");	
					continue;
				}
				if(currentUser == null)
				{
					//System.out.println("Login Failed.");	
					continue;
				}
				System.out.println("Login successful. Welcome " + currentUser.getName());
				
				// Display menu based on UserType
				if (userType == UserType.STUDENT && currentUser instanceof Student) {
					StudentController studentController = new StudentController();
					studentController.showMenu((Student) currentUser);
					//currentUser = null;
				} else if (userType == UserType.CCSTAFF && currentUser instanceof CareerCenterStaff) {
					CareerCenterStaffController staffController = new CareerCenterStaffController();
					staffController.showMenu((CareerCenterStaff) currentUser);
					//currentUser = null;
				} else if (userType == UserType.COMREP && currentUser instanceof CompanyRep) {
					CompanyRepController companyRepController = new CompanyRepController();
					companyRepController.showMenu((CompanyRep) currentUser);
					//currentUser = null;
					//currentCompanyRep = null;
				}
				
				//currentUser.changePassword();
	        } 
	        catch (NumberFormatException e) {
	            System.out.println("Invalid input! Please enter a number for user type.");
	        } 
	        catch (Exception e) {
	            System.out.println("An error occurred during login: " + e.getMessage());
	            e.printStackTrace();
	        }
        }
        
        //WriteToCSV("InternshipSystem/src/sample_student_list.csv", UserType.STUDENT);
        //WriteToCSV("InternshipSystem/src/sample_staff_list.csv", UserType.CCSTAFF);
	}
	
	// Helper
	public static boolean verifyUserFromList(UserType userType, String userID, String password)
	{ 
		if(userType == UserType.STUDENT)
		{
			for (Student student : studentList) 
			{
	            String id = student.getUserID();
	            String pw = student.getPassword();
	            if(id.equals(userID))
	            {
	            	if(pw.equals(password))
	            	{
	            		currentUser = student;
	            		student.login();
	            	}
	            	else
	            	{
	            		System.out.println("Password wrong, please enter again:");
	            		String newPw = App.sc.nextLine();
	            		if(pw.equals(newPw))
	            		{
	            			currentUser = student;
	            			student.login();
	            		}
	            		else
	            		{
	            			 System.out.println("Wrong password again. Login Failed");
	            		}
	            	}
	            	return true;
	            }
			}
		}
		else if(userType == UserType.CCSTAFF)
		{
			for (CareerCenterStaff staff : staffList) 
			{
	            String id = staff.getUserID();
	            String pw = staff.getPassword();
	            if(id.equals(userID))
	            {
	            	if(pw.equals(password))
	            	{
	            		currentUser = staff;
	            		staff.login();
	            	}
	            	else
	            	{
	            		System.out.println("Password wrong, please enter again:");
	            		String newPw = App.sc.nextLine();
	            		if(pw.equals(newPw))
	            		{
	            			currentUser = staff;
	            			staff.login();
	            		}
	            		else
	            		{
	            			 System.out.println("Wrong password again. Login Failed");
	            		}
	            	}
	            	return true;
	            }
			}
		}
		else if(userType == UserType.COMREP)
		{
			for (CompanyRep rep : compRepList) {
				String repId = rep.getUserID();
				if(repId.equalsIgnoreCase(userID))
				{
					if(rep.getAccountStatus() != UserAccountStatus.APPROVED)
					{
						System.out.println("Your account is not approved yet. Please contact the career center staff.");
						return true;
					}
					
					String expectedPassword = "password";
					if(expectedPassword.equals(password))
					{
						currentUser = rep;
						currentUser.setUserType(UserType.COMREP);
						currentUser.login();
						//currentCompanyRep = registration;
					}
					else
					{
						System.out.println("Password wrong, please enter again:");
	            		String newPw = App.sc.nextLine();
	            		if(expectedPassword.equals(newPw))
	            		{
	            			currentUser = rep;
	            			currentUser.login();
	            		}
	            		else
	            		{
	            			 System.out.println("Wrong password again. Login Failed");
	            		}
					}
					return true;
				}
			}
		}
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
 		    	   String major = values[2].trim();
 		    	   int year = Integer.parseInt(values[3].trim());
 		    	   String email = values[4].trim();
 		    	   
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
                   UserAccountStatus status;
                   try {
                	   status = UserAccountStatus.valueOf(statusValue);
                   } catch (IllegalArgumentException ex) {
                	   status = UserAccountStatus.PENDING;
                   }
                  
                   CompanyRep registration = new CompanyRep(repId, name, email, "password", companyName, department, position, status);
                   compRepList.add(registration);
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
                	String major = student.getMajor();
                	int year = student.getYear();
                	String email = student.getEmail();
                	writer.append(userID + "," + name + "," + major + "," + year + "," + email + "\n");
                }
                // Testing
                //writer.append("U2310006F,Pang Wei Jie,Computing,1,pang0270@e.ntu.edu.sg\n");
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
                // Testing
                //writer.append("chan004,Mr. Chan Fa Long,Career Center Staff,CCDS,chan004@ntu.edu.sg\n");
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
                 System.out.println("Unable to find src/application.properties");
                 return;
             }
             prop.load(input);
             
             envFilePathStudent = prop.getProperty("filepath.student");
             envFilePathStaff = prop.getProperty("filepath.staff");
             envFilePathRep = prop.getProperty("filepath.rep");
             envFilePathInternship = prop.getProperty("filepath.internship");
         } catch (IOException ex) {
             ex.printStackTrace();
         }
	}
	
	public static void InitialiseStudentMajors() {
		studentMajors.put(StudentMajor.CS, "Computer Science");
		studentMajors.put(StudentMajor.DSAI, "Data Science & AI");
		studentMajors.put(StudentMajor.CE, "Computer Engineering");
		studentMajors.put(StudentMajor.IEM, "Information Engineering & Media");
		studentMajors.put(StudentMajor.COMP, "Computing");
        
	}
	
	public static void printSectionTitle(String title) {
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
		return Arrays.stream(ids)
				.anyMatch(id -> id.equalsIgnoreCase(candidate));
	}
}
