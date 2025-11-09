package team5.interfaces;

import java.util.ArrayList;

import team5.Internship;

public interface FileBoundary {
	boolean writeInternship(ArrayList<Internship> internships);
	
	boolean appendInternship(Internship internship);
}
