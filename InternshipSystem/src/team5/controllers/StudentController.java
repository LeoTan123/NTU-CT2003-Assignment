package team5.controllers;

import team5.App;
import team5.enums.UserType;
import team5.Student;
import team5.boundaries.ConsoleBoundary;
import team5.studentactions.CheckApplicationStatusAction;
import team5.studentactions.StudentAction;
import team5.studentactions.ViewInternshipsAction;

public class StudentController {

	private final StudentAction viewInternshipsAction;
	private final StudentAction checkApplicationStatusAction;

	public StudentController() {
		this.viewInternshipsAction = new ViewInternshipsAction();
		this.checkApplicationStatusAction = new CheckApplicationStatusAction();
	}

    public void showMenu(Student student) {
        boolean exit = false;
        while (!exit) {
        	ConsoleBoundary.printSectionTitle("Student Menu", true);
            System.out.println("1. View Internships");
            System.out.println("2. Check Internship Application Status");
            System.out.println("3. Update Password");
            System.out.println("4. Logout");
            String input = ConsoleBoundary.promptUserInput(true);
            switch (input) {
                case "1":
                    viewInternshipsAction.run(student);
                    break;
                case "2":
                    checkApplicationStatusAction.run(student);
                    break;
                case "3":
                    boolean updated = student.changePassword();
                    if (updated) {
                    	boolean ok = App.updatePasswordAndPersist(
                    			UserType.STUDENT,
                    			student.getUserID(),
                    			student.getPassword());
                    	if (ok) {
                    		System.out.println("password saved to CSV.");
                    		student.logout();
                    		System.out.println("Please log in again with your new password.");
                    		exit = true;
                    	}else {
                    		System.out.println("Failed to save new password to CSV.");
                    	}
                    }
                    break;
                case "4":
                    student.logout();
                    exit = true;
                    break;
                default:
                   ConsoleBoundary.printInvalidSelection();
            }
        }
    }
}
