package team5.interfaces;

import java.util.ArrayList;

import team5.InternshipApplication;

public interface ApplicationCsvRepository {
	boolean writeInternshipApplications(ArrayList<InternshipApplication> internshipApplications);
	
	boolean appendInternshipApplicationToCsv(InternshipApplication internship);
}
