# UML Class Diagram - Detailed Explanation
## NTU Internship Management System

---

## Table of Contents
1. [System Overview](#1-system-overview)
2. [Architecture Overview](#2-architecture-overview)
3. [Layer-by-Layer Explanation](#3-layer-by-layer-explanation)
4. [Relationship Types Explained](#4-relationship-types-explained)
5. [Design Patterns Implementation](#5-design-patterns-implementation)
6. [Data Flow Through the System](#6-data-flow-through-the-system)
7. [Key Business Logic](#7-key-business-logic)
8. [How to Read the Diagram](#8-how-to-read-the-diagram)

---

## 1. System Overview

### What is This System?
The **NTU Internship Management System** is a console-based Java application that manages the complete lifecycle of internship opportunities at Nanyang Technological University. It facilitates interactions between three types of users:

1. **Students** - Browse internships, apply, and accept offers
2. **Company Representatives** - Create internships, review applications
3. **Career Center Staff** - Approve companies and internships

### Core Functionality
- User authentication and authorization
- Internship lifecycle management (create, approve, fill)
- Application management (apply, review, accept/reject)
- Filtering and searching capabilities
- CSV-based data persistence

---

## 2. Architecture Overview

### Architectural Pattern: MVC (Model-View-Controller)

The system follows a **layered MVC architecture** with additional action and filter layers:

```
┌─────────────────────────────────────────────────────────┐
│                    Application Layer                     │
│                      (App.java)                         │
│              Global State & Orchestration               │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                   Controller Layer                       │
│   StudentController | StaffController | RepController    │
│              Route requests to actions                   │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                     Action Layer                         │
│        ViewInternships | CreateInternship | Review       │
│            Business Logic & Use Cases                    │
└─────────────────────────────────────────────────────────┘
                          ↓
┌──────────────────┬──────────────────┬───────────────────┐
│   Entity Layer   │  Boundary Layer  │   Filter Layer    │
│   (Models)       │    (View/IO)     │   (Utilities)     │
│  User, Student,  │  ConsoleBoundary │ InternshipFilter  │
│  Internship, etc │  InternshipUI    │ FilterCriteria    │
└──────────────────┴──────────────────┴───────────────────┘
```

### Why This Architecture?

1. **Separation of Concerns:** Each layer has a specific responsibility
2. **Maintainability:** Changes in one layer don't affect others
3. **Testability:** Each component can be tested independently
4. **Scalability:** New features can be added by creating new actions
5. **Reusability:** Boundaries and filters are reused across actions

---

## 3. Layer-by-Layer Explanation

### 3.1 Entity Layer (Model)

**Purpose:** Represents the domain model - the "what" of the system

#### Core Entities:

**User Hierarchy (Inheritance Tree):**
```
           User (abstract)
           ├─ userID
           ├─ name
           ├─ email
           ├─ password
           └─ userType
              │
              ├─── Student
              │    ├─ major
              │    ├─ yearOfStudy
              │    ├─ employedStatus
              │    └─ appliedInternships[]
              │
              ├─── CareerCenterStaff
              │    ├─ role
              │    └─ department
              │
              └─── CompanyRep
                   ├─ companyName
                   ├─ department
                   ├─ position
                   ├─ accountStatus
                   └─ createdInternships[]
```

**Why Inheritance Here?**
- All users share common attributes (ID, name, email, password)
- Each user type has specialized attributes and behaviors
- Polymorphism allows treating all users uniformly in App.currentUser

**Internship Entity:**
```
Internship
├─ internshipId
├─ title
├─ description
├─ internshipLevel (BASIC/INTERMEDIATE/ADVANCED)
├─ preferredMajor
├─ applicationOpenDate
├─ applicationCloseDate
├─ internshipStatus (PENDING/APPROVED/REJECTED/FILLED)
├─ companyName
├─ companyRep
└─ numOfSlots (max 10)
```

**InternshipApplication (Junction Entity):**
```
InternshipApplication
├─ applicationID
├─ internship ──────→ References Internship
├─ student ─────────→ References Student
├─ appliedDate
└─ status (PENDING/SUCCESSFUL/UNSUCCESSFUL)
```

**Key Relationship:** This is a **many-to-many relationship handler**
- One Student can have many Applications
- One Internship can have many Applications
- InternshipApplication bridges them

---

### 3.2 Controller Layer

**Purpose:** Routes user requests to appropriate actions - the "traffic cop"

#### Three Controllers:

```
StudentController
├─ Holds: ViewInternshipsAction
├─ Holds: CheckApplicationStatusAction
└─ Method: showMenu(Student) - displays menu and routes choice

CareerCenterStaffController
├─ Holds: ViewInternshipsAction
├─ Holds: ReviewInternshipSubmissionsAction
├─ Holds: ReviewCompanyRegistrationsAction
└─ Method: showMenu(CareerCenterStaff)

CompanyRepController
├─ Holds: CreateInternshipAction
├─ Holds: ListOwnInternshipsAction
├─ Holds: ReviewApplicationsAction
├─ Holds: FileBoundary (for persistence)
└─ Method: showMenu(CompanyRep)
```

**Design Pattern Used:** **Command Pattern**
- Controllers hold references to action objects (commands)
- User's menu choice triggers execution of specific action
- Actions encapsulate all logic for that operation

**Example Flow:**
```
1. User logs in as Student
2. App creates StudentController
3. Controller displays menu
4. User selects "1. View Internships"
5. Controller calls viewInternshipsAction.run(student)
6. Action executes business logic
7. Returns to menu
```

---

### 3.3 Action Layer

**Purpose:** Implements business logic and use cases - the "brain"

#### Action Interfaces:
```
┌─────────────────────┐
│   StudentAction     │
│  + run(Student)     │
└─────────────────────┘
         △
         │ implements
         ├─── ViewInternshipsAction
         └─── CheckApplicationStatusAction

┌─────────────────────────┐
│   StaffAction           │
│  + run(CareerCenterStaff)│
└─────────────────────────┘
         △
         │ implements
         ├─── ViewInternshipsAction
         ├─── ReviewInternshipSubmissionsAction
         └─── ReviewCompanyRegistrationsAction

┌─────────────────────┐
│  CompanyRepAction   │
│  + run(CompanyRep)  │
└─────────────────────┘
         △
         │ implements
         ├─── CreateInternshipAction
         ├─── ListOwnInternshipsAction
         └─── ReviewApplicationsAction
```

**Why Interfaces?**
- **Polymorphism:** Controllers can hold different actions through interface reference
- **Extensibility:** New actions can be added without changing controllers
- **Testability:** Mock actions can be injected for testing

#### Key Actions Explained:

**ViewInternshipsAction (Student):**
```
Purpose: Display internships students can apply to
Business Logic:
1. Filter internships to only show APPROVED ones
2. Filter by student's major compatibility
3. If student is year 1-2, show only BASIC level
4. Apply additional filters (user-selected)
5. Paginate results (5 per page)
6. Allow viewing details and applying
7. Check eligibility before allowing application:
   - Not already applied
   - Within application period
   - Not at max 3 applications
   - Not already employed
```

**CreateInternshipAction (CompanyRep):**
```
Purpose: Allow company reps to create new internships
Business Logic:
1. Check if rep has reached max 5 internships
2. Prompt for all internship details
3. Validate dates (close date after open date)
4. Generate unique internship ID (I####)
5. Set status to PENDING
6. Save to CSV file
7. Add to global internship list
8. Add to company rep's created internships list
```

**ReviewApplicationsAction (CompanyRep):**
```
Purpose: Review and approve/reject student applications
Business Logic:
1. Get all approved internships for this rep
2. Find all pending applications for these internships
3. Display internships with pending application counts
4. Allow rep to select an internship
5. Show all pending applications with student details
6. Allow rep to approve (SUCCESSFUL) or reject (UNSUCCESSFUL)
7. Check if student is already employed before approving
8. Update application status
```

---

### 3.4 Boundary Layer (View)

**Purpose:** Handle all user interface and I/O - the "face"

```
ConsoleBoundary (Abstract Base)
├─ Provides: Input prompting methods
├─ Provides: Validation methods
├─ Provides: Output formatting methods
├─ Provides: Error message methods
└─ Constants: PAGE_SIZE = 5

InternshipBoundary (Extends ConsoleBoundary)
├─ Provides: Internship-specific display methods
├─ printInternshipList()
├─ printInternshipDetails()
├─ promptInternshipLevel()
├─ promptPreferredMajor()
└─ displayUpdateInternshipMenu()
```

**Why Abstract ConsoleBoundary?**
- **Template Method Pattern:** Provides common methods all boundaries need
- **Code Reuse:** Date parsing, input validation, error handling shared
- **Consistency:** All console I/O follows same format and style

**FileBoundary Interface:**
```
FileBoundary (Interface)
├─ writeInternship()
├─ appendInternship()
├─ writeInternshipApplications()
└─ appendInternshipApplicationToCsv()
    │
    └─── CsvFileBoundary (Implementation)
```

**Why Interface for File Operations?**
- **Strategy Pattern:** Can swap CSV for database later
- **Dependency Injection:** Actions receive FileBoundary, not concrete implementation
- **Testing:** Can inject mock file boundary for unit tests

---

### 3.5 Filter Layer

**Purpose:** Provide dynamic filtering capabilities

```
InternshipFilter (Static Utility)
└─ apply(List<Internship>, InternshipFilterCriteria)
      └─ Returns filtered list

InternshipFilterCriteria (Data Class)
├─ preferredMajor: StudentMajor
├─ internshipLevel: InternshipLevel
├─ internshipStatus: InternshipStatus
├─ applicationOpenFrom: LocalDate
├─ applicationCloseTo: LocalDate
└─ isEmpty(): boolean

InternshipFilterPrompt (UI Helper)
└─ prompt(List<InternshipFilterOption>)
      └─ Returns InternshipFilterCriteria
```

**How Filtering Works:**
```
1. Action determines which filters are available for user
2. Action calls InternshipFilterPrompt.prompt()
3. User selects which filters to apply
4. FilterPrompt creates FilterCriteria object
5. Action calls InternshipFilter.apply(internships, criteria)
6. Filter returns new filtered list
7. Action displays filtered results
```

**Benefits:**
- **Reusable:** Multiple actions use same filter logic
- **Flexible:** Easy to add new filter criteria
- **Separation:** Filter logic separate from business logic

---

### 3.6 Application Layer

**App Class - The Orchestrator:**

```
App (Singleton-style with static members)
├─ Global State:
│  ├─ studentList: ArrayList<Student>
│  ├─ staffList: ArrayList<CareerCenterStaff>
│  ├─ compRepList: ArrayList<CompanyRep>
│  ├─ internshipList: ArrayList<Internship>
│  ├─ internshipApplicationList: ArrayList<InternshipApplication>
│  ├─ currentUser: User
│  └─ sc: Scanner (shared scanner)
│
├─ Data Management:
│  ├─ ReadFromCSV()
│  ├─ WriteToCSV()
│  └─ loadEnvironmentVariables()
│
├─ Authentication:
│  ├─ verifyUserFromList()
│  └─ updatePasswordAndPersist()
│
├─ Utilities:
│  ├─ generateUniqueId()
│  └─ idExists()
│
└─ Main Flow:
   1. Load environment variables
   2. Loop:
      a. Clear and reload all data from CSV
      b. Show login menu
      c. Authenticate user
      d. Create appropriate controller
      e. Show controller menu
      f. Handle logout
   3. Exit
```

**Why This Design?**
- **Central State Management:** All data accessible from one place
- **Simplicity:** For console application, global state is acceptable
- **Data Consistency:** Reload from CSV each loop ensures fresh data

**Note:** This is an **anti-pattern** for larger applications (should use dependency injection), but acceptable for small console apps.

---

## 4. Relationship Types Explained

### 4.1 Inheritance (Generalization)

**Visual:** Solid line with hollow triangle → Child --|> Parent

**Example:** Student extends User

```
┌──────────────┐
│     User     │
│  (abstract)  │
└──────────────┘
       △
       │
       │
┌──────────────┐
│   Student    │
└──────────────┘
```

**What it means:**
- Student **IS-A** User
- Student inherits all attributes and methods from User
- Student can add specialized attributes (major, yearOfStudy)
- Student can override User methods if needed
- Polymorphism: Can treat Student as User

**When to use:**
- Clear "is-a" relationship
- Shared attributes/behavior
- Need polymorphism

---

### 4.2 Interface Implementation (Realization)

**Visual:** Dashed line with hollow triangle → Implementation ..|> Interface

**Example:** ViewInternshipsAction implements StudentAction

```
┌───────────────────┐
│  StudentAction    │
│   <<interface>>   │
│  + run(Student)   │
└───────────────────┘
         △
         ┊
         ┊
┌──────────────────────┐
│ ViewInternshipsAction│
│  + run(Student)      │
└──────────────────────┘
```

**What it means:**
- ViewInternshipsAction **must implement** run(Student) method
- Can be used wherever StudentAction is expected
- Defines a contract

**When to use:**
- Define behavior contract
- Multiple unrelated classes need same interface
- Enable polymorphism without inheritance

---

### 4.3 Composition (Strong Ownership)

**Visual:** Solid line with filled diamond → Container *-- Contained

**Example:** Student contains InternshipApplication

```
┌──────────────┐     *  ┌────────────────────────┐
│   Student    │───────*│ InternshipApplication  │
└──────────────┘ 1  0..3└────────────────────────┘
```

**What it means:**
- Student **HAS** InternshipApplications
- InternshipApplications **cannot exist** without Student
- When Student is deleted, applications are deleted (cascading)
- Strong lifecycle dependency

**When to use:**
- Contained object has no meaning without container
- Container manages lifecycle of contained
- Part-whole relationship

---

### 4.4 Aggregation (Weak Ownership)

**Visual:** Solid line with hollow diamond → Container o-- Contained

**Example:** StudentController aggregates ViewInternshipsAction

```
┌─────────────────────┐     o  ┌──────────────────────┐
│  StudentController  │───────*│ ViewInternshipsAction│
└─────────────────────┘ 1    1 └──────────────────────┘
```

**What it means:**
- Controller **HAS** Action
- Action **can exist independently** of Controller
- When Controller is deleted, Action is not necessarily deleted
- Weaker lifecycle dependency than composition

**When to use:**
- Container uses contained object
- Contained can exist independently
- Shared ownership possible

---

### 4.5 Dependency (Usage)

**Visual:** Dashed line with open arrow → Dependent ..> Dependency

**Example:** ViewInternshipsAction uses InternshipBoundary

```
┌──────────────────────┐       ┌───────────────────┐
│ ViewInternshipsAction│ - - →│ InternshipBoundary│
└──────────────────────┘       └───────────────────┘
```

**What it means:**
- Action **USES** Boundary
- Action calls Boundary's static methods
- No ownership relationship
- Temporary relationship (during method call)

**When to use:**
- One class uses another's methods
- Parameter type or local variable
- Method calls

---

### 4.6 Association (Reference)

**Visual:** Solid line with open arrow → Class --> Enum

**Example:** User has UserType

```
┌──────────────┐          ┌──────────────┐
│     User     │  ──────→ │   UserType   │
│              │          │   <<enum>>   │
└──────────────┘          └──────────────┘
```

**What it means:**
- User has a reference to UserType enum
- Simple relationship
- User stores UserType value

**When to use:**
- Attribute type
- Simple reference
- Enum usage

---

## 5. Design Patterns Implementation

### 5.1 MVC (Model-View-Controller)

**Structure:**
```
Model (Entity)         View (Boundary)        Controller
     │                       │                    │
     │                       │                    │
  Student  ←───────────  displays  ←──────  StudentController
     │                       │                    │
     └───────────────────────┴────────────────────┘
               Business logic flows
```

**Benefits:**
- Separation of concerns
- Can change UI without touching model
- Can change model without touching UI
- Multiple views can display same model

**In This System:**
- **Model:** User, Student, Internship, etc.
- **View:** ConsoleBoundary, InternshipBoundary, CsvFileBoundary
- **Controller:** StudentController, StaffController, CompanyRepController

---

### 5.2 Command Pattern

**Structure:**
```
   Controller (Invoker)
         │
         │ has
         ↓
   Action (Command Interface)
         △
         │ implements
         │
   ConcreteAction (Command)
         │
         │ operates on
         ↓
   Student (Receiver)
```

**How it works:**
1. Controller holds reference to Action interface
2. User makes menu choice
3. Controller invokes action.run(receiver)
4. Action executes logic on receiver

**Benefits:**
- Decouples sender from receiver
- Easy to add new commands
- Can queue or log commands
- Supports undo/redo (if implemented)

**In This System:**
- **Command Interface:** StudentAction, StaffAction, CompanyRepAction
- **Concrete Commands:** ViewInternshipsAction, CreateInternshipAction, etc.
- **Invoker:** Controllers
- **Receiver:** User entities (Student, Staff, CompanyRep)

---

### 5.3 Strategy Pattern

**Structure:**
```
   CreateInternshipAction (Context)
         │
         │ uses
         ↓
   FileBoundary (Strategy Interface)
         △
         │ implements
         │
   CsvFileBoundary (Concrete Strategy)
```

**How it works:**
1. Context (Action) receives strategy through constructor (dependency injection)
2. Context calls strategy's methods
3. Strategy implementation can be swapped at runtime

**Benefits:**
- Algorithm can be changed independently
- Easy to add new strategies
- Better than if-else for algorithm selection

**In This System:**
- **Strategy Interface:** FileBoundary
- **Concrete Strategy:** CsvFileBoundary
- **Context:** CreateInternshipAction, ListOwnInternshipsAction, CompanyRepController
- **Future:** Could add DatabaseBoundary, JsonFileBoundary

---

### 5.4 Template Method Pattern

**Structure:**
```
   ConsoleBoundary (Abstract Template)
   ├─ promptUserInput() (template method)
   ├─ parseDate() (common method)
   └─ clearScreen() (common method)
         △
         │ extends
         │
   InternshipBoundary (Concrete Class)
   ├─ printInternshipList() (specific method)
   └─ promptInternshipLevel() (specific method)
```

**How it works:**
1. Abstract class defines template methods
2. Subclass inherits and uses these methods
3. Subclass can add specialized methods

**Benefits:**
- Code reuse
- Consistent behavior
- Common logic in one place

**In This System:**
- **Template:** ConsoleBoundary
- **Concrete:** InternshipBoundary
- **Shared methods:** Input prompting, validation, formatting

---

### 5.5 Factory Method Pattern

**Structure:**
```
App.generateUniqueId(prefix, existingIds[])
     │
     ├─ If prefix = "I" → Generate I0001, I0002, ...
     └─ If prefix = "A" → Generate A0001, A0002, ...
```

**How it works:**
1. Caller requests new ID with prefix
2. Factory method generates unique ID
3. Returns formatted ID string

**Benefits:**
- Centralized ID generation
- Ensures uniqueness
- Consistent format

**In This System:**
- **Factory Method:** App.generateUniqueId()
- **Products:** Internship IDs (I####), Application IDs (A####)

---

## 6. Data Flow Through the System

### 6.1 User Login Flow

```
1. App.main()
   ↓
2. Display login menu
   ↓
3. User enters credentials
   ↓
4. App.verifyUserFromList(userType, email, password)
   ├─ Search in appropriate list (studentList, staffList, compRepList)
   ├─ Check password match
   └─ Check account status (for CompanyRep)
   ↓
5. If valid: Set App.currentUser
   ↓
6. Create appropriate controller
   ├─ Student → StudentController
   ├─ Staff → CareerCenterStaffController
   └─ CompanyRep → CompanyRepController
   ↓
7. controller.showMenu(user)
```

---

### 6.2 Student Applies for Internship Flow

```
1. Student selects "View Internships"
   ↓
2. StudentController.showMenu() calls viewInternshipsAction.run(student)
   ↓
3. ViewInternshipsAction.run():
   ├─ a. Get eligible internships
   │    ├─ Filter: status = APPROVED
   │    ├─ Filter: major compatible with student
   │    └─ Filter: if year 1-2, only BASIC level
   │
   ├─ b. Prompt for additional filters
   │    ├─ InternshipFilterPrompt.prompt()
   │    └─ InternshipFilter.apply(internships, criteria)
   │
   ├─ c. Display paginated list (InternshipBoundary)
   │    └─ 5 per page, navigation options
   │
   ├─ d. User selects internship
   │
   ├─ e. Display details (InternshipBoundary)
   │
   ├─ f. User chooses "Apply"
   │
   └─ g. applyForInternship():
        ├─ Check: Already applied?
        ├─ Check: Within application period?
        ├─ Check: At max 3 applications?
        ├─ Check: Already employed?
        │
        ├─ If all pass:
        │  ├─ Generate application ID (App.generateUniqueId("A"))
        │  ├─ Create InternshipApplication object
        │  ├─ Set status = PENDING
        │  ├─ Add to App.internshipApplicationList
        │  ├─ Add to student.appliedInternships
        │  ├─ Save to CSV (appendInternshipApplicationToCsv)
        │  └─ Display success message
        │
        └─ If fail: Display error message
```

---

### 6.3 Company Rep Creates Internship Flow

```
1. CompanyRep selects "Create Internship"
   ↓
2. CompanyRepController.showMenu() calls createInternshipAction.run(companyRep)
   ↓
3. CreateInternshipAction.run():
   ├─ a. Check if max 5 internships reached
   │    └─ If yes: Display error, return
   │
   ├─ b. Prompt for internship details:
   │    ├─ Title (InternshipBoundary.promptFormInput())
   │    ├─ Description
   │    ├─ Level (InternshipBoundary.promptInternshipLevel())
   │    ├─ Preferred Major (InternshipBoundary.promptPreferredMajor())
   │    ├─ Open Date (ConsoleBoundary.promptDate())
   │    ├─ Close Date (validate after open date)
   │    └─ Slots (InternshipBoundary.promptNumOfSlots(), max 10)
   │
   ├─ c. Generate unique ID:
   │    └─ App.generateUniqueId("I", existingIds)
   │
   ├─ d. Create Internship object:
   │    ├─ Set internshipStatus = PENDING
   │    ├─ Set companyName = companyRep.getCompanyName()
   │    └─ Set companyRep = companyRep.getEmail()
   │
   ├─ e. Save to CSV:
   │    └─ fileBoundary.appendInternship(internship)
   │
   ├─ f. Add to lists:
   │    ├─ App.internshipList.add(internship)
   │    └─ companyRep.addInternship(internship)
   │
   └─ g. Display success message
        └─ InternshipBoundary.printCreateInternshipSummary()
```

---

### 6.4 Staff Approves Internship Flow

```
1. Staff selects "Review Internship Submissions"
   ↓
2. CareerCenterStaffController calls reviewInternshipAction.run(staff)
   ↓
3. ReviewInternshipSubmissionsAction.run():
   ├─ a. Filter internships:
   │    └─ Only show PENDING status
   │
   ├─ b. If no pending internships:
   │    └─ Display "No pending submissions", return
   │
   ├─ c. Display paginated list:
   │    └─ InternshipBoundary.printInternshipList()
   │
   ├─ d. Staff selects internship
   │
   ├─ e. Display full details:
   │    └─ InternshipBoundary.printInternshipDetails()
   │
   ├─ f. handleReview():
   │    ├─ Prompt: Approve or Reject?
   │    │
   │    ├─ If Approve:
   │    │  ├─ Set internship.internshipStatus = APPROVED
   │    │  └─ Display success message
   │    │
   │    └─ If Reject:
   │       ├─ Set internship.internshipStatus = REJECTED
   │       └─ Display rejection message
   │
   └─ g. Save changes:
        └─ writeInternshipsToCsv()
             └─ Overwrites entire CSV with updated data
```

---

### 6.5 Company Rep Reviews Applications Flow

```
1. CompanyRep selects "Review Applications"
   ↓
2. CompanyRepController calls reviewApplicationsAction.run(companyRep)
   ↓
3. ReviewApplicationsAction.run():
   ├─ a. Get rep's approved internships:
   │    └─ Filter createdInternships where status = APPROVED
   │
   ├─ b. Find applications for these internships:
   │    └─ Loop through internshipApplicationList
   │         └─ Match by internship ID
   │              └─ Filter: status = PENDING
   │
   ├─ c. Display internships with application counts:
   │    ├─ Internship 1: 5 pending applications
   │    ├─ Internship 2: 2 pending applications
   │    └─ Internship 3: 0 pending applications
   │
   ├─ d. Rep selects internship
   │
   ├─ e. Display list of pending applications:
   │    └─ Show: Student name, major, year, applied date
   │
   ├─ f. Rep selects application
   │
   ├─ g. Display student and internship details
   │
   └─ h. processApplication():
        ├─ Prompt: Approve or Reject?
        │
        ├─ If Approve:
        │  ├─ Check if student already employed
        │  │  └─ If yes: Show error, return
        │  │
        │  ├─ Confirm with rep
        │  │
        │  └─ If confirmed:
        │     ├─ Set application.status = SUCCESSFUL
        │     ├─ Update in App.internshipApplicationList
        │     ├─ Update in student.appliedInternships
        │     ├─ Save to CSV (writeInternshipApplications)
        │     └─ Display success message
        │
        └─ If Reject:
           ├─ Confirm with rep
           │
           └─ If confirmed:
              ├─ Set application.status = UNSUCCESSFUL
              ├─ Update in lists
              ├─ Save to CSV
              └─ Display rejection message
```

---

### 6.6 Student Accepts Offer Flow

```
1. Student selects "Check Application Status"
   ↓
2. StudentController calls checkApplicationStatusAction.run(student)
   ↓
3. CheckApplicationStatusAction.run():
   ├─ a. Get student's applications:
   │    └─ student.getInternshipApplications()
   │
   ├─ b. Display all applications with status:
   │    ├─ Application 1: PENDING
   │    ├─ Application 2: SUCCESSFUL ← Can accept
   │    └─ Application 3: UNSUCCESSFUL
   │
   ├─ c. Student selects application
   │
   ├─ d. Display application details:
   │    └─ displayApplicationDetails()
   │
   ├─ e. If status = SUCCESSFUL:
   │    └─ Show option to accept offer
   │
   └─ f. acceptOffer():
        ├─ Confirm with student
        │
        └─ If confirmed:
           ├─ Set student.employedStatus = true
           ├─ Clear student.appliedInternships (remove all)
           ├─ Get internship from application
           ├─ Decrease internship.numOfSlots by 1
           ├─ If slots = 0:
           │  └─ Set internship.internshipStatus = FILLED
           ├─ Update App.studentList
           ├─ Update App.internshipList
           ├─ Save students to CSV (App.WriteToCSV)
           ├─ Save internships to CSV
           └─ Display congratulations message
```

---

## 7. Key Business Logic

### 7.1 Student Eligibility Rules

**Can Apply for Internship If:**
```
✓ Internship status = APPROVED
✓ Student's major matches internship's preferredMajor
✓ If student year is 1 or 2 → Internship level must be BASIC
✓ Current date is between applicationOpenDate and applicationCloseDate
✓ Student has < 3 applications
✓ Student.employedStatus = false
✓ Student hasn't already applied to this internship
```

**Implementation Location:**
- [ViewInternshipsAction.java:getEligibleInternships()](InternshipSystem/src/team5/studentactions/ViewInternshipsAction.java#L45-L78)
- [ViewInternshipsAction.java:applyForInternship()](InternshipSystem/src/team5/studentactions/ViewInternshipsAction.java#L125-L180)

---

### 7.2 Company Rep Constraints

**Create Internship Constraints:**
```
✓ CompanyRep can create max 5 internships
✓ Each internship can have max 10 slots
✓ applicationCloseDate must be after applicationOpenDate
✓ New internships created with status = PENDING
```

**Update/Delete Constraints:**
```
✓ Can only update internships with status = PENDING
✓ Can only delete internships with status = PENDING
✓ Cannot update/delete APPROVED, REJECTED, or FILLED internships
✓ Deleting internship cascades to delete all its applications
```

**Implementation Location:**
- [CompanyRep.java:addInternship()](InternshipSystem/src/team5/CompanyRep.java#L35-L42)
- [ListOwnInternshipsAction.java:updateInternship()](InternshipSystem/src/team5/companyrepactions/ListOwnInternshipsAction.java#L150-L250)

---

### 7.3 Account Status Flow

**Company Rep Registration Flow:**
```
1. CompanyRep registers
   ├─ Email must be unique
   ├─ Default password = "password"
   └─ Initial status = PENDING
        ↓
2. Staff reviews registration
   ├─ Approve → status = APPROVED → Can login
   └─ Reject → status = REJECTED → Cannot login, can re-register
```

**Internship Approval Flow:**
```
1. CompanyRep creates internship
   └─ Initial status = PENDING
        ↓
2. Staff reviews internship
   ├─ Approve → status = APPROVED → Visible to students
   └─ Reject → status = REJECTED → Not visible to students
        ↓
3. Students apply (only to APPROVED)
        ↓
4. CompanyRep reviews applications
        ↓
5. Student accepts offer
   ├─ Internship.numOfSlots -= 1
   └─ If slots = 0 → status = FILLED → No more applications
```

---

### 7.4 Data Consistency Rules

**Global Lists in App:**
```
studentList              ←─────┐
staffList                      │
compRepList                    │ All synchronized
internshipList                 │ with CSV files
internshipApplicationList ←────┘
```

**Consistency Mechanisms:**
1. **Reload on Each Loop:** App reloads all data from CSV after logout
2. **Immediate Save:** Changes saved to CSV immediately after update
3. **Cascading Deletes:** Deleting internship deletes all its applications
4. **Reference Integrity:** InternshipApplication stores references to both Internship and Student

**Potential Issues:**
- No transaction support (CSV-based)
- Concurrent access not handled (single-user console app)
- No referential integrity enforcement (manual maintenance)

---

## 8. How to Read the Diagram

### 8.1 Follow the Lines

**Start from Top (Entity Layer):**
```
1. Look at User hierarchy
   ├─ See inheritance relationships (hollow triangles)
   └─ Understand what attributes each user type has

2. Look at Internship and InternshipApplication
   └─ See how they're composed together

3. Follow composition diamonds
   └─ Understand ownership relationships
```

**Middle (Controller & Action Layers):**
```
1. See how Controllers aggregate Actions
   └─ Hollow diamonds show this

2. Follow interface implementations
   └─ Dashed lines with hollow triangles

3. Understand Command pattern
   └─ Interface → Concrete implementations
```

**Bottom (Boundary & Support Layers):**
```
1. See Boundary inheritance
   └─ ConsoleBoundary → InternshipBoundary

2. See FileBoundary interface
   └─ Strategy pattern

3. Follow dependencies (dashed arrows)
   └─ Who uses whom
```

---

### 8.2 Understanding Arrows

**Arrow Direction = Direction of Knowledge:**

```
A ──→ B  means "A knows about B"
A ←── B  means "B knows about A"
```

**Examples:**
```
Student *── InternshipApplication
↑ Student contains applications (Student knows about Application)

InternshipApplication ──* Student
↑ Application references student (Application knows about Student)

ViewInternshipsAction ..> InternshipBoundary
↑ Action uses Boundary (Action knows about Boundary)
```

---

### 8.3 Reading Multiplicities

```
1      = exactly one
0..1   = zero or one
0..*   = zero or many
1..*   = one or many
0..3   = zero to three (specific max)
0..5   = zero to five (specific max)
```

**Examples:**
```
Student "1" *─── "0..3" InternshipApplication
↑ One student has zero to three applications

CompanyRep "1" *─── "0..5" Internship
↑ One company rep creates zero to five internships

InternshipApplication "many" ──* "1" Internship
↑ Many applications reference one internship
```

---

### 8.4 Color Coding (In PlantUML)

- **Yellow (Entity):** Core business objects
- **Blue (Controller):** Request handlers
- **Green (Boundary):** UI and I/O
- **Pink (Action):** Business logic
- **Gray (Enum):** Enumeration types

---

### 8.5 Reading Packages

**Packages group related classes:**

```
┌─────────────────────────────────┐
│       Entity Layer              │
│  User, Student, Internship, etc │
└─────────────────────────────────┘

┌─────────────────────────────────┐
│     Controller Layer            │
│  StudentController, etc         │
└─────────────────────────────────┘
```

**Benefits:**
- Logical grouping
- Easier to understand organization
- Shows architectural layers

---

## 9. Common Questions & Answers

### Q1: Why are there so many Action classes?

**Answer:** Each action represents a **single use case** (Single Responsibility Principle). This makes the code:
- Easier to test (test one use case at a time)
- Easier to maintain (changes to one feature don't affect others)
- Easier to understand (each class has one clear purpose)

---

### Q2: Why use interfaces for Actions?

**Answer:** **Polymorphism and extensibility**
- Controllers can hold any action through the interface
- New actions can be added without changing controller code
- Enables dependency injection for testing
- Follows Open/Closed Principle (open for extension, closed for modification)

---

### Q3: Why is App class all static?

**Answer:** **Simplicity trade-off**
- For a small console application, global state is acceptable
- Avoids complexity of dependency injection
- All components need access to same data anyway
- Easy to understand for beginners

**Trade-offs:**
- Hard to test (can't mock dependencies)
- Not scalable to larger applications
- Tight coupling to App class

---

### Q4: Why separate ConsoleBoundary and InternshipBoundary?

**Answer:** **Template Method pattern + specialization**
- ConsoleBoundary provides common methods (input, validation, formatting)
- InternshipBoundary adds internship-specific methods
- Reuses code while allowing specialization
- Other boundaries could be added (StudentBoundary, ApplicationBoundary)

---

### Q5: What's the difference between Composition and Aggregation?

**Answer:**

**Composition (filled diamond):**
- **Strong ownership:** Child cannot exist without parent
- Example: Student *── InternshipApplication
  - If student is deleted, applications should be deleted
  - Applications have no meaning without the student who applied

**Aggregation (hollow diamond):**
- **Weak ownership:** Child can exist independently
- Example: Controller o── Action
  - If controller is deleted, action can still exist
  - Action can be used by other controllers

---

### Q6: Why CSV instead of database?

**Answer:** **Project scope and requirements**
- Simpler for educational project
- No external dependencies
- Easy to inspect and debug (open CSV in Excel)
- Demonstrates file I/O concepts

**Future enhancement:** Replace CsvFileBoundary with DatabaseBoundary (that's why FileBoundary is an interface!)

---

### Q7: How do I add a new feature?

**Answer:** Follow this pattern:

1. **Identify user type:** Student, Staff, or CompanyRep?
2. **Create Action class:** Implement appropriate Action interface
3. **Add to Controller:** Add action to appropriate controller
4. **Update menu:** Add menu option in controller
5. **Implement logic:** Write business logic in action.run()
6. **Use boundaries:** Call ConsoleBoundary/InternshipBoundary for UI
7. **Update CSV:** If data changes, update CSV through FileBoundary

**Example:** Add "View Application History" for Students
```
1. Create ViewApplicationHistoryAction implements StudentAction
2. Add to StudentController:
   private StudentAction viewHistoryAction;
3. Update StudentController.showMenu():
   Add menu option "4. View Application History"
4. Implement ViewApplicationHistoryAction.run():
   - Get all applications (even UNSUCCESSFUL)
   - Display with timestamps
   - Show rejection reasons (if added to model)
```

---

## 10. Tips for Creating the Diagram

### Layout Suggestions:

1. **Horizontal Layers:**
```
┌────────────────────────────────────────────┐
│              Entity Layer                  │
└────────────────────────────────────────────┘
┌────────────────────────────────────────────┐
│           Controller Layer                 │
└────────────────────────────────────────────┘
┌────────────────────────────────────────────┐
│             Action Layer                   │
└────────────────────────────────────────────┘
┌────────────────────────────────────────────┐
│         Boundary & Filter Layers           │
└────────────────────────────────────────────┘
```

2. **Group by User Type:**
```
┌───────────────┬───────────────┬───────────────┐
│   Student     │     Staff     │  CompanyRep   │
│   Ecosystem   │   Ecosystem   │   Ecosystem   │
├───────────────┼───────────────┼───────────────┤
│ Controller    │ Controller    │ Controller    │
│ Actions       │ Actions       │ Actions       │
└───────────────┴───────────────┴───────────────┘
```

3. **Minimize Line Crossings:**
- Place related classes near each other
- Route lines around classes when possible
- Use orthogonal routing (right angles)

4. **Use Notes for Clarification:**
```
┌──────────────────┐
│   Student        │
│  - max 3 apps    │ ←─── Note: Business rule
└──────────────────┘
```

---

### Simplification Strategies:

**If diagram is too complex, create multiple diagrams:**

1. **Class Hierarchy Diagram**
   - Focus on inheritance
   - Show User hierarchy
   - Show Boundary hierarchy

2. **Component Interaction Diagram**
   - Focus on dependencies
   - Show how actions use boundaries
   - Show how controllers use actions

3. **Data Model Diagram**
   - Focus on entities and their relationships
   - Show composition relationships
   - Show enum associations

4. **Package Diagram**
   - High-level view
   - Show packages and their dependencies
   - Don't show individual classes

---

## 11. Summary

### Key Takeaways:

1. **Architecture:** Layered MVC with Action layer
2. **Design Patterns:** Command, Strategy, Template Method, MVC
3. **Relationships:**
   - Inheritance for User hierarchy
   - Composition for ownership (Student → Application)
   - Aggregation for usage (Controller → Action)
   - Dependency for temporary usage (Action → Boundary)
4. **Data Flow:** App → Controller → Action → Boundary → User
5. **Business Logic:** Encapsulated in Action classes
6. **Persistence:** Strategy pattern with CSV implementation

### Strengths:
✓ Clear separation of concerns
✓ Extensible (easy to add new actions)
✓ Uses multiple design patterns appropriately
✓ Organized into logical layers and packages

### Areas for Improvement:
- Global state in App (anti-pattern for larger apps)
- No transaction support (CSV limitations)
- Tight coupling to App class
- No proper logging or error handling layer

---

## 12. Additional Resources

### Files Created:
1. **class_diagram.puml** - The complete PlantUML diagram
2. **UML_Relationships_Reference.md** - Comprehensive list of all relationships
3. **UML_Diagram_Explanation.md** (this file) - Detailed explanation

### How to Use PlantUML:
1. Install PlantUML extension in VS Code
2. Open class_diagram.puml
3. Press Alt+D to preview
4. Export as PNG/SVG/PDF

### For Your Teammate:
- Give them **class_diagram.puml** to view the complete diagram
- Give them **UML_Relationships_Reference.md** as a checklist
- Give them **UML_Diagram_Explanation.md** for understanding

---

## END OF EXPLANATION DOCUMENT

Good luck with your UML diagram! If you have any questions, refer back to this document or examine the specific source files mentioned in the links.
