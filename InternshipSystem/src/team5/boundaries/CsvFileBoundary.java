package team5.boundaries;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import team5.App;
import team5.Internship;
import team5.InternshipApplication;
import team5.interfaces.FileBoundary;

public class CsvFileBoundary implements FileBoundary {
	
	public CsvFileBoundary() { }
	
	public boolean writeInternship(ArrayList<Internship> internships) {
        try (FileWriter writer = new FileWriter(App.envFilePathInternship)) {
    		// Header
            writer.append("InternshipID,Title,Description,Level,PreferredMajor,OpenDate,CloseDate,Status,CompanyName,CompanyRep,Slots\n");
            // Rows
            for (Internship internship : internships) {
            	writer.append(buildInternshipString(internship).toString());
            }
            writer.flush();
            return true;
        }
        catch (IOException e) {
        	System.out.println("Failed to save to file: " + e.getMessage());
        	System.out.println("Stack trace:");
        	e.printStackTrace();
            return false;
            
        }
        
	}
	
	public boolean appendInternship(Internship internship) {
		try (FileWriter writer = new FileWriter(App.envFilePathInternship, true)) {
			writer.append(buildInternshipString(internship).toString());
			writer.flush();
			return true;
		} catch (IOException e) {
			System.out.println("Failed to save to file: " + e.getMessage());
        	System.out.println("Stack trace:");
        	e.printStackTrace();
			return false;
		}
	}
	
	public boolean writeInternshipApplications(ArrayList<InternshipApplication> internshipApplications) {
        try (FileWriter writer = new FileWriter(App.envFilePathInternshipApplication)) {
    		// Header
            writer.append("ApplicationID,InternshipID,StudentName,ApplicationDate,Status\n");
            // Rows
            for (InternshipApplication internshipApplication : internshipApplications) {
            	writer.append(buildInternshipApplicationString(internshipApplication).toString());
            }
            writer.flush();
            return true;
        }
        catch (IOException e) {
        	System.out.println("Failed to save to file: " + e.getMessage());
        	System.out.println("Stack trace:");
        	e.printStackTrace();
            return false;
            
        }
        
	}
	
	public boolean appendInternshipApplicationToCsv(InternshipApplication internship) {
		try (FileWriter writer = new FileWriter(App.envFilePathInternshipApplication, true)) {
			writer.append(buildInternshipApplicationString(internship).toString());
			writer.flush();
			return true;
		} 
		catch (IOException e) {
			System.out.println("Failed to save to file: " + e.getMessage());
        	System.out.println("Stack trace:");
        	e.printStackTrace();
			return false;
		}
	}
	
	private static StringBuilder buildInternshipString(Internship internship) {
		StringBuilder sb = new StringBuilder();
		sb.append(internship.getInternshipId()).append(",")
		  .append(internship.getTitle()).append(",")
		  .append(internship.getDescription()).append(",")
		  .append(internship.getInternshipLevel()).append(",")
		  .append(internship.getPreferredMajor().getFullName()).append(",")
		  .append(internship.getApplicationOpenDate().format(App.DATE_DB_FORMATTER)).append(",")
		  .append(internship.getApplicationCloseDate().format(App.DATE_DB_FORMATTER)).append(",")
		  .append(internship.getInternshipStatus()).append(",")
		  .append(internship.getCompanyName()).append(",")
		  .append(internship.getCompanyRep()).append(",")
		  .append(internship.getNumOfSlots()).append("\n");
		return sb;
	}
	
	private static StringBuilder buildInternshipApplicationString(InternshipApplication internshipApplication) {
		StringBuilder sb = new StringBuilder();
		sb.append(internshipApplication.getApplicationId()).append(",")
		  .append(internshipApplication.getInternshipInfo().getInternshipId()).append(",")
		  .append(internshipApplication.getStudentInfo().getName()).append(",")
		  .append(internshipApplication.getAppliedDate().format(App.DATE_DB_FORMATTER)).append(",")
		  .append(internshipApplication.getStatus()).append("\n");
		return sb;
	}
}
