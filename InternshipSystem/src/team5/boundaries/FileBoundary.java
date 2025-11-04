package team5.boundaries;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import team5.App;
import team5.Internship;

public class FileBoundary {
	
	public FileBoundary() { }
	
	public static boolean writeInternshipToCSV(ArrayList<Internship> internships) {
        try (FileWriter writer = new FileWriter(App.envFilePathInternship)) {
    		// Header
            writer.append("InternshipID,Title,Description,Level,PreferredMajor,OpenDate,CloseDate,Status,CompanyName,CompanyRep,Slots\n");
            // Rows
            for (Internship internship : internships) {
            	writer.append(buildInternshipString(internship, "test").toString()); //TODO: add companyname to internship
            }
            writer.flush();
            return true;
        } 
        catch (FileNotFoundException fe) {
        	System.out.println("CSV file not found!");
        }
        catch (IOException e) {
            e.printStackTrace();
            
        }
        return false;
	}
	
	public static boolean appendInternshipToCsv(Internship internship, String companyName) {
		try (FileWriter writer = new FileWriter(App.envFilePathInternship, true)) {
			writer.append(buildInternshipString(internship, companyName).toString());
			writer.flush();
			return true;
		} catch (IOException e) {
			System.out.println("Failed to save to file: " + e.getMessage());
			return false;
		}
	}
	
	private static StringBuilder buildInternshipString(Internship internship, String companyName) {
		StringBuilder sb = new StringBuilder();
		sb.append(internship.getInternshipId()).append(",")
		  .append(internship.getTitle()).append(",")
		  .append(internship.getDescription()).append(",")
		  .append(internship.getInternshipLevel()).append(",")
		  .append(internship.getPreferredMajor().getFullName()).append(",")
		  .append(internship.getApplicationOpenDate().format(App.DATE_DB_FORMATTER)).append(",")
		  .append(internship.getApplicationCloseDate().format(App.DATE_DB_FORMATTER)).append(",")
		  .append(internship.getInternshipStatus()).append(",")
		  .append(companyName).append(",")
		  .append(internship.getCompanyRep()).append(",")
		  .append(internship.getNumOfSlots()).append("\n");
		return sb;
	}
}
