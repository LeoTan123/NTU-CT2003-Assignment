# Internship Management System - Personal Reference Guide
**Your Complete Understanding Guide for CT2003 Project**

---

## Table of Contents
1. [Project Overview - What Are We Building?](#1-project-overview)
2. [Understanding the Big Picture](#2-understanding-the-big-picture)
3. [Java Concepts Used (Learning Guide)](#3-java-concepts-used)
4. [Project Architecture](#4-project-architecture)
5. [How Users Actually Use the System (CLI Guide)](#5-how-users-use-the-system)
6. [Detailed Feature Walkthrough](#6-detailed-feature-walkthrough)
7. [Code Deep Dive - Understanding Key Functions](#7-code-deep-dive)
8. [Data Flow - How Everything Connects](#8-data-flow)
9. [Common Presentation Questions & Answers](#9-presentation-qa)
10. [Troubleshooting & Edge Cases](#10-troubleshooting)

---

## 1. Project Overview

### What Are We Building?

Imagine you're a university student looking for an internship. Right now, you might:
- Search online job boards
- Email companies individually
- Track applications in a spreadsheet
- Wonder if your application was received

**Our Internship Management System solves all this!**

It's a **Command-Line Interface (CLI) application** that manages the entire internship process for:
1. **Students** - Browse and apply for internships
2. **Companies** - Post internship opportunities
3. **Career Center Staff** - Oversee and approve everything

### Real-World Analogy

Think of it like **LinkedIn + Indeed + University Portal** combined, but simpler and focused only on internships for NTU students.

- **LinkedIn** (Company posting) = Company Representatives create internship posts
- **Indeed** (Job search) = Students browse and apply
- **University Portal** (Admin approval) = Staff approve companies and internships

### Why Does This Exist?

**Problems it solves:**
1. **For Students:** No more scattered applications across websites
2. **For Companies:** Easy way to reach NTU students specifically
3. **For Career Center:** One place to monitor all internship activities

### Technology Stack

- **Language:** Java (Object-Oriented Programming)
- **Interface:** CLI (Command-Line Interface) - text-based, no GUI
- **Data Storage:** CSV files (simple database alternative)
- **Runtime:** Runs on any machine with Java installed

---

## 2. Understanding the Big Picture

### The Complete Workflow

```
NEW COMPANY REPRESENTATIVE
        |
        v
1. Register account → Status: PENDING
        |
        v
2. Staff reviews → APPROVE or REJECT
        |
        v
3. If APPROVED → Can login
        |
        v
4. Create internship posting → Status: PENDING
        |
        v
5. Staff reviews internship → APPROVE or REJECT
        |
        v
6. If APPROVED → Visible to students
        |
        v
7. Student browses and applies → Application: PENDING
        |
        v
8. Company reviews → SUCCESSFUL or UNSUCCESSFUL
        |
        v
9. Student accepts offer → Becomes EMPLOYED
```

### Three Separate Worlds (User Types)

Think of the system as three separate "portals":

**Portal 1: Student Portal**
- View approved internships
- Apply for internships (max 3 at a time)
- Check application status
- Accept job offers

**Portal 2: Company Representative Portal**
- Create internship postings (max 5)
- Manage existing postings
- Update internship details

**Portal 3: Career Center Staff Portal**
- Review company registrations
- Approve/reject internships
- Monitor all activities

**Key Insight:** Each user type sees a completely different menu and has different permissions. This is called **Role-Based Access Control (RBAC)**.

---

## 3. Java Concepts Used (Learning Guide)

Let me explain the Java concepts in this project as if you're learning them for the first time.

### 3.1 Object-Oriented Programming (OOP)

**What is OOP?**
Instead of writing code as a long list of instructions, we organize it into "objects" that represent real-world things.

**Real-world analogy:**
- A **Student** in real life has: name, email, major, year
- A **Student object** in code has: name, email, major, year (as variables)
- A **Student** in real life can: apply for internships, check application status
- A **Student object** in code can: apply() method, checkStatus() method

**Four Pillars of OOP in Our Project:**

#### 1. **Encapsulation** (Hiding internal details)

```java
public class Student {
    private String name;  // Private - can't be accessed directly from outside

    public String getName() {  // Public - controlled access
        return this.name;
    }

    public void setName(String name) {  // Public - controlled modification
        this.name = name;
    }
}
```

**Why?** We don't want other code to directly change a student's name without validation. The getter/setter methods control access.

#### 2. **Inheritance** (Parent-child relationships)

```java
public class User {
    private String userID;
    private String name;
    private String password;

    public void login() { /* login logic */ }
    public void logout() { /* logout logic */ }
}

public class Student extends User {
    private StudentMajor major;  // Additional field specific to Student
    private int yearOfStudy;      // Additional field specific to Student

    // Inherits: userID, name, password, login(), logout()
    // Adds: major, yearOfStudy, and student-specific methods
}
```

**Why?** All users (Student, Staff, Company Rep) share common features (login, logout, password). We put shared code in **User** (parent class) and specific code in child classes.

**Think of it like:**
- **User** = General concept of "any person using the system"
- **Student, Staff, CompanyRep** = Specific types of users with unique features

#### 3. **Polymorphism** (Many forms)

```java
// All these are Users, but behave differently
User student = new Student(...);
User staff = new CareerCenterStaff(...);
User companyRep = new CompanyRep(...);

// Same method call, different behavior based on actual type
student.login();  // Student-specific login
staff.login();    // Staff-specific login
```

**Why?** We can write code that works with "User" in general, but each specific user type can have its own behavior.

#### 4. **Abstraction** (Hiding complexity)

```java
student.applyForInternship(internship);
// Behind the scenes: checks eligibility, validates dates, updates slots,
// saves to CSV, but the caller doesn't need to know all that!
```

**Why?** Users of a method don't need to know HOW it works, just WHAT it does.

---

### 3.2 Classes and Objects

**Class** = Blueprint (like an architectural plan)
**Object** = Actual thing built from blueprint (like an actual house)

```java
// Class = Blueprint for a Student
public class Student {
    private String name;
    private String email;
    // ... more fields
}

// Objects = Actual students created from blueprint
Student alice = new Student("Alice", "alice@ntu.edu.sg");
Student bob = new Student("Bob", "bob@ntu.edu.sg");

// alice and bob are two DIFFERENT objects from the SAME class
```

**In our project:**
- **Student class** (Student.java) = Blueprint for any student
- **alice, bob objects** = Actual student instances stored in App.studentList

---

### 3.3 Collections (ArrayList)

**What is ArrayList?**
Think of it as a dynamic array that can grow and shrink.

```java
// Old way (regular array) - fixed size
String[] names = new String[5];  // Can only hold 5 items

// New way (ArrayList) - dynamic size
ArrayList<String> names = new ArrayList<>();
names.add("Alice");   // Can add as many as we want
names.add("Bob");
names.add("Charlie");
```

**In our project:**

```java
// Stores all students in the system
public static ArrayList<Student> studentList = new ArrayList<>();

// Stores all internships
public static ArrayList<Internship> internshipList = new ArrayList<>();

// Each student has their own list of applications
private ArrayList<InternshipApplication> appliedInternships = new ArrayList<>();
```

**Common ArrayList operations:**
```java
// Add item
studentList.add(newStudent);

// Get item by index
Student first = studentList.get(0);

// Get size
int count = studentList.size();

// Loop through all items
for (Student student : studentList) {
    System.out.println(student.getName());
}

// Check if empty
if (studentList.isEmpty()) {
    System.out.println("No students!");
}

// Remove item
studentList.remove(student);

// Clear all
studentList.clear();
```

---

### 3.4 Enums (Enumerated Types)

**What are Enums?**
A set of fixed, predefined values. Like multiple choice options.

**Real-world analogy:**
Days of the week: MONDAY, TUESDAY, WEDNESDAY... (can't have any other value)

```java
// Defining an enum
public enum StudentMajor {
    CS,      // Computer Science
    DSAI,    // Data Science & AI
    CE,      // Computer Engineering
    IEM,     // Information Engineering & Media
    COMP     // Computing
}

// Using an enum
StudentMajor myMajor = StudentMajor.CS;

// Can only be one of the predefined values
// StudentMajor invalid = "Biology";  // ERROR! Not allowed
```

**Enums in our project:**

1. **InternshipStatus**
```java
public enum InternshipStatus {
    PENDING,    // Waiting for staff approval
    APPROVED,   // Staff approved, visible to students
    REJECTED,   // Staff rejected
    FILLED      // All slots taken
}
```

2. **UserAccountStatus**
```java
public enum UserAccountStatus {
    PENDING,    // Registration waiting for approval
    APPROVED,   // Can login and use system
    REJECTED    // Cannot login, can re-apply
}
```

3. **InternshipLevel**
```java
public enum InternshipLevel {
    BASIC,         // For Year 1-4 students
    INTERMEDIATE,  // For Year 3-4 students only
    ADVANCED       // For Year 3-4 students only
}
```

**Why use enums?**
1. **Type safety:** Prevents typos (can't write "PENDNG" by mistake)
2. **Code clarity:** Makes code more readable
3. **IDE support:** Auto-completion shows all valid options

---

### 3.5 Static vs Instance

**Instance (non-static)** = Belongs to a specific object
**Static** = Belongs to the class itself, shared by all objects

```java
public class Student {
    // Instance variable - each student has their own
    private String name;

    // Static variable - shared by ALL students
    private static int totalStudents = 0;

    public Student(String name) {
        this.name = name;
        totalStudents++;  // Increment shared counter
    }

    // Instance method - operates on specific student
    public String getName() {
        return this.name;
    }

    // Static method - operates on class level
    public static int getTotalStudents() {
        return totalStudents;
    }
}

// Usage
Student alice = new Student("Alice");
Student bob = new Student("Bob");

alice.getName();  // "Alice" - instance method
bob.getName();    // "Bob" - instance method

Student.getTotalStudents();  // 2 - static method (called on class, not object)
```

**In our project (App.java):**

```java
public class App {
    // Static - shared across entire application
    public static Scanner sc = new Scanner(System.in);
    public static ArrayList<Student> studentList = new ArrayList<>();
    public static User currentUser = null;

    public static void main(String[] args) {
        // Static method - entry point of program
    }
}
```

**Why static here?** Because we want ONE scanner, ONE student list, ONE current user for the ENTIRE application, not separate ones for each object.

---

### 3.6 Interfaces

**What is an interface?**
A contract that says "any class implementing this MUST have these methods."

```java
// Interface = Contract
public interface StudentAction {
    void run(Student student);  // Every StudentAction MUST have this method
}

// Class implementing interface
public class ViewInternshipsAction implements StudentAction {
    @Override
    public void run(Student student) {
        // Implementation of viewing internships
    }
}

// Another class implementing same interface
public class CheckApplicationStatusAction implements StudentAction {
    @Override
    public void run(Student student) {
        // Implementation of checking status
    }
}
```

**Why use interfaces?**
All student actions have the same signature `run(Student student)`, so we can treat them uniformly:

```java
StudentAction action1 = new ViewInternshipsAction();
StudentAction action2 = new CheckApplicationStatusAction();

// Both can be called the same way
action1.run(student);
action2.run(student);
```

This is called **polymorphism** - different actions, same interface.

---

### 3.7 File I/O (Reading/Writing CSV)

**CSV** = Comma-Separated Values (simple text file for storing data)

Example CSV:
```
StudentID,Name,Major,Year,Email
S001,Alice Tan,Computer Science,2,alice@ntu.edu.sg
S002,Bob Lee,Data Science and AI,3,bob@ntu.edu.sg
```

**Reading CSV in Java:**

```java
// 1. Open file
BufferedReader br = new BufferedReader(new FileReader("students.csv"));

// 2. Read line by line
String line = br.readLine();  // Skip header
while ((line = br.readLine()) != null) {
    // 3. Split by comma
    String[] values = line.split(",");

    // 4. Extract values
    String id = values[0].trim();
    String name = values[1].trim();
    String major = values[2].trim();
    int year = Integer.parseInt(values[3].trim());
    String email = values[4].trim();

    // 5. Create object and add to list
    Student student = new Student(id, name, email, "password", major, year);
    studentList.add(student);
}

// 6. Close file
br.close();
```

**Writing CSV in Java:**

```java
// 1. Open file for writing
FileWriter writer = new FileWriter("students.csv");

// 2. Write header
writer.append("StudentID,Name,Major,Year,Email\n");

// 3. Write each student
for (Student student : studentList) {
    writer.append(student.getUserID() + ",");
    writer.append(student.getName() + ",");
    writer.append(student.getMajor().getFullName() + ",");
    writer.append(student.getYear() + ",");
    writer.append(student.getEmail() + "\n");
}

// 4. Save and close
writer.flush();
writer.close();
```

**In our project:**
- `ReadFromCSV()` - Loads data when application starts
- `WriteToCSV()` - Saves data when changes are made

---

### 3.8 Exception Handling (try-catch)

**What are exceptions?**
Errors that occur during runtime (like file not found, invalid input).

```java
try {
    // Try to do something that might fail
    int year = Integer.parseInt(userInput);  // What if user enters "abc"?
}
catch (NumberFormatException e) {
    // Handle the error gracefully
    System.out.println("Invalid input! Please enter a number.");
}
```

**In our project (App.java):**

```java
try {
    int choice = Integer.parseInt(App.sc.nextLine());
    // Process choice
}
catch (NumberFormatException e) {
    System.out.println("Invalid input! Please enter a number for user type.");
}
```

**Why?** Without try-catch, if user enters "hello" instead of "1", the program would crash. With try-catch, we show an error message and continue.

---

### 3.9 Date and Time (LocalDate)

**LocalDate** = Represents a date (year-month-day) without time.

```java
// Create date
LocalDate today = LocalDate.now();  // Current date
LocalDate specific = LocalDate.of(2025, 11, 7);  // Nov 7, 2025

// Parse from string
LocalDate parsed = LocalDate.parse("2025-11-07");  // Format: yyyy-MM-dd

// Compare dates
if (today.isBefore(applicationDeadline)) {
    System.out.println("Still time to apply!");
}

if (today.isAfter(openDate)) {
    System.out.println("Application period started!");
}

// Format for display
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String formatted = today.format(formatter);  // "07/11/2025"
```

**In our project:**

```java
// Check if application period is open
LocalDate today = LocalDate.now();
if (today.isBefore(internship.getApplicationOpenDate()) ||
    today.isAfter(internship.getApplicationCloseDate())) {
    System.out.println("Application period is closed.");
    return;
}
```

---

### 3.10 Streams and Lambda Expressions

**Stream** = Sequence of elements that can be processed in a pipeline.

**Lambda** = Short way to write anonymous functions.

```java
// Old way: loop through and filter
List<Student> csStudents = new ArrayList<>();
for (Student student : studentList) {
    if (student.getMajor() == StudentMajor.CS) {
        csStudents.add(student);
    }
}

// New way: stream + lambda
List<Student> csStudents = studentList.stream()
    .filter(s -> s.getMajor() == StudentMajor.CS)  // Lambda: s -> ...
    .toList();
```

**In our project (ViewInternshipsAction.java):**

```java
// Filter internships by major
filteredList = filteredList.stream()
    .filter(i -> i.getPreferredMajor() != null &&
                  i.getPreferredMajor() == selectedMajor)
    .toList();

// Read as: "For each internship i, keep it if major matches"
```

**Lambda syntax:**
```java
(parameter) -> { code }

// Examples:
() -> System.out.println("Hi")           // No parameters
(x) -> x * 2                             // One parameter
(x, y) -> x + y                          // Two parameters
(student) -> student.getYear() > 2       // Returns boolean
```

---

## 4. Project Architecture

### 4.1 Package Structure

```
InternshipSystem/
└── src/
    └── team5/
        ├── App.java                           (Main entry point)
        ├── User.java                          (Parent class)
        ├── Student.java                       (Student user type)
        ├── CareerCenterStaff.java             (Staff user type)
        ├── CompanyRep.java                    (Company rep user type)
        ├── Internship.java                    (Internship entity)
        ├── InternshipApplication.java         (Application entity)
        ├── CompanyRepRegistration.java        (Registration entity)
        │
        ├── controllers/
        │   ├── StudentController.java         (Student menu handler)
        │   ├── CareerCenterStaffController.java
        │   └── CompanyRepController.java
        │
        ├── boundaries/
        │   ├── ConsoleBoundary.java           (Console I/O)
        │   ├── FileBoundary.java              (File I/O)
        │   └── InternshipBoundary.java        (Internship display)
        │
        ├── studentactions/
        │   ├── StudentAction.java             (Interface)
        │   ├── ViewInternshipsAction.java
        │   └── CheckApplicationStatusAction.java
        │
        ├── staffactions/
        │   ├── StaffAction.java               (Interface)
        │   ├── ReviewCompanyRegistrationsAction.java
        │   ├── ReviewInternshipSubmissionsAction.java
        │   └── ViewInternshipsAction.java
        │
        ├── companyrepactions/
        │   ├── CompanyRepAction.java          (Interface)
        │   ├── CreateInternshipAction.java
        │   └── ListOwnInternshipsAction.java
        │
        ├── registration/
        │   └── CompanyRepRegistrationHandler.java
        │
        └── enums/
            ├── InternshipApplicationStatus.java
            ├── InternshipLevel.java
            ├── InternshipStatus.java
            ├── StudentMajor.java
            ├── UserAccountStatus.java
            └── UserType.java
```

### 4.2 Design Patterns Used

#### 1. **MVC Pattern (Model-View-Controller)**

**Model** = Data and business logic
- Student.java, Internship.java, InternshipApplication.java
- These classes hold data and basic operations

**View** = User interface
- ConsoleBoundary.java, InternshipBoundary.java
- These handle display and user input

**Controller** = Connects Model and View
- StudentController.java, CareerCenterStaffController.java, CompanyRepController.java
- These process user choices and coordinate between Model and View

**Why?** Separates concerns - data logic separate from display logic.

#### 2. **Strategy Pattern (Action Classes)**

Each action (ViewInternships, CheckApplicationStatus) is its own class implementing a common interface.

**Why?** Easy to add new actions without changing existing code.

#### 3. **Singleton Pattern (App class)**

```java
public class App {
    public static Scanner sc = new Scanner(System.in);
    public static ArrayList<Student> studentList = new ArrayList<>();
    public static User currentUser = null;
    // ...
}
```

Only ONE instance of these shared resources exists for the entire application.

**Why?** Prevents conflicts from multiple scanners or multiple user lists.

---

### 4.3 Class Relationships

```
                           User (Parent)
                             |
          +------------------+------------------+
          |                  |                  |
       Student      CareerCenterStaff      CompanyRep
          |
          |
   [has many] InternshipApplication
                      |
                      |
                [references] Internship
```

**Inheritance (IS-A relationship):**
- Student IS-A User
- CareerCenterStaff IS-A User
- CompanyRep IS-A User

**Composition (HAS-A relationship):**
- Student HAS-A list of InternshipApplications
- InternshipApplication HAS-A reference to Internship
- InternshipApplication HAS-A reference to Student

---

## 5. How Users Use the System (CLI Guide)

Let me walk you through EXACTLY what a user sees and does when using the system.

### 5.1 Starting the Application

**Step 1:** User opens terminal/command prompt

**Step 2:** User navigates to project directory
```bash
cd InternshipSystem/src
```

**Step 3:** User runs the application
```bash
java team5.App
```

**Step 4:** User sees the main menu:

```
===== Welcome to Internship System =====

===== Login =====
Please choose your user type:
1: Student
2: Career Center Staff
3: Company Representatives
4: Register Company Representative
0: Exit
```

---

### 5.2 Student User Journey

#### Scenario: Alice wants to find an internship

**Step 1: Login**
```
Please choose your user type:
> 1

Please enter your user ID:
> alice.tan@student.ntu.edu.sg

Please enter your password:
> password

Login successful. Welcome Alice Tan.
```

**Step 2: View Menu**
```
===== Student Menu =====
1. View Internship
2. Check Internship Application Status
3. Update Password
4. Logout
Please choose an option:
```

**Step 3: View Internships**
```
> 1

Would you like to filter internships?
1. Filter by Preferred Major
2. Filter by Internship Level
3. Filter by Application Open Date
4. Filter by Application Close Date
5. Show all internships
0. Return to menu

Enter your choice:
> 5

===== Available Internship Opportunities =====
1. Internship ID: I001 | Title: Software Engineering Intern | Level: BASIC |
   Preferred Major: Computer Science | Application Opening Date: 15/01/2025 |
   Application Closing Date: 28/02/2025

2. Internship ID: I002 | Title: Data Analyst Intern | Level: INTERMEDIATE |
   Preferred Major: Data Science and AI | Application Opening Date: 01/02/2025 |
   Application Closing Date: 15/03/2025

Select internship number (or 0 to return):
```

**Step 4: View Internship Details**
```
> 1

You have chosen Internship ID: I001 Title: Software Engineering Intern
1. View Internship Details
2. Apply for this Internship
0. Return to internship list

> 1

===== Internship Details =====
Internship ID: I001
Title: Software Engineering Intern
Description: Develop web applications using React and Node.js
Level: BASIC
Preferred Majors: Computer Science
Application Open Date: 15/01/2025
Application Close Date: 28/02/2025
Number of Slots: 5

Enter 0 to return to the internship action list:
> 0
```

**Step 5: Apply for Internship**
```
You have chosen Internship ID: I001 Title: Software Engineering Intern
1. View Internship Details
2. Apply for this Internship
0. Return to internship list

> 2

Successfully applied for internship: Software Engineering Intern
```

**Step 6: Check Application Status**
```
===== Student Menu =====
1. View Internship
2. Check Internship Application Status
3. Update Password
4. Logout
Please choose an option:
> 2

===== Your Internship Applications =====
Application ID: 1 | Title: Software Engineering Intern |
Application Date: 07/11/2025 | Status: PENDING

Select application number to view details (or 0 to return):
> 1

===== Application Details =====
Application ID: 1
Title: Software Engineering Intern
Description: Develop web applications using React and Node.js
Application Date: 07/11/2025
Status: PENDING

Press 0 to return:
> 0
```

**Step 7: Logout**
```
===== Student Menu =====
1. View Internship
2. Check Internship Application Status
3. Update Password
4. Logout
Please choose an option:
> 4

You have already logged out.

===== Login =====
Please choose your user type:
```

---

### 5.3 Company Representative User Journey

#### Scenario: John from Tech Corp wants to post an internship

**Step 1: Register (First Time)**
```
Please choose your user type:
> 4

===== Company Representative Registration =====
Enter full name:
> John Smith

Enter company name:
> Tech Innovations

Enter department:
> Human Resources

Enter position:
> Recruiter

Enter email:
> john.smith@techinnov.com

===== Registration Summary =====
Name: John Smith
Company: Tech Innovations
Department: Human Resources
Position: Recruiter
Email: john.smith@techinnov.com

1. Confirm and submit
2. Start over
0. Cancel

> 1

Registration submitted successfully!
Your account is pending approval from Career Center Staff.
You will be able to login once approved.
```

**Step 2: Wait for Approval** (Staff approves in their portal)

**Step 3: Login (After Approval)**
```
Please choose your user type:
> 3

Please enter your user ID:
> john.smith@techinnov.com

Please enter your password:
> password

Login successful. Welcome John Smith.
```

**Step 4: View Menu**
```
===== Company Representative Menu =====
1. Create Internship Opportunity
2. View My Created Internships
3. Update Password
4. Logout
Please choose an option:
```

**Step 5: Create Internship**
```
> 1

===== Create Internship Opportunity =====
Enter internship title:
> Software Engineering Intern

Enter internship description:
> Develop web applications using React and Node.js. Work with experienced developers on real projects.

Select internship level:
1. BASIC
2. INTERMEDIATE
3. ADVANCED
> 1

Select preferred major:
1. Computer Science
2. Data Science & AI
3. Computer Engineering
4. Information Engineering & Media
5. Computing
> 1

Enter application open date (yyyy-MM-dd):
> 2025-01-15

Enter application close date (yyyy-MM-dd):
> 2025-02-28

Enter number of slots (1-10):
> 5

===== Internship Summary =====
Title: Software Engineering Intern
Description: Develop web applications using React and Node.js. Work with experienced developers on real projects.
Level: BASIC
Preferred Major: Computer Science
Open Date: 15/01/2025
Close Date: 28/02/2025
Slots: 5

1. Confirm and submit
2. Start over
0. Cancel

> 1

Internship created successfully!
Internship ID: I001
Status: PENDING (awaiting staff approval)
```

**Step 6: View My Internships**
```
===== Company Representative Menu =====
1. Create Internship Opportunity
2. View My Created Internships
3. Update Password
4. Logout
Please choose an option:
> 2

===== My Internship Postings =====
1. ID: I001 | Title: Software Engineering Intern | Level: BASIC |
   Status: PENDING | Slots: 5

Select internship number for details (or 0 to return):
> 1

===== Internship Details =====
Internship ID: I001
Title: Software Engineering Intern
Description: Develop web applications using React and Node.js. Work with experienced developers on real projects.
Level: BASIC
Preferred Major: Computer Science
Open Date: 15/01/2025
Close Date: 28/02/2025
Status: PENDING
Slots: 5

1. Update Internship (only if PENDING)
2. Delete Internship (WIP)
0. Return to list

> 0
```

---

### 5.4 Career Center Staff User Journey

#### Scenario: Dr. Wong reviews registrations and internships

**Step 1: Login**
```
Please choose your user type:
> 2

Please enter your user ID:
> sarah.wong@ntu.edu.sg

Please enter your password:
> password

Login successful. Welcome Dr. Sarah Wong.
```

**Step 2: View Menu**
```
===== Career Center Staff Menu =====
1. Review Company Representative Registrations
2. Review Internship Submissions
3. View Internship Opportunities
4. Update Password
5. Logout
Please choose an option:
```

**Step 3: Review Company Registrations**
```
> 1

===== Pending Company Representative Registrations =====
Page 1 of 1

1. Rep ID: john.smith@techinnov.com
   Name: John Smith
   Company: Tech Innovations
   Department: Human Resources
   Position: Recruiter
   Email: john.smith@techinnov.com
   Status: PENDING

Select registration number to review (or 'n' for next page, 'p' for previous, 0 to return):
> 1

===== Registration Details =====
Rep ID: john.smith@techinnov.com
Name: John Smith
Company: Tech Innovations
Department: Human Resources
Position: Recruiter
Email: john.smith@techinnov.com
Status: PENDING

1. Approve registration
2. Reject registration
0. Return to list

> 1

Registration approved successfully!
Rep can now login and create internships.
```

**Step 4: Review Internship Submissions**
```
===== Career Center Staff Menu =====
1. Review Company Representative Registrations
2. Review Internship Submissions
3. View Internship Opportunities
4. Update Password
5. Logout
Please choose an option:
> 2

===== Pending Internship Submissions =====
Page 1 of 1

1. Internship ID: I001
   Title: Software Engineering Intern
   Company: Tech Innovations
   Rep: john.smith@techinnov.com
   Level: BASIC
   Preferred Major: Computer Science
   Status: PENDING
   Slots: 5

Select internship number to review (or 'n' for next page, 'p' for previous, 0 to return):
> 1

===== Internship Details =====
Internship ID: I001
Title: Software Engineering Intern
Description: Develop web applications using React and Node.js. Work with experienced developers on real projects.
Level: BASIC
Preferred Major: Computer Science
Open Date: 15/01/2025
Close Date: 28/02/2025
Company: Tech Innovations
Representative: john.smith@techinnov.com
Status: PENDING
Slots: 5

1. Approve internship
2. Reject internship
0. Return to list

> 1

Internship approved successfully!
Internship is now visible to students.
```

**Step 5: View All Internships**
```
===== Career Center Staff Menu =====
1. Review Company Representative Registrations
2. Review Internship Submissions
3. View Internship Opportunities
4. Update Password
5. Logout
Please choose an option:
> 3

===== All Internship Opportunities =====
1. ID: I001 | Title: Software Engineering Intern | Status: APPROVED | Slots: 5
2. ID: I002 | Title: Data Analyst Intern | Status: PENDING | Slots: 3
3. ID: I003 | Title: Cloud Engineer | Status: REJECTED | Slots: 2

Total: 3 internships

Press 0 to return:
> 0
```

---

## 6. Detailed Feature Walkthrough

### 6.1 Student Features

#### Feature 1: View Internships with Filters

**Purpose:** Allow students to browse approved internships with optional filters.

**How it works:**

1. **Filtering Stage:**
   - Student chooses filter type (major, level, date)
   - System prompts for filter value
   - System applies filter using Java streams

```java
// Filter by major example
filteredList = filteredList.stream()
    .filter(i -> i.getPreferredMajor() == selectedMajor)
    .toList();
```

2. **Display Stage:**
   - Only APPROVED internships shown
   - Displayed with: ID, Title, Level, Major, Dates
   - Numbered list for easy selection

3. **Selection Stage:**
   - Student enters number to select internship
   - Options: View Details or Apply

**Business Rules:**
- Only show APPROVED internships (PENDING/REJECTED hidden)
- If Year 1-2 student viewing, may filter to BASIC only (depending on implementation)
- Empty list shows "No internships available"

---

#### Feature 2: Apply for Internship

**Purpose:** Submit application for an internship.

**How it works:**

1. **Validation Checks (in order):**

```java
// Check 1: Application period open?
if (today.isBefore(openDate) || today.isAfter(closeDate)) {
    return "Application period closed";
}

// Check 2: Student already has 3 applications?
if (student.getInternshipApplications().size() >= 3) {
    return "Maximum 3 applications reached";
}

// Check 3: Student already employed?
if (student.getEmployedStatus() == true) {
    return "Already accepted another internship";
}

// Check 4: Already applied to this internship?
for (InternshipApplication app : student.getApplications()) {
    if (app.getInternship().equals(chosen)) {
        return "Already applied for this internship";
    }
}

// Check 5: Year eligibility
if (student.getYear() <= 2 && internship.getLevel() != BASIC) {
    return "Year 1-2 can only apply to BASIC internships";
}

// Check 6: Major matching
if (internship.getPreferredMajor() != null &&
    student.getMajor() != internship.getPreferredMajor()) {
    return "Major mismatch";
}

// Check 7: Slots available?
if (internship.getNumOfSlots() <= 0) {
    return "Internship fully booked";
}
```

2. **Application Creation:**
```java
InternshipApplication app = new InternshipApplication(
    id,                    // Auto-generated
    internship,            // Selected internship
    student,               // Current student
    LocalDate.now()        // Today's date
);

student.addInternshipApplications(app);      // Add to student's list
internship.setNumOfSlots(slots - 1);         // Decrement available slots
```

3. **Confirmation:**
   - Success message shown
   - Application status: PENDING
   - Waiting for company decision

---

#### Feature 3: Check Application Status

**Purpose:** View all submitted applications and their status.

**How it works:**

1. **Display Stage:**
```java
if (student.getInternshipApplications().isEmpty()) {
    System.out.println("You have not applied for any internships yet.");
    return;
}

for (InternshipApplication app : student.getApplications()) {
    System.out.println("Application ID: " + app.getApplicationId());
    System.out.println("Internship: " + app.getInternship().getTitle());
    System.out.println("Date: " + app.getAppliedDate());
    System.out.println("Status: " + app.getStatus());  // PENDING/SUCCESSFUL/UNSUCCESSFUL
}
```

2. **Status Meanings:**
   - **PENDING:** Company hasn't reviewed yet
   - **SUCCESSFUL:** Company accepted application (offer made)
   - **UNSUCCESSFUL:** Company rejected application

3. **Accept Offer (if SUCCESSFUL):**
```java
// Check if student can accept
if (student.getEmployedStatus()) {
    return "Already accepted another offer";
}

if (internship.getNumOfSlots() <= 0) {
    return "Internship fully booked";
}

// Accept offer
student.setEmployedStatus(true);         // Mark as employed
app.setHasStudentAccepted(true);         // Mark offer as accepted
student.getApplications().clear();       // Clear other applications
```

---

### 6.2 Company Representative Features

#### Feature 1: Company Rep Registration

**Purpose:** Allow companies to register before creating internships.

**How it works:**

1. **Data Collection:**
```java
System.out.println("Enter full name:");
String name = sc.nextLine();

System.out.println("Enter company name:");
String companyName = sc.nextLine();

System.out.println("Enter department:");
String department = sc.nextLine();

System.out.println("Enter position:");
String position = sc.nextLine();

System.out.println("Enter email:");
String email = sc.nextLine();
```

2. **Validation:**
```java
// Check if email already exists
for (CompanyRep rep : compRepList) {
    if (rep.getEmail().equalsIgnoreCase(email)) {
        if (rep.getStatus() == PENDING || rep.getStatus() == APPROVED) {
            return "Email already registered";
        }
        else if (rep.getStatus() == REJECTED) {
            // Allow re-submission
            return "Previous registration rejected. Re-submit?";
        }
    }
}
```

3. **Account Creation:**
```java
CompanyRep rep = new CompanyRep(
    email,              // Used as user ID
    name,
    email,
    "password",         // Default password
    companyName,
    department,
    position,
    PENDING,            // Status
    new ArrayList<>()   // Empty internship list
);

compRepList.add(rep);
WriteToCSV(envFilePathRep, UserType.COMREP);  // Save to file
```

4. **Outcome:**
   - Status: PENDING
   - Cannot login until staff approves
   - Can check back later

---

#### Feature 2: Create Internship

**Purpose:** Post a new internship opportunity.

**How it works:**

1. **Pre-checks:**
```java
// Check if already has 5 internships
if (companyRep.getInternships().size() >= 5) {
    return "Maximum 5 internships reached";
}
```

2. **Data Collection:**
```java
System.out.println("Enter internship title:");
String title = sc.nextLine();

System.out.println("Enter description:");
String description = sc.nextLine();

// Level selection (1-3)
InternshipLevel level = selectLevel();

// Major selection (1-5)
StudentMajor major = selectMajor();

System.out.println("Enter application open date (yyyy-MM-dd):");
LocalDate openDate = LocalDate.parse(sc.nextLine());

System.out.println("Enter application close date (yyyy-MM-dd):");
LocalDate closeDate = LocalDate.parse(sc.nextLine());

// Validation: close date after open date
if (closeDate.isBefore(openDate)) {
    return "Close date cannot be before open date";
}

System.out.println("Enter number of slots (1-10):");
int slots = Integer.parseInt(sc.nextLine());

// Validation: slots 1-10
if (slots < 1 || slots > 10) {
    return "Slots must be between 1 and 10";
}
```

3. **ID Generation:**
```java
// Generate unique ID starting with "I"
String internshipId = generateUniqueId("I", existingIds);
// Example: I1234, I5678
```

4. **Internship Creation:**
```java
Internship internship = new Internship(
    internshipId,
    title,
    description,
    level,
    major,
    openDate,
    closeDate,
    PENDING,            // Status (needs staff approval)
    companyRep.getUserID(),
    slots
);

internshipList.add(internship);
companyRep.addInternship(internship);
WriteToCSV(envFilePathInternship, UserType.NONE);  // Save
```

5. **Outcome:**
   - Internship created with PENDING status
   - NOT visible to students yet
   - Waiting for staff approval

---

#### Feature 3: Update Internship

**Purpose:** Modify internship details before approval.

**How it works:**

1. **Check Status:**
```java
if (internship.getStatus() != PENDING) {
    return "Can only update PENDING internships";
}
```

2. **Update Options:**
```java
System.out.println("What would you like to update?");
System.out.println("1. Title");
System.out.println("2. Description");
System.out.println("3. Level");
System.out.println("4. Preferred Major");
System.out.println("5. Application Open Date");
System.out.println("6. Application Close Date");
System.out.println("7. Number of Slots");
System.out.println("0. Done updating");

int choice = Integer.parseInt(sc.nextLine());

switch(choice) {
    case 1:
        System.out.println("Enter new title:");
        internship.setTitle(sc.nextLine());
        break;
    case 2:
        System.out.println("Enter new description:");
        internship.setDescription(sc.nextLine());
        break;
    // ... etc for other fields
}

WriteToCSV(envFilePathInternship, UserType.NONE);  // Save changes
```

3. **Restrictions:**
   - Can only update PENDING internships
   - APPROVED/REJECTED internships locked
   - Date validation still applies

---

### 6.3 Career Center Staff Features

#### Feature 1: Review Company Registrations

**Purpose:** Approve or reject company representative accounts.

**How it works:**

1. **Fetch Pending Registrations:**
```java
List<CompanyRep> pendingReps = compRepList.stream()
    .filter(rep -> rep.getStatus() == PENDING)
    .toList();

if (pendingReps.isEmpty()) {
    System.out.println("No pending registrations");
    return;
}
```

2. **Pagination Display (5 per page):**
```java
int currentPage = 0;
int pageSize = 5;
int totalPages = (pendingReps.size() + pageSize - 1) / pageSize;

while (true) {
    int start = currentPage * pageSize;
    int end = Math.min(start + pageSize, pendingReps.size());

    System.out.println("Page " + (currentPage + 1) + " of " + totalPages);

    for (int i = start; i < end; i++) {
        CompanyRep rep = pendingReps.get(i);
        System.out.println((i - start + 1) + ". " + rep.getName());
        System.out.println("   Company: " + rep.getCompanyName());
        System.out.println("   Email: " + rep.getEmail());
    }

    System.out.println("Select number, 'n' for next, 'p' for previous, 0 to return:");
    String input = sc.nextLine();

    if (input.equals("n") && currentPage < totalPages - 1) {
        currentPage++;
    }
    else if (input.equals("p") && currentPage > 0) {
        currentPage--;
    }
    else if (input.equals("0")) {
        return;
    }
    else {
        // Process selection
    }
}
```

3. **Approve/Reject:**
```java
System.out.println("1. Approve");
System.out.println("2. Reject");
int choice = Integer.parseInt(sc.nextLine());

if (choice == 1) {
    rep.setStatus(APPROVED);
    System.out.println("Registration approved!");
    // Rep can now login
}
else if (choice == 2) {
    rep.setStatus(REJECTED);
    System.out.println("Registration rejected.");
    // Rep can re-apply
}

WriteToCSV(envFilePathRep, UserType.COMREP);  // Save status
```

---

#### Feature 2: Review Internship Submissions

**Purpose:** Approve or reject internship postings.

**How it works (similar to registration review):**

1. **Fetch Pending Internships:**
```java
List<Internship> pendingInternships = internshipList.stream()
    .filter(i -> i.getInternshipStatus() == PENDING)
    .toList();
```

2. **Display with Pagination (5 per page)**

3. **Approve/Reject:**
```java
if (choice == 1) {
    internship.setInternshipStatus(APPROVED);
    System.out.println("Internship approved!");
    // Now visible to students
}
else if (choice == 2) {
    internship.setInternshipStatus(REJECTED);
    System.out.println("Internship rejected.");
    // NOT visible to students
}

WriteToCSV(envFilePathInternship, UserType.NONE);
```

---

## 7. Code Deep Dive

Let me explain the most important functions in detail.

### 7.1 Main Method (App.java)

**Purpose:** Entry point of the application.

```java
public static void main(String[] args) {
    // Step 1: Load environment variables (file paths)
    LoadEnvironmentVariables();

    // Step 2: Clear existing data (in case of reload)
    studentList.clear();
    staffList.clear();
    compRepList.clear();
    internshipList.clear();

    // Step 3: Load data from CSV files
    // IMPORTANT: Load internships before company reps
    // because reps need to link to their internships
    ReadFromCSV(envFilePathInternship, UserType.NONE);
    ReadFromCSV(envFilePathStudent, UserType.STUDENT);
    ReadFromCSV(envFilePathStaff, UserType.CCSTAFF);
    ReadFromCSV(envFilePathRep, UserType.COMREP);

    // Step 4: Show welcome message
    printSectionTitle("Welcome to Internship System");

    // Step 5: Main loop
    boolean exitProgram = false;
    while (!exitProgram) {
        printSectionTitle("Login", true);

        try {
            // Display menu
            System.out.println("Please choose your user type:");
            System.out.println("1: Student");
            System.out.println("2: Career Center Staff");
            System.out.println("3: Company Representatives");
            System.out.println("4: Register Company Representative");
            System.out.println("0: Exit");

            // Get user choice
            int choice = Integer.parseInt(App.sc.nextLine());

            if (choice == 0) {
                System.out.println("Exiting system...");
                exitProgram = true;
                break;
            }

            if (choice < 0 || choice > 4) {
                System.out.println("Invalid user type. Please enter again.");
                continue;
            }

            // Handle registration
            if (choice == 4) {
                CompanyRepRegistrationHandler registrationHandler = new CompanyRepRegistrationHandler();
                registrationHandler.startRegistration();
                continue;
            }

            // Get login credentials
            System.out.println("Please enter your user ID:");
            String userID = App.sc.nextLine();

            System.out.println("Please enter your password:");
            String password = App.sc.nextLine();

            // Determine user type
            UserType userType = UserType.NONE;
            switch(choice) {
                case 1: userType = UserType.STUDENT; break;
                case 2: userType = UserType.CCSTAFF; break;
                case 3: userType = UserType.COMREP; break;
            }

            // Verify user
            boolean foundUser = verifyUserFromList(userType, userID, password);
            if (!foundUser || currentUser == null) {
                continue;  // Back to login menu
            }

            System.out.println("Login successful. Welcome " + currentUser.getName() + ".");

            // Show appropriate menu based on user type
            if (userType == UserType.STUDENT && currentUser instanceof Student) {
                StudentController studentController = new StudentController();
                studentController.showMenu((Student)currentUser);
            }
            else if (userType == UserType.CCSTAFF && currentUser instanceof CareerCenterStaff) {
                CareerCenterStaffController staffController = new CareerCenterStaffController();
                staffController.showMenu((CareerCenterStaff)currentUser);
            }
            else if (userType == UserType.COMREP && currentUser instanceof CompanyRep) {
                CompanyRepController companyRepController = new CompanyRepController();
                companyRepController.showMenu((CompanyRep)currentUser);
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number for user type.");
        }
        catch (Exception e) {
            System.out.println("An error occurred during login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

**Key Points:**
- Uses a **while loop** to keep showing login menu until user exits
- Uses **try-catch** to handle invalid input gracefully
- Uses **instanceof** to check actual type before casting
- **CSV loading order matters** - internships before reps!

---

### 7.2 verifyUserFromList Method

**Purpose:** Authenticate user and handle password attempts.

```java
public static boolean verifyUserFromList(UserType userType, String userID, String password) {
    User foundUser = null;

    // Step 1: Find user in appropriate list
    switch(userType) {
        case STUDENT:
            for (Student student : studentList) {
                if (student.getUserID().equals(userID)) {
                    foundUser = student;
                    break;
                }
            }
            break;

        case CCSTAFF:
            for (CareerCenterStaff staff : staffList) {
                if (staff.getUserID().equals(userID)) {
                    foundUser = staff;
                    break;
                }
            }
            break;

        case COMREP:
            for (CompanyRep rep : compRepList) {
                if (rep.getUserID().equalsIgnoreCase(userID)) {
                    foundUser = rep;
                    break;
                }
            }
            break;

        default:
            System.out.println("Invalid User Type.");
            return false;
    }

    // Step 2: Check if user exists
    if (foundUser == null) {
        System.out.println("User ID not found in system.");
        return false;
    }

    // Step 3: Special check for company rep account status
    if (userType == UserType.COMREP) {
        CompanyRep rep = (CompanyRep) foundUser;
        UserAccountStatus status = rep.getAccountStatus();

        if (status == UserAccountStatus.PENDING) {
            System.out.println("Your account is pending approval. Please wait for the career center staff to review it.");
            return false;
        }

        if (status == UserAccountStatus.REJECTED) {
            System.out.println("Your registration was rejected. Please submit a new registration request for review.");
            return false;
        }
    }

    // Step 4: Verify password (3 attempts)
    int maxAttempts = 3;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
        if (foundUser.getPassword().equals(password)) {
            // Password correct!
            currentUser = foundUser;
            currentUser.setUserType(userType);
            foundUser.login();
            return true;
        }
        else {
            // Password wrong
            if (attempt < maxAttempts) {
                System.out.println("Password wrong, please enter again:");
                password = App.sc.nextLine().trim();
            }
            else {
                System.out.println("Wrong password 3 times. Login Failed.");
            }
        }
    }

    // Failed after 3 attempts
    return false;
}
```

**Key Points:**
- **Linear search** through appropriate list to find user
- **Early return** if user not found or account not approved
- **3 password attempts** before failing
- Sets **currentUser** global variable on success
- Returns **boolean** to indicate success/failure

---

### 7.3 ReadFromCSV Method

**Purpose:** Load data from CSV files into memory.

```java
public static void ReadFromCSV(String fileName, UserType userType) {
    String csvFile = fileName;
    String line;
    String csvSplitBy = ",";

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        // Skip header line
        br.readLine();

        // Read each line
        while ((line = br.readLine()) != null) {
            // Split by comma
            String[] values = line.split(csvSplitBy);

            // Parse based on user type
            if (userType == UserType.STUDENT && values.length == 5) {
                String id = values[0].trim();
                String name = values[1].trim();
                String preferredMajor = values[2].trim();
                int year = Integer.parseInt(values[3].trim());
                String email = values[4].trim();

                StudentMajor major = StudentMajor.fromFullName(preferredMajor);

                Student student = new Student(id, name, email, "password", major, year);
                studentList.add(student);
            }
            else if (userType == UserType.CCSTAFF && values.length == 5) {
                String id = values[0].trim();
                String name = values[1].trim();
                String role = values[2].trim();
                String department = values[3].trim();
                String email = values[4].trim();

                CareerCenterStaff staff = new CareerCenterStaff(id, name, email, "password", role, department);
                staffList.add(staff);
            }
            else if (userType == UserType.COMREP && values.length == 7) {
                String repId = values[0].trim();
                String name = values[1].trim();
                String companyName = values[2].trim();
                String department = values[3].trim();
                String position = values[4].trim();
                String email = values[5].trim();
                String statusValue = values[6].trim().toUpperCase();

                UserAccountStatus status = UserAccountStatus.fromString(statusValue);

                // Find internships belonging to this company rep
                ArrayList<Internship> comprepInternships = App.internshipList.stream()
                    .filter(i -> i.getCompanyRep().toLowerCase().equals(repId.toLowerCase()))
                    .collect(Collectors.toCollection(ArrayList::new));

                CompanyRep registration = new CompanyRep(repId, name, email, "password", companyName, department, position, status, comprepInternships);
                compRepList.add(registration);
            }
            else {
                // Internship data (length == 11)
                if (values.length == 11) {
                    String internshipId = values[0].trim();
                    String title = values[1].trim();
                    String description = values[2].trim();
                    String level = values[3].trim();
                    String preferredMajor = values[4].trim();
                    String openDate = values[5].trim();
                    String closeDate = values[6].trim();
                    String status = values[7].trim();
                    String companyRep = values[9].trim();
                    String slots = values[10].trim();

                    InternshipLevel internshipLevel = InternshipLevel.fromString(level);
                    StudentMajor major = StudentMajor.fromFullName(preferredMajor);
                    LocalDate appOpenDate = parseDate(openDate);
                    LocalDate appCloseDate = parseDate(closeDate);
                    InternshipStatus appStatus = InternshipStatus.fromString(status);
                    int numOfSlots = Integer.parseInt(slots);

                    Internship internship = new Internship(internshipId, title, description, internshipLevel, major, appOpenDate, appCloseDate, appStatus, companyRep, numOfSlots);
                    internshipList.add(internship);
                }
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
```

**Key Points:**
- Uses **try-with-resources** for automatic file closing
- **Skips header line** with first `br.readLine()`
- **Splits each line** by comma
- **Trims whitespace** from each value
- **Parses enums** using custom `fromString()` methods
- **Links internships to company reps** using stream filter
- **Catches exceptions** to prevent crashes

---

### 7.4 WriteToCSV Method

**Purpose:** Save data from memory back to CSV files.

```java
public static void WriteToCSV(String fileName, UserType userType) {
    String csvFile = fileName;

    try (FileWriter writer = new FileWriter(csvFile)) {

        if (userType == UserType.STUDENT) {
            // Write header
            writer.append("StudentID,Name,Major,Year,Email\n");

            // Write each student
            for (Student student : studentList) {
                String userID = student.getUserID();
                String name = student.getName();
                String major = student.getMajor().getFullName();
                int year = student.getYear();
                String email = student.getEmail();

                writer.append(userID + "," + name + "," + major + "," + year + "," + email + "\n");
            }
        }
        else if (userType == UserType.CCSTAFF) {
            // Write header
            writer.append("StaffID,Name,Role,Department,Email\n");

            // Write each staff
            for (CareerCenterStaff staff : staffList) {
                String userID = staff.getUserID();
                String name = staff.getName();
                String role = staff.getRole();
                String department = staff.getDepartment();
                String email = staff.getEmail();

                writer.append(userID + "," + name + "," + role + "," + department + "," + email + "\n");
            }
        }
        // ... similar for other types

        writer.flush();
        System.out.println("CSV file written successfully!");
    }
    catch (FileNotFoundException fe) {
        System.out.println("CSV file not found!");
    }
    catch (IOException e) {
        System.out.println("Failed to save to file: " + e.getMessage());
        System.out.println("Stack trace:");
        e.printStackTrace();
    }
}
```

**Key Points:**
- **Overwrites entire file** (not append)
- **Writes header first**, then data
- Uses **flush()** to ensure data is written
- **Catches exceptions** and shows user-friendly messages
- Called after any data modification (approve, reject, create, update)

---

### 7.5 applyForInternship Method (ViewInternshipsAction.java)

**Purpose:** Process student application with all validations.

```java
private void applyForInternship(Student student, Internship chosen) {
    LocalDate today = LocalDate.now();

    // Validation 1: Application period
    if (today.isBefore(chosen.getApplicationOpenDate()) ||
        today.isAfter(chosen.getApplicationCloseDate())) {
        System.out.println("The application period for this internship is closed.");
        return;
    }

    // Validation 2: Max 3 applications
    if (student.getInternshipApplications().size() >= 3) {
        System.out.println("You have already applied for 3 internships. You are not allowed to apply this internship.");
        return;
    }

    // Validation 3: Already employed
    if (student.getEmployedStatus()) {
        System.out.println("You have already accepted another internship. You are not allowed to apply this internship.");
        return;
    }

    // Validation 4: Duplicate application
    for (InternshipApplication app : student.getInternshipApplications()) {
        if (app.getInternshipInfo().equals(chosen)) {
            System.out.println("You have already applied for this internship before.");
            return;
        }
    }

    // Validation 5: Year eligibility
    if (student.getYear() <= 2 && chosen.getInternshipLevel() != InternshipLevel.BASIC) {
        System.out.println("You are not eligible for this internship.");
        return;
    }

    // Validation 6: Major matching
    StudentMajor preferred = chosen.getPreferredMajor();
    if (preferred != null && student.getMajor() != preferred) {
        System.out.println("This internship prefers students from " + preferred.getFullName() + ".");
        return;
    }

    // Validation 7: Slots available
    if (chosen.getNumOfSlots() <= 0 || chosen.getInternshipStatus() == InternshipStatus.FILLED) {
        System.out.println("This internship is fully booked.");
        return;
    }

    // All validations passed! Create application
    InternshipApplication internApp = new InternshipApplication(
        student.getInternshipApplications().size() + 1,  // Simple ID
        chosen,
        student,
        LocalDate.now()
    );

    // Add to student's list
    student.addInternshipApplications(internApp);

    // Decrement slots
    chosen.setNumOfSlots(chosen.getNumOfSlots() - 1);

    System.out.println("Successfully applied for internship: " + chosen.getTitle());
}
```

**Key Points:**
- **Multiple return points** for early exit on validation failure
- **Order matters** - check cheapest validations first
- **Atomicity** - all checks pass before modifying data
- **Side effects** - decrements slots, adds to student's list
- **No CSV save here** - handled at higher level

---

## 8. Data Flow

### 8.1 Application Startup Flow

```
1. main() method starts
   |
2. LoadEnvironmentVariables()
   | - Reads application.properties
   | - Gets file paths for CSVs
   |
3. Clear all ArrayLists
   |
4. ReadFromCSV() for each file
   | - internships.csv → internshipList
   | - students.csv → studentList
   | - staff.csv → staffList
   | - companyreps.csv → compRepList
   |
5. Show main menu
   |
6. Wait for user input
```

### 8.2 Student Application Flow

```
1. Student logs in
   |
2. Chooses "View Internship"
   |
3. ViewInternshipsAction.run(student)
   | - Filter internships
   | - Display approved only
   |
4. Student selects internship
   |
5. Student chooses "Apply"
   |
6. applyForInternship(student, internship)
   | - Run all validations
   | - Create InternshipApplication object
   | - Add to student.appliedInternships
   | - Decrement internship.numOfSlots
   |
7. Return to menu
```

### 8.3 Company Rep Registration Flow

```
1. User chooses "Register Company Representative"
   |
2. CompanyRepRegistrationHandler.startRegistration()
   | - Collect user input
   | - Validate email uniqueness
   |
3. Create CompanyRep object
   | - userID = email
   | - password = "password"
   | - status = PENDING
   |
4. Add to compRepList
   |
5. WriteToCSV(companyreps.csv)
   | - Overwrite file with all reps
   |
6. Show confirmation
```

### 8.4 Staff Approval Flow

```
1. Staff logs in
   |
2. Chooses "Review Company Registrations"
   |
3. ReviewCompanyRegistrationsAction.run(staff)
   | - Filter: status == PENDING
   | - Display paginated list (5 per page)
   |
4. Staff selects a registration
   |
5. Staff chooses "Approve"
   |
6. rep.setStatus(APPROVED)
   |
7. WriteToCSV(companyreps.csv)
   | - Save updated status
   |
8. Rep can now login!
```

### 8.5 Data Persistence Flow

```
Memory (Runtime)                    Disk (Permanent)
================                    ================

ArrayList<Student>                  students.csv
ArrayList<Staff>         <-->       staff.csv
ArrayList<CompanyRep>               companyreps.csv
ArrayList<Internship>               internships.csv

On Startup: CSV → ArrayList (ReadFromCSV)
On Change:  ArrayList → CSV (WriteToCSV)
```

**Important:** Changes only persist if `WriteToCSV()` is called!

---

## 9. Presentation Q&A

Here are common questions you might get during your presentation, with detailed answers.

### Q1: Why did you use CSV files instead of a real database?

**Answer:**
"We chose CSV files for simplicity and portability. For a university assignment demonstrating object-oriented programming concepts, CSV provides:
1. **Human-readable format** - easy to verify data manually
2. **No external dependencies** - works on any machine with Java
3. **Simple file I/O** - demonstrates BufferedReader and FileWriter usage
4. **Version control friendly** - CSV files can be tracked in Git

For a production system, we would use a proper database like MySQL or PostgreSQL for:
- Concurrent access
- Data integrity constraints
- Better performance
- ACID transactions"

---

### Q2: What design patterns did you use?

**Answer:**
"We implemented several design patterns:

1. **MVC (Model-View-Controller)**
   - Models: Student, Internship, InternshipApplication (data classes)
   - Views: ConsoleBoundary, InternshipBoundary (display logic)
   - Controllers: StudentController, StaffController, CompanyRepController (business logic)

2. **Strategy Pattern**
   - StudentAction interface with different implementations (ViewInternshipsAction, CheckApplicationStatusAction)
   - Allows easy addition of new actions without modifying existing code

3. **Singleton Pattern**
   - App class with static lists (studentList, internshipList, etc.)
   - Only one instance of shared resources

4. **Inheritance Hierarchy**
   - User parent class with Student, Staff, CompanyRep children
   - Promotes code reuse and polymorphism"

---

### Q3: How do you prevent duplicate applications?

**Answer:**
"We validate at multiple levels:

1. **In applyForInternship() method**, we loop through the student's existing applications:
```java
for (InternshipApplication app : student.getInternshipApplications()) {
    if (app.getInternshipInfo().equals(chosen)) {
        System.out.println('Already applied');
        return;
    }
}
```

2. **Each student object maintains their own ArrayList** of applications, making it easy to check

3. **This check happens before creating the InternshipApplication object**, so no duplicate is ever created

The time complexity is O(n) where n is the number of applications per student, which is capped at 3, so effectively O(1)."

---

### Q4: What happens if two students apply for the last slot simultaneously?

**Answer:**
"This is a **race condition** problem. In our current implementation:
1. The application is single-threaded (one user at a time via CLI)
2. The `numOfSlots` is checked and decremented atomically within the same method call
3. Since we're using a CLI (not a web app), only one user can use the program at a time

If this were a multi-user web application, we would need:
1. **Database transactions** with row-level locking
2. **Optimistic locking** (check slot count, if changed since read, retry)
3. **Pessimistic locking** (lock the internship row before checking)

For our CLI assignment, this race condition cannot occur since it's single-threaded."

---

### Q5: Why is the password stored in plain text?

**Answer:**
"You're right to notice this security issue! We stored passwords in plain text for simplicity in this academic project. We acknowledge this is **NOT secure**.

In a real-world application, we would:
1. **Hash passwords** using bcrypt, scrypt, or PBKDF2
2. **Never store plain text** passwords
3. **Add salt** to prevent rainbow table attacks
4. **Use HTTPS** for transmission

Example with hashing:
```java
// Storing password
String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

// Verifying password
if (BCrypt.checkpw(inputPassword, hashedPassword)) {
    // Login successful
}
```

For this assignment focusing on OOP concepts, we prioritized functionality over security, but we understand the risks."

---

### Q6: How does the inheritance hierarchy work?

**Answer:**
"We have a three-level hierarchy:

**Base Class: User**
```java
public class User {
    private String userID;
    private String name;
    private String email;
    private String password;

    public void login() { }
    public void logout() { }
    public boolean changePassword() { }
}
```

**Child Classes inherit from User:**
```java
public class Student extends User {
    private StudentMajor major;
    private int yearOfStudy;
    private ArrayList<InternshipApplication> appliedInternships;

    // Inherits: userID, name, email, password, login(), logout(), changePassword()
    // Adds: major, yearOfStudy, appliedInternships
}
```

**Benefits:**
1. **Code reuse** - login/logout logic written once in User
2. **Polymorphism** - can treat all users uniformly as type User
3. **Extensibility** - easy to add new user types (e.g., Admin)
4. **Type safety** - compiler ensures correct usage

**Example of polymorphism:**
```java
User currentUser = verifyLogin();  // Could be Student, Staff, or CompanyRep

currentUser.changePassword();  // Works for any user type
```"

---

### Q7: How do you handle invalid input from users?

**Answer:**
"We use multiple strategies:

1. **Try-catch blocks** for parsing:
```java
try {
    int choice = Integer.parseInt(sc.nextLine());
} catch (NumberFormatException e) {
    System.out.println('Invalid input! Please enter a number.');
}
```

2. **Input validation loops**:
```java
while (!valid) {
    String input = sc.nextLine();
    if (input.matches(pattern)) {
        valid = true;
    } else {
        System.out.println('Invalid format. Try again.');
    }
}
```

3. **Null checks**:
```java
if (userInput == null || userInput.trim().isEmpty()) {
    System.out.println('Field cannot be empty.');
    return;
}
```

4. **Business rule validation**:
```java
if (closeDate.isBefore(openDate)) {
    System.out.println('Close date cannot be before open date.');
    return;
}
```

This prevents crashes and provides clear feedback to users."

---

### Q8: What are the limitations of your system?

**Answer:**
"We acknowledge several limitations:

1. **No concurrent access** - Only one user can use the CLI at a time
2. **Plain text passwords** - Security vulnerability
3. **No application review workflow** - Company reps can't actually review student applications
4. **Limited search** - No full-text search or advanced filtering
5. **No notifications** - Students don't get notified of application status changes
6. **No data validation on CSV** - Corrupted CSV files could break the system
7. **No undo/audit trail** - Can't track who changed what and when
8. **Fixed application limit** - Hardcoded limit of 3 applications per student
9. **No resume upload** - Students can't attach documents

**Future enhancements:**
- Web interface for concurrent access
- Email notifications
- Resume/document upload
- Advanced search and filters
- Admin dashboard with analytics
- Two-factor authentication"

---

### Q9: Explain the filter functionality in ViewInternships

**Answer:**
"The filter uses Java Streams for efficient filtering:

**No filter (baseline):**
```java
List<Internship> all = new ArrayList<>(internshipList);
```

**Filter by major:**
```java
filteredList = filteredList.stream()
    .filter(i -> i.getPreferredMajor() == selectedMajor)
    .toList();
```

**How it works:**
1. `.stream()` converts ArrayList to Stream
2. `.filter()` takes a lambda that returns boolean
3. Only internships where lambda returns true are kept
4. `.toList()` converts Stream back to List

**Example:**
```
Before filter:
[I001: CS, I002: DSAI, I003: CE, I004: CS]

After filter(i -> i.getMajor() == CS):
[I001: CS, I004: CS]
```

**Chaining filters:**
```java
filteredList = internshipList.stream()
    .filter(i -> i.getPreferredMajor() == CS)
    .filter(i -> i.getLevel() == BASIC)
    .filter(i -> i.getNumOfSlots() > 0)
    .toList();
```

This is more readable than nested loops and allows for flexible composition of filters."

---

### Q10: How would you test this system?

**Answer:**
"We would use multiple testing levels:

1. **Unit Testing** - Test individual methods
```java
@Test
public void testStudentCannotApplyForFourthInternship() {
    Student student = new Student(...);
    // Add 3 applications
    student.addApplication(app1);
    student.addApplication(app2);
    student.addApplication(app3);

    // Try to add 4th - should fail
    boolean result = student.applyForInternship(internship4);
    assertFalse(result);
}
```

2. **Integration Testing** - Test workflows
- Test complete registration → approval → login → create internship flow
- Test student application → company review → student acceptance flow

3. **Boundary Testing**
- Test with 0 slots, 1 slot, 10 slots
- Test on last day of application period
- Test with Year 1 student applying for BASIC vs INTERMEDIATE

4. **Negative Testing**
- Invalid dates (close before open)
- Duplicate emails
- Invalid user types
- Empty required fields

5. **User Acceptance Testing**
- Have actual users (students, staff) try the system
- Verify it meets requirements
- Gather feedback on usability

We've created a comprehensive testing document with 86 test cases covering all these scenarios."

---

## 10. Troubleshooting

### Common Issues and Solutions

#### Issue 1: File Not Found Exception

**Error:**
```
java.io.FileNotFoundException: students.csv
```

**Cause:** CSV file path in application.properties is incorrect or file doesn't exist

**Solution:**
1. Check `application.properties` file:
```properties
filepath.student=data/students.csv
filepath.staff=data/staff.csv
filepath.rep=data/companyreps.csv
filepath.internship=data/internships.csv
```

2. Verify files exist in specified locations
3. Use absolute paths if relative paths don't work:
```properties
filepath.student=/full/path/to/students.csv
```

---

#### Issue 2: NumberFormatException

**Error:**
```
java.lang.NumberFormatException: For input string: "abc"
```

**Cause:** User entered non-numeric input where number expected

**Solution:** Already handled with try-catch:
```java
try {
    int year = Integer.parseInt(input);
} catch (NumberFormatException e) {
    System.out.println("Please enter a valid number");
}
```

---

#### Issue 3: NullPointerException

**Error:**
```
java.lang.NullPointerException
```

**Cause:** Accessing method/property on null object

**Common scenarios:**
1. `currentUser` is null after failed login
2. Internship preferred major is null
3. Application list not initialized

**Solution:** Add null checks:
```java
if (internship.getPreferredMajor() != null) {
    // Use major
}

if (student.getApplications() != null && !student.getApplications().isEmpty()) {
    // Process applications
}
```

---

#### Issue 4: IndexOutOfBoundsException

**Error:**
```
java.lang.IndexOutOfBoundsException: Index 5 out of bounds for length 3
```

**Cause:** Accessing ArrayList index that doesn't exist

**Solution:** Always check bounds:
```java
if (index >= 0 && index < list.size()) {
    element = list.get(index);
}
```

---

#### Issue 5: Date Parsing Exception

**Error:**
```
java.time.format.DateTimeParseException: Text '28/02/2025' could not be parsed
```

**Cause:** Date format doesn't match parser format

**Solution:** Use correct format or handle multiple formats:
```java
DateTimeFormatter[] formatters = {
    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
    DateTimeFormatter.ofPattern("d M yyyy")
};

for (DateTimeFormatter formatter : formatters) {
    try {
        return LocalDate.parse(dateString, formatter);
    } catch (Exception ignored) { }
}
```

---

## Quick Reference Cheat Sheet

### Key Classes
- **App.java** - Main entry point, global data, CSV operations
- **User.java** - Base class for all users
- **Student.java** - Student-specific data and operations
- **Internship.java** - Internship data
- **InternshipApplication.java** - Links student to internship
- **StudentController.java** - Handles student menu
- **ViewInternshipsAction.java** - Browse and apply logic

### Key Methods
- `main()` - Program entry point
- `verifyUserFromList()` - Login authentication
- `ReadFromCSV()` - Load data from files
- `WriteToCSV()` - Save data to files
- `applyForInternship()` - Process application with validation
- `showMenu()` - Display user-specific menu

### Key Concepts
- **Inheritance** - Student extends User
- **Polymorphism** - Different actions, same interface
- **Encapsulation** - Private fields, public getters/setters
- **Collections** - ArrayList for dynamic lists
- **Enums** - Fixed set of values (Status, Major, Level)
- **Streams** - Filtering and mapping collections
- **File I/O** - BufferedReader and FileWriter

### Important Workflows
1. **Login** → verify credentials → show menu → execute actions → logout
2. **Apply** → validate student → validate internship → create application → update data
3. **Approve** → find pending item → change status → save to CSV
4. **Register** → collect data → validate → create account (PENDING) → save

---

## Final Tips for Presentation

1. **Understand the "why"** - Not just "what" the code does, but WHY we made design choices
2. **Be honest about limitations** - Shows critical thinking
3. **Use examples** - Walk through actual user scenarios
4. **Draw diagrams** - Visual aids help explain architecture
5. **Practice common questions** - Especially OOP concepts
6. **Know your code** - Be able to explain any method if asked
7. **Connect to learning** - Show how project demonstrates course concepts

---

**Good luck with your presentation!**

You now have a complete understanding of the project from user perspective, code perspective, and architectural perspective. Use this guide to confidently discuss any aspect of the system.
