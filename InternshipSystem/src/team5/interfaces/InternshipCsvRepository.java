package team5.interfaces;

import java.util.ArrayList;

import team5.Internship;

public interface InternshipCsvRepository {
	boolean writeInternship(ArrayList<Internship> internships);
	
	boolean appendInternship(Internship internship);
}
