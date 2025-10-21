package team5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import team5.enums.UserType;

public class App {
	
	// Global Scanner for the whole program
    public static Scanner sc = new Scanner(System.in);
	// Student List
	public static ArrayList<Student> studentList = new ArrayList<>();
	// Staff List
	public static ArrayList<CareerCenterStaff> staffList = new ArrayList<>();
	// CompanyRepresentatives List
	public static ArrayList<CompanyRep> compRepList = new ArrayList<>();
	// CompanyRepresentatives List
	public static ArrayList<Internship> internshipList = new ArrayList<>();
	
	public static User currentUser = null;
	
	public static void main(String[] args) {
		System.out.println("===== Internship System =====");
		
		// Read from CSV files
		studentList.clear();
		staffList.clear();
		ReadFromCSV("InternshipSystem/src/sample_student_list.csv", UserType.STUDENT);
        /*for (Student student : studentList) {
            System.out.println(student);
        }
		ReadFromCSV("InternshipSystem/src/sample_staff_list.csv", UserType.CCSTAFF);
        for (CareerCenterStaff staff : staffList) {
            System.out.println(staff);
        }*/
        
        System.out.println("===== Login =====");
        try 
        {
			// Choose login type
			System.out.println("Please choose your user type:");
			System.out.println("1: Student");	
			System.out.println("2: Career Center Staff");	
			System.out.println("3: Company Representatives");	
			int choice = Integer.parseInt(App.sc.nextLine()); // consume the newline
			if(choice <= 0 || choice > 3)
			{
				System.out.println("Invalid user type.");	
				return;
			}
			
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
				return;
			}
			if(currentUser == null)
			{
				//System.out.println("Login Failed.");	
				return;
			}
			System.out.println("Login Successfully. Welcome " + currentUser.getName());
			
			// Display menu based on UserType
			
			
			//currentUser.changePassword();
        } 
        catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number for user type.");
        } 
        catch (Exception e) {
            System.out.println("An error occurred during login: " + e.getMessage());
            e.printStackTrace();
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
}
