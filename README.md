# Internship Placement Management System

**CT2003: Object-Oriented Design & Programming**
**Academic Year 2025/26 Semester 1**
**Nanyang Technological University**

---

## Table of Contents
- [Overview](#overview)
- [Quick Start Guide](#quick-start-guide)
- [System Architecture](#system-architecture)
- [Key Features by User Role](#key-features-by-user-role)
- [OOP Principles Demonstrated](#oop-principles-demonstrated)
- [Data Persistence](#data-persistence)
- [Test Cases Coverage](#test-cases-coverage)
- [Project Structure](#project-structure)
- [Documentation](#documentation)

---

## Overview

The **Internship Placement Management System** is a Command-Line Interface (CLI) application designed to facilitate internship placement coordination between students, companies, and university career services. The system serves as a centralized hub where:

- **Students** can browse and apply for internship opportunities matching their academic profile
- **Company Representatives** can post internship opportunities and review student applications
- **Career Center Staff** can moderate company registrations and approve internship postings

This project demonstrates comprehensive application of Object-Oriented Design and Programming principles, including inheritance, polymorphism, encapsulation, abstraction, and multiple design patterns. The system implements role-based access control, data validation, and CSV-based persistence without using databases, JSON, or XML as per assignment requirements.

---

## Quick Start Guide

### Prerequisites
- **Java Development Kit (JDK)**: Version 8 or higher
- **IDE**: IntelliJ IDEA, Eclipse, NetBeans, or any Java-compatible IDE
- **Operating System**: Windows, macOS, or Linux

### How to Run

1. **Clone or Download the Repository**
   ```bash
   git clone <repository-url>
   cd NTU-CT2003-Assignment
   ```

2. **Open the Project**
   - Open the `InternshipSystem` folder in your preferred Java IDE
   - Ensure the source folder is set to `InternshipSystem/src`

3. **Run the Application**
   - Locate the main class: `team5/App.java`
   - Run the `main()` method
   - The application will load data from CSV files and display the login menu

4. **Login Credentials**

   **Test Accounts (Default Password: `password`)**

   | User Type | User ID | Role |
   |-----------|---------|------|
   | Student | U1234567A | Year 1-4 Students |
   | Career Center Staff | staff@ntu.edu.sg | Approve companies & internships |
   | Company Representative | comrep@company.com | Post internships (must be approved first) |

   > **Note**: Check `sample_student_list.csv`, `sample_staff_list.csv`, and `sample_company_representative_list.csv` for all available test accounts.

### Data Files Location
All CSV data files are located in:
```
InternshipSystem/src/
├── sample_student_list.csv
├── sample_staff_list.csv
├── sample_company_representative_list.csv
├── sample_internship_list.csv
├── internship_applications_list.csv
└── application.properties (file paths configuration)
```

---

## System Architecture

### Design Pattern: Modified MVC + Command Pattern

The system follows a layered architecture that separates concerns across multiple components:

```
┌─────────────────────────────────────────────────┐
│              User Interface Layer               │
│  (ConsoleBoundary, InternshipBoundary, etc.)   │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│              Controller Layer                   │
│  (StudentController, CompanyRepController,      │
│   CareerCenterStaffController)                  │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│          Action Layer (Command Pattern)         │
│  (ViewInternshipsAction, CreateInternshipAction,│
│   ReviewApplicationsAction, etc.)               │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│              Model Layer (Entities)             │
│  (User, Student, CompanyRep, Internship,        │
│   InternshipApplication, etc.)                  │
└────────────────┬────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────────┐
│          Data Persistence Layer                 │
│        (CsvFileBoundary, CSV Files)             │
└─────────────────────────────────────────────────┘
```

### Core Components

1. **Models (Entity Classes)**: Represent domain objects with encapsulated data and behavior
   - `User` (abstract base class)
   - `Student`, `CompanyRep`, `CareerCenterStaff` (inherit from User)
   - `Internship`, `InternshipApplication`

2. **Controllers**: Manage user sessions and delegate actions
   - `UserController` (abstract base)
   - Role-specific controllers for each user type

3. **Boundaries (Views)**: Handle all user input/output
   - `ConsoleBoundary`: Base utility class for console I/O
   - `InternshipBoundary`: Specialized internship displays
   - `CsvFileBoundary`: CSV file operations

4. **Actions (Command Pattern)**: Encapsulate specific features
   - Each user action is a separate class implementing role-specific interfaces
   - Examples: `ViewInternshipsAction`, `CreateInternshipAction`, `ReviewApplicationsAction`

5. **Utilities**: Support classes for filtering, enums, and interfaces
   - Enums for type safety (UserType, InternshipStatus, etc.)
   - Filter classes for advanced internship searches
   - Repository interfaces for data operations

---

## Key Features by User Role

### Student Features

**View & Apply for Internships**
- Browse internships filtered automatically by major and year eligibility
- Advanced filtering options (major, level, application dates)
- Pagination (5 internships per page)
- Apply for up to 3 internships concurrently
- Comprehensive validation (no duplicates, within application period, not already employed)

**Check Application Status**
- View all submitted applications with current status
- Track application lifecycle: PENDING → SUCCESSFUL/UNSUCCESSFUL → ACCEPTED
- Accept successful internship offers
- Automatic withdrawal of other applications upon acceptance

**Business Rules**
- Year 1-2 students: Can only apply for BASIC level internships
- Year 3-4 students: Can apply for any level (BASIC, INTERMEDIATE, ADVANCED)
- Maximum 3 concurrent applications
- Can only accept 1 internship offer
- Can only view internships matching their major and status = APPROVED

### Company Representative Features

**Registration & Account Management**
- Public registration system (no login required)
- Account status: PENDING → APPROVED/REJECTED by Career Center Staff
- Only APPROVED accounts can login
- Can re-submit after REJECTED status

**Create Internship Opportunities**
- Create up to 5 internship postings per representative
- Specify: Title, Description, Level, Preferred Major, Dates, Slots (max 10)
- Status starts as PENDING (requires staff approval)
- Multi-step form with validation and cancellation option (enter 0 to cancel)

**Manage Posted Internships**
- View all created internships with status
- Update/Delete PENDING or REJECTED internships
- Cannot modify APPROVED internships (policy enforcement)

**Review Student Applications**
- View applications grouped by internship
- See student details (name, major, year of study, application date)
- Approve or Reject applications
- System checks if student is already employed before approval
- Track slot availability (internship becomes FILLED when slots = 0)

### Career Center Staff Features

**Review Company Registrations**
- Paginated list of PENDING company representative registrations
- View full company details (name, department, position, email)
- Approve or Reject registrations
- Only APPROVED representatives can login

**Review Internship Submissions**
- Paginated list of PENDING internship submissions
- View complete internship details
- Approve (makes visible to eligible students) or Reject
- Controls which internships students can see and apply for

**System-Wide Internship View**
- View all internships regardless of status
- Advanced filtering options
- Read-only access for monitoring purposes

### Common Features (All Users)

- **Login/Logout**: Session-based authentication with 3 password attempts
- **Change Password**: 3-stage verification process (old password, new password, confirm password)
  - 2 attempts per stage
  - Forced re-login after successful password change
  - Updates persisted to CSV immediately

---

## OOP Principles Demonstrated

### 1. Inheritance
- **Class Hierarchy**: `User` (base class) → `Student`, `CompanyRep`, `CareerCenterStaff` (derived classes)
- **Code Reuse**: Common user attributes (userID, name, email, password) inherited from User
- **Specialization**: Each subclass adds role-specific attributes and behaviors

**Example**: [User.java](InternshipSystem/src/team5/User.java) is the parent class for all user types

### 2. Polymorphism
- **Method Overriding**: `UserController.showMenu()` is abstract and implemented differently by each controller
- **Dynamic Binding**: Appropriate controller menu displayed based on user type at runtime
- **Interface Implementation**: Action interfaces implemented by multiple concrete action classes

**Example**: [UserController.java:71-72](InternshipSystem/src/team5/controllers/UserController.java#L71-L72) - abstract `showMenu()` method

### 3. Encapsulation
- **Data Hiding**: All entity class fields are private
- **Controlled Access**: Public getters/setters provide controlled access to private data
- **Validation**: Setters include validation logic (e.g., date validation, slot limits)

**Example**: All entity classes (User, Student, Internship, etc.) use private fields with public accessors

### 4. Abstraction
- **Abstract Classes**: `UserController` defines common controller behavior
- **Interfaces**: `StudentAction`, `CompanyRepAction`, `StaffAction`, `CsvRepository`
- **Separation of Concerns**: Implementation details hidden behind clean interfaces

**Example**: [StudentAction.java](InternshipSystem/src/team5/studentactions/StudentAction.java) - interface for all student actions

### 5. Design Patterns Applied

**Command Pattern**
- Each user action is a separate command class
- Actions encapsulate requests as objects
- Enables parameterization, queuing, and logging of operations
- Location: `studentactions/`, `companyrepactions/`, `staffactions/` packages

**Repository Pattern**
- Abstracts data access through interfaces
- `CsvRepository` interface separates business logic from data persistence
- Enables easy switching of data sources if needed

**Strategy Pattern**
- Filter system uses strategy pattern for flexible filtering
- `InternshipFilter` applies different filter criteria dynamically
- Location: `filters/` package

**MVC (Model-View-Controller)**
- Models: Entity classes (User, Internship, etc.)
- Views: Boundary classes (ConsoleBoundary, InternshipBoundary)
- Controllers: Controller classes (StudentController, etc.)

**Single Responsibility Principle**
- Each action class handles one specific feature
- Boundaries handle only I/O operations
- Controllers only manage navigation and delegation

---

## Data Persistence

### CSV-Based Storage
All data is stored in CSV files without using databases, JSON, or XML as per assignment requirements.

### File Structure

**1. sample_student_list.csv**
```
StudentID,Name,Major,Year,Email,Password
U1234567A,John Doe,CS,2,john@ntu.edu.sg,password
```

**2. sample_staff_list.csv**
```
StaffID,Name,Role,Department,Email,Password
staff001,Jane Smith,Career Counselor,Career Center,jane@ntu.edu.sg,password
```

**3. sample_company_representative_list.csv**
```
CompanyRepID,Name,CompanyName,Department,Position,Email,Status,Password
rep@company.com,Alice Johnson,TechCorp,HR,HR Manager,rep@company.com,APPROVED,password
```

**4. sample_internship_list.csv**
```
InternshipID,Title,Description,Level,PreferredMajor,OpenDate,CloseDate,Status,CompanyName,CompanyRep,Slots
I1234,Software Engineer Intern,Full-stack development,INTERMEDIATE,CS,01 01 2025,31 12 2025,APPROVED,TechCorp,rep@company.com,5
```

**5. internship_applications_list.csv**
```
ApplicationID,InternshipID,StudentName,ApplicationDate,Status
A5678,I1234,John Doe,15 01 2025,PENDING
```

### Data Operations

**Loading Data**
- CSV files are read at application startup
- Data parsed into ArrayList collections
- Order: Internships → Users → Applications (dependency management)

**Saving Data**
- Immediate write after each modification
- Two modes: Overwrite entire file OR Append single record
- Consistency maintained through synchronized write operations

**Date Formats**
- Display Format: `DD/MM/YYYY` (user-friendly)
- Database Format: `dd MM yyyy` (CSV storage)
- Parser supports multiple input formats for flexibility

**ID Generation**
- Format: Prefix + 4-digit random number
- Prefixes: `I` (Internship), `A` (Application)
- Collision detection with retry logic (200 attempts, then incremental)

### Configuration
File paths are configured in `application.properties`:
```properties
filepath.student=InternshipSystem/src/sample_student_list.csv
filepath.staff=InternshipSystem/src/sample_staff_list.csv
filepath.rep=InternshipSystem/src/sample_company_representative_list.csv
filepath.internship=InternshipSystem/src/sample_internship_list.csv
filepath.internshipApplication=InternshipSystem/src/internship_applications_list.csv
```

---

## Test Cases Coverage

The system has been thoroughly tested against all 15 required test cases from the assignment specification:

### User Authentication & Account Management (Test Cases 1-5)
1. ✅ Valid user login for all user types
2. ✅ Invalid user ID detection with appropriate error messages
3. ✅ Incorrect password handling with 3-attempt limit
4. ✅ Password change functionality with re-login enforcement
5. ✅ Company Representative account approval workflow

### Internship Visibility & Application (Test Cases 6-8)
6. ✅ Internship visibility filtering by user profile and eligibility
7. ✅ Application eligibility validation (year, major, level constraints)
8. ✅ Single internship acceptance with automatic withdrawal of other applications

### Company Representative Features (Test Cases 9-11)
9. ✅ Internship opportunity creation with validation (max 5 per rep, max 10 slots)
10. ✅ Internship approval status tracking (PENDING → APPROVED/REJECTED)
11. ✅ Edit restriction enforcement for APPROVED internships

### Application Management (Test Cases 12-14)
12. ✅ Student application retrieval and slot management
13. ✅ Placement confirmation status updates (SUCCESSFUL → ACCEPTED)
14. ✅ CRUD operations for internship listings (Create, Read, Update, Delete)

### Career Center Staff Features (Test Case 15)
15. ✅ Internship opportunity approval/rejection workflow

### Additional Validations
- Maximum concurrent applications (3 per student)
- Year-based internship level restrictions
- Duplicate application prevention
- Application date range validation (within open/close dates)
- Employment status checking (cannot apply if already employed)
- Slot availability tracking (status → FILLED when slots = 0)
- Company representative limit enforcement (max 5 internships)

---

## Project Structure

```
NTU-CT2003-Assignment/
│
├── README.md                          # This file
├── diagrams/                          # UML diagrams and design documents
│
└── InternshipSystem/
    └── src/
        ├── application.properties     # Configuration file for CSV paths
        ├── sample_student_list.csv    # Student data
        ├── sample_staff_list.csv      # Career center staff data
        ├── sample_company_representative_list.csv  # Company rep data
        ├── sample_internship_list.csv # Internship opportunities data
        ├── internship_applications_list.csv  # Student applications data
        │
        └── team5/
            ├── App.java               # Main entry point
            │
            ├── User.java              # Base user entity
            ├── Student.java           # Student entity
            ├── CompanyRep.java        # Company representative entity
            ├── CareerCenterStaff.java # Career center staff entity
            ├── Internship.java        # Internship entity
            ├── InternshipApplication.java  # Application entity
            │
            ├── boundaries/            # View layer
            │   ├── ConsoleBoundary.java     # Base console I/O utilities
            │   ├── InternshipBoundary.java  # Internship-specific displays
            │   └── CsvFileBoundary.java     # CSV persistence operations
            │
            ├── controllers/           # Controller layer
            │   ├── UserController.java           # Abstract base controller
            │   ├── StudentController.java        # Student session controller
            │   ├── CompanyRepController.java     # Company rep controller
            │   └── CareerCenterStaffController.java  # Staff controller
            │
            ├── studentactions/        # Student features (Command Pattern)
            │   ├── StudentAction.java            # Student action interface
            │   ├── ViewInternshipsAction.java    # Browse & apply feature
            │   └── CheckApplicationStatusAction.java  # Track & accept feature
            │
            ├── companyrepactions/     # Company rep features
            │   ├── CompanyRepAction.java         # Company action interface
            │   ├── CreateInternshipAction.java   # Create internship feature
            │   ├── ListOwnInternshipsAction.java # Manage internships feature
            │   └── ReviewApplicationsAction.java # Review students feature
            │
            ├── staffactions/          # Career center staff features
            │   ├── StaffAction.java              # Staff action interface
            │   ├── ReviewCompanyRegistrationsAction.java
            │   ├── ReviewInternshipSubmissionsAction.java
            │   └── ViewInternshipsAction.java
            │
            ├── registration/          # Registration system
            │   └── CompanyRepRegistrationHandler.java
            │
            ├── enums/                 # Type-safe enumerations
            │   ├── UserType.java                 # STUDENT, CCSTAFF, COMREP
            │   ├── UserAccountStatus.java        # PENDING, APPROVED, REJECTED
            │   ├── InternshipStatus.java         # For internship lifecycle
            │   ├── InternshipApplicationStatus.java  # For application lifecycle
            │   ├── InternshipLevel.java          # BASIC, INTERMEDIATE, ADVANCED
            │   ├── StudentMajor.java             # CS, DSAI, CE, IEM, COMP
            │   ├── CSVType.java                  # CSV file type identifiers
            │   └── InternshipFilterOption.java   # Filter criteria types
            │
            ├── filters/               # Filtering utilities
            │   ├── InternshipFilter.java         # Filter application logic
            │   ├── InternshipFilterCriteria.java # Filter parameters
            │   └── InternshipFilterPrompt.java   # Filter selection UI
            │
            └── interfaces/            # Abstraction layer
                ├── CsvRepository.java            # Main repository interface
                ├── InternshipCsvRepository.java  # Internship data operations
                └── ApplicationCsvRepository.java # Application data operations
```

---

## Documentation

### Repository
- **GitHub Repository**: [View on GitHub](<your-github-link>)
- **Branch**: `main` (or `harish` for development)

### Additional Documentation
- **Detailed Report**: 12-page comprehensive report (separate document)
  - Detailed UML Class Diagram with annotations
  - Design considerations and trade-offs
  - Extensibility and maintainability analysis
  - Reflection on learning outcomes
- **UML Diagrams**: Located in `diagrams/` folder
- **Code Documentation**: Javadoc comments throughout source code

### Key Implementation Notes

**Business Constants**
- `MAX_APPLICATIONS = 3` (Student)
- `MAX_INTERNSHIPS_PER_REP = 5` (Company Rep)
- `MAX_SLOTS_NUM = 10` (Internship)
- `PAGE_SIZE = 5` (Pagination)
- `MAX_PASSWORD_ATTEMPTS = 2` (Per stage during password change)

**Default Credentials**
- All initial accounts use password: `password`
- Students: University email addresses
- Staff: NTU email addresses
- Company Reps: Company email addresses

**Session Management**
- Data reloaded on each application start
- No persistent session across restarts
- Logout returns to main menu
- Password change forces re-login

---

**Developed for CT2003: Object-Oriented Design & Programming**
**Nanyang Technological University, College of Computing and Data Science**
**Academic Year 2025/26 Semester 1**
