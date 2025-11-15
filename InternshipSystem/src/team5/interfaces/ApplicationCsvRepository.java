package team5.interfaces;

import java.util.ArrayList;

import team5.InternshipApplication;

/**
 * Interface to persist internship applications
 */
public interface ApplicationCsvRepository {
	boolean writeInternshipApplications(ArrayList<InternshipApplication> internshipApplications);
	
	boolean appendInternshipApplicationToCsv(InternshipApplication internship);
}
