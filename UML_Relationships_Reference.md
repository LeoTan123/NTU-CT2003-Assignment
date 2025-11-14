# UML Class Diagram Relationships Reference
## NTU Internship Management System

This document lists all relationships between classes in the system to help with creating the complete UML diagram.

---

## 1. INHERITANCE RELATIONSHIPS (Generalization)
*Child classes inherit from parent classes*

| Parent Class | Child Class | Description |
|--------------|-------------|-------------|
| `User` (abstract) | `Student` | Student is a type of User |
| `User` (abstract) | `CareerCenterStaff` | Career Center Staff is a type of User |
| `User` (abstract) | `CompanyRep` | Company Representative is a type of User |
| `ConsoleBoundary` (abstract) | `InternshipBoundary` | InternshipBoundary extends ConsoleBoundary for specialized UI |

**UML Notation:** Solid line with hollow triangle pointing to parent
```
Child --|> Parent
```

---

## 2. INTERFACE IMPLEMENTATIONS (Realization)
*Classes that implement interfaces*

### 2.1 FileBoundary Interface
| Interface | Implementing Class | Description |
|-----------|-------------------|-------------|
| `FileBoundary` | `CsvFileBoundary` | Implements CSV file operations |

### 2.2 StudentAction Interface
| Interface | Implementing Class | Description |
|-----------|-------------------|-------------|
| `StudentAction` | `ViewInternshipsAction` | Implements view internships functionality |
| `StudentAction` | `CheckApplicationStatusAction` | Implements check application status functionality |

### 2.3 StaffAction Interface
| Interface | Implementing Class | Description |
|-----------|-------------------|-------------|
| `StaffAction` | `StaffViewInternshipsAction` | Implements staff view internships functionality |
| `StaffAction` | `ReviewInternshipSubmissionsAction` | Implements review internship submissions |
| `StaffAction` | `ReviewCompanyRegistrationsAction` | Implements review company registrations |

### 2.4 CompanyRepAction Interface
| Interface | Implementing Class | Description |
|-----------|-------------------|-------------|
| `CompanyRepAction` | `CreateInternshipAction` | Implements create internship functionality |
| `CompanyRepAction` | `ListOwnInternshipsAction` | Implements list own internships functionality |
| `CompanyRepAction` | `ReviewApplicationsAction` | Implements review applications functionality |

**UML Notation:** Dashed line with hollow triangle pointing to interface
```
Implementation ..|> Interface
```

---

## 3. COMPOSITION RELATIONSHIPS (Strong Ownership)
*"Has-a" relationship where the contained object cannot exist independently*

| Container Class | Contained Class | Multiplicity | Description |
|----------------|----------------|--------------|-------------|
| `Student` | `InternshipApplication` | 1 to 0..3 | Student contains/owns up to 3 applications |
| `CompanyRep` | `Internship` | 1 to 0..5 | CompanyRep owns up to 5 internships they created |
| `InternshipApplication` | `Internship` | 1 to 1 | Application references a specific internship |
| `InternshipApplication` | `Student` | 1 to 1 | Application is linked to a specific student |

**UML Notation:** Solid line with filled diamond on container side
```
Container *-- "multiplicity" Contained
```

**Key Characteristics:**
- If Student is deleted, their InternshipApplications are deleted
- If CompanyRep is deleted, their created Internships are deleted
- If Internship is deleted, its InternshipApplications should be deleted (cascading delete)

---

## 4. AGGREGATION RELATIONSHIPS (Weak Ownership)
*"Has-a" relationship where the contained object can exist independently*

### 4.1 Controller-Action Relationships

| Container Class | Contained Class | Multiplicity | Description |
|----------------|----------------|--------------|-------------|
| `StudentController` | `ViewInternshipsAction` | 1 to 1 | Controller has reference to action |
| `StudentController` | `CheckApplicationStatusAction` | 1 to 1 | Controller has reference to action |
| `CareerCenterStaffController` | `StaffViewInternshipsAction` | 1 to 1 | Controller has reference to action |
| `CareerCenterStaffController` | `ReviewInternshipSubmissionsAction` | 1 to 1 | Controller has reference to action |
| `CareerCenterStaffController` | `ReviewCompanyRegistrationsAction` | 1 to 1 | Controller has reference to action |
| `CompanyRepController` | `CreateInternshipAction` | 1 to 1 | Controller has reference to action |
| `CompanyRepController` | `ListOwnInternshipsAction` | 1 to 1 | Controller has reference to action |
| `CompanyRepController` | `ReviewApplicationsAction` | 1 to 1 | Controller has reference to action |
| `CompanyRepController` | `FileBoundary` | 1 to 1 | Controller has reference to file boundary |

**UML Notation:** Solid line with hollow diamond on container side
```
Container o-- "multiplicity" Contained
```

**Key Characteristics:**
- Actions can exist independently of controllers
- Controllers aggregate actions for execution

---

## 5. DEPENDENCY RELATIONSHIPS (Usage)
*"Uses" relationship where one class depends on another*

### 5.1 App Dependencies

| Dependent Class | Dependency | Usage Type | Description |
|----------------|-----------|------------|-------------|
| `App` | `StudentController` | Creates | Creates controller instances |
| `App` | `CareerCenterStaffController` | Creates | Creates controller instances |
| `App` | `CompanyRepController` | Creates | Creates controller instances |
| `App` | `CompanyRepRegistrationHandler` | Creates | Creates registration handler |
| `App` | `Student` | Manages | Manages list of students |
| `App` | `CareerCenterStaff` | Manages | Manages list of staff |
| `App` | `CompanyRep` | Manages | Manages list of company reps |
| `App` | `Internship` | Manages | Manages list of internships |
| `App` | `InternshipApplication` | Manages | Manages list of applications |

### 5.2 Student Action Dependencies

| Dependent Class | Dependency | Description |
|----------------|-----------|-------------|
| `ViewInternshipsAction` | `InternshipBoundary` | Uses for UI display |
| `ViewInternshipsAction` | `ConsoleBoundary` | Uses for general console I/O |
| `ViewInternshipsAction` | `InternshipFilter` | Uses for filtering internships |
| `ViewInternshipsAction` | `InternshipFilterPrompt` | Uses for filter input |
| `CheckApplicationStatusAction` | `InternshipBoundary` | Uses for UI display |
| `CheckApplicationStatusAction` | `ConsoleBoundary` | Uses for general console I/O |

### 5.3 Company Rep Action Dependencies

| Dependent Class | Dependency | Description |
|----------------|-----------|-------------|
| `CreateInternshipAction` | `InternshipBoundary` | Uses for UI display |
| `CreateInternshipAction` | `ConsoleBoundary` | Uses for general console I/O |
| `ListOwnInternshipsAction` | `InternshipBoundary` | Uses for UI display |
| `ListOwnInternshipsAction` | `ConsoleBoundary` | Uses for general console I/O |
| `ListOwnInternshipsAction` | `InternshipFilterPrompt` | Uses for filter input |
| `ReviewApplicationsAction` | `InternshipBoundary` | Uses for UI display |
| `ReviewApplicationsAction` | `ConsoleBoundary` | Uses for general console I/O |

### 5.4 Staff Action Dependencies

| Dependent Class | Dependency | Description |
|----------------|-----------|-------------|
| `StaffViewInternshipsAction` | `InternshipBoundary` | Uses for UI display |
| `StaffViewInternshipsAction` | `ConsoleBoundary` | Uses for general console I/O |
| `StaffViewInternshipsAction` | `InternshipFilter` | Uses for filtering internships |
| `StaffViewInternshipsAction` | `InternshipFilterPrompt` | Uses for filter input |
| `ReviewInternshipSubmissionsAction` | `InternshipBoundary` | Uses for UI display |
| `ReviewInternshipSubmissionsAction` | `ConsoleBoundary` | Uses for general console I/O |
| `ReviewCompanyRegistrationsAction` | `ConsoleBoundary` | Uses for general console I/O |

### 5.5 Filter Layer Dependencies

| Dependent Class | Dependency | Description |
|----------------|-----------|-------------|
| `InternshipFilter` | `InternshipFilterCriteria` | Uses criteria for filtering |
| `InternshipFilterPrompt` | `InternshipFilterCriteria` | Creates filter criteria based on user input |

### 5.6 Registration Handler Dependencies

| Dependent Class | Dependency | Description |
|----------------|-----------|-------------|
| `CompanyRepRegistrationHandler` | `CompanyRep` | Creates new company representative instances |
| `CompanyRepRegistrationHandler` | `ConsoleBoundary` | Uses for console I/O |

**UML Notation:** Dashed line with open arrow pointing to dependency
```
Dependent ..> Dependency
```

---

## 6. ASSOCIATION RELATIONSHIPS (Enum Usage)
*Classes that use enum types*

| Class | Enum Type | Attribute/Usage | Description |
|-------|-----------|----------------|-------------|
| `User` | `UserType` | userType: UserType | User has a user type |
| `CompanyRep` | `UserAccountStatus` | accountStatus: UserAccountStatus | Company rep has account status |
| `CompanyRepRegistration` | `UserAccountStatus` | status: UserAccountStatus | Registration has status |
| `Student` | `StudentMajor` | major: StudentMajor | Student has a major |
| `Internship` | `InternshipLevel` | internshipLevel: InternshipLevel | Internship has a level |
| `Internship` | `StudentMajor` | preferredMajor: StudentMajor | Internship has preferred major |
| `Internship` | `InternshipStatus` | internshipStatus: InternshipStatus | Internship has status |
| `InternshipApplication` | `InternshipApplicationStatus` | status: InternshipApplicationStatus | Application has status |
| `App` | `CSVType` | Parameter in methods | Used for CSV operations |
| `InternshipFilterCriteria` | `InternshipFilterOption` | Filter criteria | Used for filtering options |
| `InternshipFilterCriteria` | `StudentMajor` | preferredMajor | Filter by major |
| `InternshipFilterCriteria` | `InternshipLevel` | internshipLevel | Filter by level |
| `InternshipFilterCriteria` | `InternshipStatus` | internshipStatus | Filter by status |

**UML Notation:** Solid line with open arrow pointing to enum
```
Class --> Enum
```

---

## 7. KEY RELATIONSHIPS SUMMARY BY LAYER

### Entity Layer Relationships
- **User hierarchy:** User ← Student, CareerCenterStaff, CompanyRep (Inheritance)
- **Student ↔ InternshipApplication:** Composition (1 to 0..3)
- **CompanyRep ↔ Internship:** Composition (1 to 0..5)
- **InternshipApplication → Internship:** Composition (many to 1)
- **InternshipApplication → Student:** Composition (many to 1)

### Controller Layer Relationships
- **All Controllers ↔ Actions:** Aggregation (1 to 1)
- **CompanyRepController ↔ FileBoundary:** Aggregation (1 to 1)

### Boundary Layer Relationships
- **ConsoleBoundary ← InternshipBoundary:** Inheritance
- **FileBoundary ← CsvFileBoundary:** Interface Implementation

### Action Layer Relationships
- **All Actions → Boundaries:** Dependency (uses)
- **View/List Actions → Filter classes:** Dependency (uses)
- **Action Interfaces ← Concrete Actions:** Interface Implementation

### Application Layer Relationships
- **App → All Controllers:** Dependency (creates)
- **App → All Entity Classes:** Dependency (manages)
- **App → CompanyRepRegistrationHandler:** Dependency (creates)

---

## 8. DESIGN PATTERNS AND THEIR RELATIONSHIPS

### 8.1 MVC Pattern
- **Model (Entity):** User, Student, CareerCenterStaff, CompanyRep, Internship, InternshipApplication
- **View (Boundary):** ConsoleBoundary, InternshipBoundary
- **Controller:** StudentController, CareerCenterStaffController, CompanyRepController

**Relationships:**
- Controller → Model (manages)
- Controller → View (uses)
- View → Model (displays)

### 8.2 Command Pattern
- **Command Interface:** StudentAction, StaffAction, CompanyRepAction
- **Concrete Commands:** All action implementation classes
- **Invoker:** Controllers
- **Receiver:** Entity objects (Student, Staff, CompanyRep)

**Relationships:**
- Controller aggregates Command
- Command operates on Receiver
- Concrete Commands implement Command Interface

### 8.3 Strategy Pattern
- **Strategy Interface:** FileBoundary
- **Concrete Strategy:** CsvFileBoundary
- **Context:** CreateInternshipAction, ListOwnInternshipsAction, CompanyRepController

**Relationships:**
- Context depends on Strategy Interface
- Concrete Strategy implements Strategy Interface

### 8.4 Singleton Pattern (App Global State)
- **Singleton:** App class with static members
- **Clients:** All Controllers, Actions, Handlers

**Relationships:**
- All components depend on App for global state access

---

## 9. MULTIPLICITY REFERENCE

| Notation | Meaning |
|----------|---------|
| 1 | Exactly one |
| 0..1 | Zero or one |
| 0..* | Zero or many |
| 1..* | One or many |
| 0..3 | Zero to three (Student applications) |
| 0..5 | Zero to five (CompanyRep internships) |
| 0..10 | Zero to ten (Internship slots) |

---

## 10. RELATIONSHIP PRIORITY FOR DIAGRAM

When creating the UML diagram, prioritize showing relationships in this order:

1. **Inheritance** (most important for understanding class hierarchy)
2. **Interface Implementation** (shows polymorphism)
3. **Composition** (shows strong ownership)
4. **Aggregation** (shows controller-action relationships)
5. **Key Dependencies** (App → Controllers, Actions → Boundaries)
6. **Enum Associations** (can be shown separately if diagram is too crowded)

---

## 11. NOTES FOR DIAGRAM CREATION

### Layout Suggestions:
1. **Top Layer:** Entity classes (User hierarchy, Internship, InternshipApplication)
2. **Middle Layer:** Controllers and Actions (horizontally arranged by user type)
3. **Bottom Layer:** Boundaries and Utilities (Filter classes, Registration handler)
4. **Side Panel:** Enums (can be grouped together)
5. **Center:** App class (as it connects to everything)

### Simplification Options:
- If diagram is too complex, consider creating separate diagrams:
  - **Class Hierarchy Diagram:** Focus on inheritance and entity relationships
  - **Component Diagram:** Focus on controllers, actions, and boundaries
  - **Interaction Diagram:** Show dependencies and usage patterns

### Color Coding (as in PlantUML):
- **Yellow:** Entity classes
- **Blue:** Controllers
- **Green:** Boundaries
- **Pink:** Actions
- **Gray:** Enums
- **White:** Utilities and Filters

---

## 12. QUICK REFERENCE: RELATIONSHIPS BY CLASS

### User
- **Is parent of:** Student, CareerCenterStaff, CompanyRep (Inheritance)
- **Uses:** UserType enum (Association)

### Student
- **Extends:** User (Inheritance)
- **Contains:** InternshipApplication (0..3) (Composition)
- **Uses:** StudentMajor enum (Association)

### CareerCenterStaff
- **Extends:** User (Inheritance)

### CompanyRep
- **Extends:** User (Inheritance)
- **Creates:** Internship (0..5) (Composition)
- **Uses:** UserAccountStatus enum (Association)

### Internship
- **Contained by:** CompanyRep (Composition)
- **Referenced by:** InternshipApplication (Composition)
- **Uses:** InternshipLevel, StudentMajor, InternshipStatus enums (Association)

### InternshipApplication
- **Contained by:** Student (Composition)
- **References:** Internship (Composition)
- **References:** Student (Composition)
- **Uses:** InternshipApplicationStatus enum (Association)

### StudentController
- **Aggregates:** ViewInternshipsAction, CheckApplicationStatusAction

### CareerCenterStaffController
- **Aggregates:** StaffViewInternshipsAction, ReviewInternshipSubmissionsAction, ReviewCompanyRegistrationsAction

### CompanyRepController
- **Aggregates:** CreateInternshipAction, ListOwnInternshipsAction, ReviewApplicationsAction, FileBoundary

### All Action Classes
- **Implement:** Respective Action interface (StudentAction/StaffAction/CompanyRepAction)
- **Depend on:** ConsoleBoundary, InternshipBoundary

### App
- **Creates:** All Controllers, CompanyRepRegistrationHandler (Dependency)
- **Manages:** All entity lists (Dependency)

---

## END OF REFERENCE DOCUMENT
