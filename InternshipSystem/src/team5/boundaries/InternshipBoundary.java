package team5.boundaries;

import java.util.ArrayList;

import team5.App;
import team5.Internship;

public class InternshipBoundary extends ConsoleBoundary {
	
	public InternshipBoundary() { }
	
	public static void displayInternshipList(ArrayList<Internship> internshipList) {
		for (int i = 0; i < internshipList.size(); i++) {
			Internship internship = internshipList.get(i);
			System.out.printf("%d. Internship ID: %s | Title: %s | Level: %s | Status: %s%n",
					i + 1,
					internship.getInternshipId(),
					safeValue(internship.getTitle()),
					valueOrNA(internship.getInternshipLevel()),
					valueOrNA(internship.getInternshipStatus()));
		}
	}
	
	public static void displayInternshipDetails(Internship internship) {
		App.printSectionTitle("Internship Details", true);
		System.out.println("Internship ID: \t\t" + internship.getInternshipId());
		System.out.println("Title: \t\t\t" + safeValue(internship.getTitle()));
		System.out.println("Description: \t\t" + safeValue(internship.getDescription()));
		System.out.println("Level: \t\t\t" + valueOrNA(internship.getInternshipLevel()));
		System.out.println("Preferred Majors: \t" + safeValue(internship.getPreferredMajor()));
		System.out.println("Application Open Date: \t" + safeValue(internship.getApplicationOpenDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Application Close Date: " + safeValue(internship.getApplicationCloseDate().format(App.DATE_DISPLAY_FORMATTER)));
		System.out.println("Status: \t\t" + valueOrNA(internship.getInternshipStatus()));
		System.out.println("Number of Slots: \t" + internship.getNumOfSlots());
	}
	
	
}
