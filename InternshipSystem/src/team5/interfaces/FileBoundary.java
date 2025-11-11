package team5.interfaces;

import java.util.ArrayList;

import team5.Internship;
import team5.InternshipApplication;

public interface FileBoundary {
	boolean writeInternship(ArrayList<Internship> internships);
	
	boolean appendInternship(Internship internship);
	
	boolean writeInternshipApplications(ArrayList<InternshipApplication> internshipApplications);
	
	boolean appendInternshipApplicationToCsv(InternshipApplication internship);
}
