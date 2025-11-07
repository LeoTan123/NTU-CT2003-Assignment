# Internship System - Testing Document
**Course:** CT2003
**Team:** Team 5
**Date:** November 2025
**Version:** 1.0

---

## Table of Contents
1. [Executive Summary](#executive-summary)
2. [Test Plan Overview](#test-plan-overview)
3. [Test Strategy](#test-strategy)
4. [Test Environment](#test-environment)
5. [Student Module Test Cases](#student-module-test-cases)
6. [Career Center Staff Module Test Cases](#career-center-staff-module-test-cases)
7. [Company Representative Module Test Cases](#company-representative-module-test-cases)
8. [Integration Test Cases](#integration-test-cases)
9. [Validation and Business Rules Test Cases](#validation-and-business-rules-test-cases)
10. [Test Execution Summary](#test-execution-summary)
11. [Defect Tracking](#defect-tracking)
12. [Conclusion](#conclusion)

---

## Executive Summary

This document outlines the comprehensive testing strategy and test cases for the Internship Management System developed for CT2003. The system supports three user types: Students, Career Center Staff, and Company Representatives, with features spanning from user authentication to internship application management.

**Testing Objectives:**
- Verify all functional requirements are met
- Ensure data integrity across CSV file operations
- Validate business rules and constraints
- Confirm proper user access control
- Test system reliability and error handling

**Scope:**
- Functional testing of all user modules
- Integration testing of workflows
- Validation testing of business rules
- Negative testing for error handling

---

## Test Plan Overview

### 1.1 Purpose
To ensure the Internship Management System functions correctly, meets all requirements, and provides a reliable user experience across all user types.

### 1.2 Scope
**In Scope:**
- Student features (view internships, apply, check status, update password)
- Career Center Staff features (review registrations, approve internships, view listings)
- Company Representative features (create/update internships, registration)
- Authentication and authorization
- Data persistence (CSV file operations)
- Business rule validation
- Workflow integration

**Out of Scope:**
- Performance testing
- Load testing
- Security penetration testing
- Cross-platform compatibility testing

### 1.3 Test Deliverables
- Test case documentation (this document)
- Test execution results
- Defect reports
- Test summary report

### 1.4 Test Schedule
- Test Planning: Week 1
- Test Case Development: Week 2
- Test Execution: Week 3-4
- Defect Resolution: Week 4-5
- Final Testing & Sign-off: Week 5

---

## Test Strategy

### 2.1 Testing Levels
1. **Unit Testing** - Individual methods and functions
2. **Integration Testing** - Module interactions and workflows
3. **System Testing** - End-to-end scenarios
4. **Acceptance Testing** - User requirement validation

### 2.2 Testing Types
- **Functional Testing** - Verify features work as specified
- **Negative Testing** - Test invalid inputs and error conditions
- **Boundary Testing** - Test limits and edge cases
- **Regression Testing** - Ensure fixes don't break existing functionality

### 2.3 Test Data
- Pre-populated CSV files with sample data
- Edge case data (empty fields, special characters, boundary values)
- Valid and invalid user credentials

### 2.4 Entry and Exit Criteria
**Entry Criteria:**
- All features implemented
- Code compilation successful
- Test environment setup complete

**Exit Criteria:**
- All test cases executed
- Critical and high-priority defects resolved
- 90% test case pass rate achieved
- Acceptance criteria met

---

## Test Environment

### 3.1 Hardware
- Standard development machines (Windows/Linux/Mac)
- Minimum 4GB RAM
- Sufficient disk space for CSV files

### 3.2 Software
- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment
- Text editor for CSV file verification
- Terminal/Command Prompt

### 3.3 Test Data Files
- students.csv
- staff.csv
- companyreps.csv
- internships.csv
- application.properties

---

## Student Module Test Cases

### TC-S001: Student Login - Valid Credentials
**Priority:** High
**Preconditions:** Student account exists in students.csv
**Test Steps:**
1. Launch application
2. Select option "1: Student"
3. Enter valid Student ID
4. Enter correct password
5. Observe login result

**Expected Result:**
- Login successful message displayed
- Student name shown in welcome message
- Student menu displayed

**Test Data:**
- UserID: [Existing Student ID from CSV]
- Password: password

---

### TC-S002: Student Login - Invalid Password
**Priority:** High
**Preconditions:** Student account exists in students.csv
**Test Steps:**
1. Launch application
2. Select option "1: Student"
3. Enter valid Student ID
4. Enter incorrect password (3 times)
5. Observe login result

**Expected Result:**
- First attempt: "Password wrong, please enter again" prompt
- Second attempt: "Password wrong, please enter again" prompt
- Third attempt: "Wrong password 3 times. Login Failed."
- Return to main menu

**Test Data:**
- UserID: [Existing Student ID]
- Password: wrongpassword

---

### TC-S003: View Internship Opportunities - No Filters
**Priority:** High
**Preconditions:** Student logged in, approved internships exist
**Test Steps:**
1. From student menu, select "View Internship Opportunities"
2. Skip all filter options (press Enter for each)
3. Observe internship listing

**Expected Result:**
- All APPROVED internships displayed
- Shows: ID, Title, Description, Level, Major, Dates, Slots
- Option to apply for each internship shown
- PENDING/REJECTED internships NOT shown

---

### TC-S004: View Internship Opportunities - With Major Filter
**Priority:** Medium
**Preconditions:** Student logged in, internships with different majors exist
**Test Steps:**
1. From student menu, select "View Internship Opportunities"
2. Select preferred major (e.g., "CS")
3. Skip other filters
4. Observe filtered results

**Expected Result:**
- Only internships matching selected major displayed
- Internships with other majors excluded
- "No preferred major" internships may also appear (if implemented)

**Test Data:**
- Filter: Major = "Computer Science (CS)"

---

### TC-S005: Apply for Internship - Valid Application
**Priority:** High
**Preconditions:**
- Student logged in
- Student has fewer than 3 active applications
- Student not employed
- Internship is APPROVED and has available slots
- Application period is open

**Test Steps:**
1. View internship opportunities
2. Select an eligible internship
3. Choose option to apply
4. Confirm application

**Expected Result:**
- Application submitted successfully
- Application ID generated and displayed
- Application status set to PENDING
- Internship slot count decremented by 1
- Application saved to student's application list

---

### TC-S006: Apply for Internship - Maximum Applications Reached
**Priority:** High
**Preconditions:**
- Student logged in
- Student already has 3 active applications

**Test Steps:**
1. View internship opportunities
2. Attempt to apply for another internship

**Expected Result:**
- Error message: "You cannot apply for more than 3 internships at a time"
- Application NOT submitted
- Slot count unchanged

---

### TC-S007: Apply for Internship - Already Employed
**Priority:** High
**Preconditions:**
- Student logged in
- Student has already accepted an internship (employed = true)

**Test Steps:**
1. View internship opportunities
2. Attempt to apply for an internship

**Expected Result:**
- Error message: "You have already accepted an internship offer"
- Application NOT submitted

---

### TC-S008: Apply for Internship - Year Eligibility (Year 1-2, BASIC only)
**Priority:** High
**Preconditions:**
- Student logged in (Year 1 or 2)
- Attempting to apply for INTERMEDIATE or ADVANCED internship

**Test Steps:**
1. View all internship opportunities
2. Attempt to apply for INTERMEDIATE/ADVANCED level internship

**Expected Result:**
- Error message: "Year 1-2 students can only apply for BASIC level internships"
- Application NOT submitted

**Test Data:**
- Student Year: 1 or 2
- Internship Level: INTERMEDIATE or ADVANCED

---

### TC-S009: Apply for Internship - Duplicate Application
**Priority:** Medium
**Preconditions:**
- Student logged in
- Student has already applied for a specific internship

**Test Steps:**
1. View internship opportunities
2. Attempt to apply for the same internship again

**Expected Result:**
- Error message: "You have already applied for this internship"
- Application NOT submitted

---

### TC-S010: Apply for Internship - Application Period Closed
**Priority:** Medium
**Preconditions:**
- Student logged in
- Current date is before open date OR after close date

**Test Steps:**
1. View internship opportunities
2. Attempt to apply for internship with closed application period

**Expected Result:**
- Error message: "Application period for this internship is closed"
- Application NOT submitted

---

### TC-S011: Check Application Status - View All Applications
**Priority:** High
**Preconditions:**
- Student logged in
- Student has submitted applications

**Test Steps:**
1. From student menu, select "Check Internship Application Status"
2. View application list

**Expected Result:**
- All submitted applications displayed
- Shows: Application ID, Internship Title, Application Date, Status
- Status shown as PENDING, SUCCESSFUL, or UNSUCCESSFUL
- Option to view details for each application

---

### TC-S012: Check Application Status - No Applications
**Priority:** Low
**Preconditions:**
- Student logged in
- Student has not submitted any applications

**Test Steps:**
1. From student menu, select "Check Internship Application Status"

**Expected Result:**
- Message: "You have not applied for any internships yet"
- Return to student menu

---

### TC-S013: Accept Internship Offer - Valid Acceptance
**Priority:** High
**Preconditions:**
- Student logged in
- Student has application with SUCCESSFUL status
- Internship has available slots
- Student is not already employed

**Test Steps:**
1. Check application status
2. Select application with SUCCESSFUL status
3. Choose option to accept offer
4. Confirm acceptance

**Expected Result:**
- Success message displayed
- Student's employed status set to true
- Student's application list cleared
- Student cannot apply for more internships
- Internship slot decremented (if applicable)

---

### TC-S014: Accept Internship Offer - Already Employed
**Priority:** Medium
**Preconditions:**
- Student logged in
- Student already accepted another internship
- Student has another application with SUCCESSFUL status

**Test Steps:**
1. Check application status
2. Attempt to accept another successful application

**Expected Result:**
- Error message: "You have already accepted an internship offer"
- Acceptance NOT processed

---

### TC-S015: Accept Internship Offer - Internship Fully Booked
**Priority:** Medium
**Preconditions:**
- Student logged in
- Application status is SUCCESSFUL
- Internship slots are all taken (slots = 0)

**Test Steps:**
1. Check application status
2. Attempt to accept offer for fully booked internship

**Expected Result:**
- Error message: "This internship is fully booked"
- Acceptance NOT processed

---

### TC-S016: Update Password - Valid Password Change
**Priority:** Medium
**Preconditions:** Student logged in
**Test Steps:**
1. From student menu, select "Update Password"
2. Enter current password correctly
3. Enter new password
4. Confirm new password (match)
5. Observe result

**Expected Result:**
- Password updated successfully message
- New password saved to CSV file
- Student automatically logged out
- Can login with new password

**Test Data:**
- Old Password: password
- New Password: newpassword123
- Confirm Password: newpassword123

---

### TC-S017: Update Password - Wrong Current Password
**Priority:** Medium
**Preconditions:** Student logged in
**Test Steps:**
1. From student menu, select "Update Password"
2. Enter incorrect current password
3. Observe result

**Expected Result:**
- Error message: "Current password is incorrect"
- Password NOT updated
- Remain in student menu

---

### TC-S018: Update Password - New Password Mismatch
**Priority:** Low
**Preconditions:** Student logged in
**Test Steps:**
1. From student menu, select "Update Password"
2. Enter current password correctly
3. Enter new password
4. Enter different password for confirmation
5. Observe result

**Expected Result:**
- Error message: "New passwords do not match"
- Password NOT updated
- Remain in update password screen or menu

---

### TC-S019: Update Password - Same as Old Password
**Priority:** Low
**Preconditions:** Student logged in
**Test Steps:**
1. From student menu, select "Update Password"
2. Enter current password correctly
3. Enter same password as new password
4. Observe result

**Expected Result:**
- Error message: "New password cannot be the same as old password"
- Password NOT updated

---

### TC-S020: Student Logout
**Priority:** Medium
**Preconditions:** Student logged in
**Test Steps:**
1. From student menu, select "Logout"
2. Observe result

**Expected Result:**
- Logged out successfully
- Return to main menu (login screen)
- Current user cleared

---

## Career Center Staff Module Test Cases

### TC-CS001: Staff Login - Valid Credentials
**Priority:** High
**Preconditions:** Staff account exists in staff.csv
**Test Steps:**
1. Launch application
2. Select option "2: Career Center Staff"
3. Enter valid Staff ID
4. Enter correct password
5. Observe login result

**Expected Result:**
- Login successful message displayed
- Staff name shown in welcome message
- Staff menu displayed

**Test Data:**
- UserID: [Existing Staff ID from CSV]
- Password: password

---

### TC-CS002: Review Company Registrations - Approve Registration
**Priority:** High
**Preconditions:**
- Staff logged in
- Company rep registrations with PENDING status exist

**Test Steps:**
1. From staff menu, select "Review Company Representative Registrations"
2. View pending registrations (paginated, 5 per page)
3. Select a registration to review
4. Choose option to approve
5. Confirm approval

**Expected Result:**
- Registration status changed to APPROVED
- Updated record saved to companyreps.csv
- Company rep can now login and create internships
- Success message displayed
- Pagination updated to show remaining pending registrations

---

### TC-CS003: Review Company Registrations - Reject Registration
**Priority:** High
**Preconditions:**
- Staff logged in
- Company rep registrations with PENDING status exist

**Test Steps:**
1. From staff menu, select "Review Company Representative Registrations"
2. View pending registrations
3. Select a registration to review
4. Choose option to reject
5. Confirm rejection

**Expected Result:**
- Registration status changed to REJECTED
- Updated record saved to companyreps.csv
- Company rep cannot login
- Company rep can re-submit registration
- Success message displayed

---

### TC-CS004: Review Company Registrations - No Pending Registrations
**Priority:** Low
**Preconditions:**
- Staff logged in
- No company rep registrations with PENDING status

**Test Steps:**
1. From staff menu, select "Review Company Representative Registrations"

**Expected Result:**
- Message: "No pending company representative registrations"
- Return to staff menu

---

### TC-CS005: Review Company Registrations - Pagination Navigation
**Priority:** Medium
**Preconditions:**
- Staff logged in
- More than 5 pending registrations exist

**Test Steps:**
1. From staff menu, select "Review Company Representative Registrations"
2. View first page (5 registrations)
3. Navigate to next page (enter 'n')
4. Navigate to previous page (enter 'p')
5. Observe pagination behavior

**Expected Result:**
- First page shows registrations 1-5
- Next page shows registrations 6-10
- Previous page returns to registrations 1-5
- Proper page boundaries enforced
- Page number indicators shown

---

### TC-CS006: Review Internship Submissions - Approve Internship
**Priority:** High
**Preconditions:**
- Staff logged in
- Internship submissions with PENDING status exist

**Test Steps:**
1. From staff menu, select "Review Internship Submissions"
2. View pending internships (paginated, 5 per page)
3. Select an internship to review
4. Choose option to approve
5. Confirm approval

**Expected Result:**
- Internship status changed to APPROVED
- Updated record saved to internships.csv
- Internship becomes visible to students
- Success message displayed
- Pagination updated to show remaining pending internships

---

### TC-CS007: Review Internship Submissions - Reject Internship
**Priority:** High
**Preconditions:**
- Staff logged in
- Internship submissions with PENDING status exist

**Test Steps:**
1. From staff menu, select "Review Internship Submissions"
2. View pending internships
3. Select an internship to review
4. Choose option to reject
5. Confirm rejection

**Expected Result:**
- Internship status changed to REJECTED
- Updated record saved to internships.csv
- Internship NOT visible to students
- Company rep can see rejection in their listings
- Success message displayed

---

### TC-CS008: Review Internship Submissions - No Pending Submissions
**Priority:** Low
**Preconditions:**
- Staff logged in
- No internship submissions with PENDING status

**Test Steps:**
1. From staff menu, select "Review Internship Submissions"

**Expected Result:**
- Message: "No pending internship submissions"
- Return to staff menu

---

### TC-CS009: View Internship Opportunities - Staff View
**Priority:** Medium
**Preconditions:**
- Staff logged in
- Internships exist in system (any status)

**Test Steps:**
1. From staff menu, select "View Internship Opportunities"
2. View internship listings

**Expected Result:**
- All internships displayed (PENDING, APPROVED, REJECTED, FILLED)
- Shows: ID, Title, Description, Level, Major, Dates, Status, Slots
- Staff can see internships in all statuses (unlike students)
- Comprehensive view for monitoring purposes

---

### TC-CS010: View Internship Opportunities - No Internships
**Priority:** Low
**Preconditions:**
- Staff logged in
- No internships in system

**Test Steps:**
1. From staff menu, select "View Internship Opportunities"

**Expected Result:**
- Message: "No internship opportunities available"
- Return to staff menu

---

### TC-CS011: Staff Update Password
**Priority:** Medium
**Preconditions:** Staff logged in
**Test Steps:**
1. From staff menu, select "Update Password"
2. Enter current password correctly
3. Enter new password
4. Confirm new password (match)
5. Observe result

**Expected Result:**
- Password updated successfully message
- New password saved to CSV file
- Staff automatically logged out
- Can login with new password

---

### TC-CS012: Staff Logout
**Priority:** Medium
**Preconditions:** Staff logged in
**Test Steps:**
1. From staff menu, select "Logout"
2. Observe result

**Expected Result:**
- Logged out successfully
- Return to main menu (login screen)
- Current user cleared

---

## Company Representative Module Test Cases

### TC-CR001: Company Rep Registration - New Registration
**Priority:** High
**Preconditions:**
- Application running at main menu
- Email address not in system

**Test Steps:**
1. From main menu, select "4: Register Company Representative"
2. Enter full name
3. Enter company name
4. Enter department
5. Enter position
6. Enter email address (unique)
7. Review summary
8. Confirm registration

**Expected Result:**
- Registration successful message
- Account created with PENDING status
- Record saved to companyreps.csv
- Unique ID generated (email-based)
- Default password set to "password"
- Cannot login until approved by staff

**Test Data:**
- Name: John Doe
- Company: Tech Corp
- Department: Engineering
- Position: HR Manager
- Email: john.doe@techcorp.com

---

### TC-CR002: Company Rep Registration - Duplicate Email (Approved/Pending)
**Priority:** High
**Preconditions:**
- Application running at main menu
- Email already exists with APPROVED or PENDING status

**Test Steps:**
1. From main menu, select "4: Register Company Representative"
2. Enter registration details
3. Enter email that already exists (APPROVED/PENDING)
4. Attempt to complete registration

**Expected Result:**
- Error message: "Email already registered in system"
- Registration NOT created
- Return to registration menu or main menu

---

### TC-CR003: Company Rep Registration - Re-submit After Rejection
**Priority:** Medium
**Preconditions:**
- Application running at main menu
- Email exists with REJECTED status

**Test Steps:**
1. From main menu, select "4: Register Company Representative"
2. Enter email that was previously rejected
3. System detects rejected account
4. Choose option to re-submit for approval
5. Confirm re-submission

**Expected Result:**
- System recognizes previously rejected email
- Option presented to re-submit
- Account status updated from REJECTED to PENDING
- Record updated in companyreps.csv
- Success message displayed

---

### TC-CR004: Company Rep Registration - Cancel Registration
**Priority:** Low
**Preconditions:** Application running at main menu
**Test Steps:**
1. From main menu, select "4: Register Company Representative"
2. Enter some registration details
3. At summary screen, choose cancel option
4. Observe result

**Expected Result:**
- Registration cancelled
- No record created
- Return to main menu

---

### TC-CR005: Company Rep Registration - Empty Required Fields
**Priority:** Medium
**Preconditions:** Application running at main menu
**Test Steps:**
1. From main menu, select "4: Register Company Representative"
2. Leave required fields empty (name, company, department, position, email)
3. Attempt to proceed

**Expected Result:**
- Error message for each empty field
- Prompt to re-enter required information
- Cannot proceed until all fields filled

---

### TC-CR006: Company Rep Login - Pending Account
**Priority:** High
**Preconditions:**
- Company rep account exists with PENDING status

**Test Steps:**
1. From main menu, select "3: Company Representatives"
2. Enter company rep user ID
3. Enter password

**Expected Result:**
- Error message: "Your account is pending approval. Please wait for the career center staff to review it."
- Login denied
- Return to main menu

---

### TC-CR007: Company Rep Login - Rejected Account
**Priority:** High
**Preconditions:**
- Company rep account exists with REJECTED status

**Test Steps:**
1. From main menu, select "3: Company Representatives"
2. Enter company rep user ID
3. Enter password

**Expected Result:**
- Error message: "Your registration was rejected. Please submit a new registration request for review."
- Login denied
- Return to main menu

---

### TC-CR008: Company Rep Login - Approved Account
**Priority:** High
**Preconditions:**
- Company rep account exists with APPROVED status

**Test Steps:**
1. From main menu, select "3: Company Representatives"
2. Enter company rep user ID
3. Enter correct password

**Expected Result:**
- Login successful message displayed
- Company rep name shown in welcome message
- Company rep menu displayed

---

### TC-CR009: Create Internship - Valid Creation
**Priority:** High
**Preconditions:**
- Company rep logged in (APPROVED status)
- Company rep has fewer than 5 existing internships

**Test Steps:**
1. From company rep menu, select "Create Internship Opportunity"
2. Enter title
3. Enter description
4. Select internship level
5. Select preferred major
6. Enter application open date (yyyy-MM-dd)
7. Enter application close date (yyyy-MM-dd)
8. Enter number of slots (1-10)
9. Review summary
10. Confirm creation

**Expected Result:**
- Internship created successfully
- Unique internship ID generated (format: "I" + number)
- Status set to PENDING
- Record saved to internships.csv
- Success message displayed
- Internship appears in company rep's listing
- Awaits staff approval before visible to students

**Test Data:**
- Title: Software Engineering Intern
- Description: Develop web applications
- Level: INTERMEDIATE
- Major: Computer Science
- Open Date: 2025-01-15
- Close Date: 2025-02-28
- Slots: 5

---

### TC-CR010: Create Internship - Maximum Limit Reached
**Priority:** High
**Preconditions:**
- Company rep logged in
- Company rep already has 5 internships

**Test Steps:**
1. From company rep menu, select "Create Internship Opportunity"
2. Attempt to create new internship

**Expected Result:**
- Error message: "You have reached the maximum limit of 5 internship postings"
- Internship NOT created
- Return to company rep menu

---

### TC-CR011: Create Internship - Invalid Date Range
**Priority:** Medium
**Preconditions:**
- Company rep logged in
- Company rep has fewer than 5 internships

**Test Steps:**
1. From company rep menu, select "Create Internship Opportunity"
2. Enter all required fields
3. Enter open date: 2025-02-28
4. Enter close date: 2025-01-15 (before open date)
5. Attempt to create

**Expected Result:**
- Error message: "Application close date cannot be before open date"
- Internship NOT created
- Prompt to re-enter dates

---

### TC-CR012: Create Internship - Invalid Slot Count
**Priority:** Low
**Preconditions:**
- Company rep logged in

**Test Steps:**
1. From company rep menu, select "Create Internship Opportunity"
2. Enter all required fields
3. Enter number of slots: 0 or negative number or > 10
4. Attempt to create

**Expected Result:**
- Error message: "Number of slots must be between 1 and 10"
- Internship NOT created
- Prompt to re-enter slot count

---

### TC-CR013: View My Created Internships - With Internships
**Priority:** High
**Preconditions:**
- Company rep logged in
- Company rep has created internships

**Test Steps:**
1. From company rep menu, select "View My Created Internships"
2. View internship list

**Expected Result:**
- All internships created by this company rep displayed
- Shows: ID, Title, Description, Level, Major, Dates, Status, Slots
- Status shown as PENDING, APPROVED, REJECTED, or FILLED
- Option to view details for each internship
- Option to update/delete (if applicable)

---

### TC-CR014: View My Created Internships - No Internships
**Priority:** Low
**Preconditions:**
- Company rep logged in
- Company rep has not created any internships

**Test Steps:**
1. From company rep menu, select "View My Created Internships"

**Expected Result:**
- Message: "You have not created any internships yet"
- Return to company rep menu

---

### TC-CR015: Update Internship - Valid Update (PENDING Status)
**Priority:** High
**Preconditions:**
- Company rep logged in
- Company rep has internship with PENDING status

**Test Steps:**
1. View my created internships
2. Select internship with PENDING status
3. Choose option to update
4. Update fields (title, description, level, major, dates, slots)
5. Confirm update

**Expected Result:**
- Internship updated successfully
- Updated fields saved to internships.csv
- Status remains PENDING
- Success message displayed
- Updated internship appears in listing

---

### TC-CR016: Update Internship - Attempt Update (APPROVED/REJECTED Status)
**Priority:** Medium
**Preconditions:**
- Company rep logged in
- Company rep has internship with APPROVED or REJECTED status

**Test Steps:**
1. View my created internships
2. Select internship with APPROVED/REJECTED status
3. Attempt to choose update option

**Expected Result:**
- Error message: "Cannot update internship that has been approved/rejected"
- Update NOT allowed
- Internship remains unchanged

---

### TC-CR017: Update Internship - Invalid Date Range on Update
**Priority:** Medium
**Preconditions:**
- Company rep logged in
- Updating internship with PENDING status

**Test Steps:**
1. View my created internships
2. Select internship to update
3. Update close date to be before open date
4. Attempt to save

**Expected Result:**
- Error message: "Close date cannot be before open date"
- Update NOT saved
- Prompt to correct dates

---

### TC-CR018: Company Rep Update Password
**Priority:** Medium
**Preconditions:** Company rep logged in
**Test Steps:**
1. From company rep menu, select "Update Password"
2. Enter current password correctly
3. Enter new password
4. Confirm new password (match)
5. Observe result

**Expected Result:**
- Password updated successfully message
- New password saved to CSV file
- Company rep automatically logged out
- Can login with new password

---

### TC-CR019: Company Rep Logout
**Priority:** Medium
**Preconditions:** Company rep logged in
**Test Steps:**
1. From company rep menu, select "Logout"
2. Observe result

**Expected Result:**
- Logged out successfully
- Return to main menu (login screen)
- Current user cleared

---

## Integration Test Cases

### TC-INT001: End-to-End Registration and Approval Flow
**Priority:** Critical
**Test Steps:**
1. Register new company representative
2. Login as staff
3. Review and approve company registration
4. Logout staff
5. Login as approved company rep
6. Create internship
7. Logout company rep
8. Login as staff
9. Approve internship
10. Logout staff
11. Login as student
12. View and verify internship is visible

**Expected Result:**
- Complete workflow executes successfully
- Data persists correctly across sessions
- Status changes reflected in all views
- CSV files updated at each stage

---

### TC-INT002: Student Application to Acceptance Workflow
**Priority:** Critical
**Preconditions:**
- Approved internship exists with available slots
- Student account exists

**Test Steps:**
1. Login as student
2. View internship opportunities
3. Apply for internship
4. Check application status (PENDING)
5. Logout student
6. [Simulate company rep approval - manual CSV edit]
7. Login as student again
8. Check application status (SUCCESSFUL)
9. Accept internship offer
10. Verify employed status
11. Attempt to apply for another internship

**Expected Result:**
- Application progresses through statuses correctly
- Student can accept offer when SUCCESSFUL
- Student becomes employed after acceptance
- Student cannot apply after employment
- All state changes persist in CSV

---

### TC-INT003: Internship Slot Management
**Priority:** High
**Preconditions:**
- Approved internship with 2 slots
- Multiple student accounts

**Test Steps:**
1. Student 1 applies (slots: 2 → 1)
2. Student 2 applies (slots: 1 → 0)
3. Student 3 attempts to apply (slots: 0)
4. Verify slot count

**Expected Result:**
- Slot count decrements with each application
- When slots = 0, internship status may change to FILLED
- Additional students cannot apply to fully booked internship
- Error message shown to Student 3

---

### TC-INT004: Multiple Students Same Internship
**Priority:** Medium
**Preconditions:**
- Approved internship with multiple slots
- Multiple student accounts

**Test Steps:**
1. 3 different students apply for same internship
2. Verify each application is independent
3. Check slot count
4. Verify all applications appear in student records

**Expected Result:**
- All applications accepted (assuming slots available)
- Each application has unique ID
- Slot count decrements correctly
- Each student sees their own application in status check

---

### TC-INT005: Rejection and Re-submission Flow
**Priority:** Medium
**Test Steps:**
1. Register company representative
2. Login as staff
3. Reject company registration
4. Attempt to login as rejected company rep (fail)
5. Re-submit registration with same email
6. Verify status changes to PENDING
7. Staff approves re-submitted registration
8. Login as company rep successfully

**Expected Result:**
- Rejection prevents login
- Re-submission allowed for rejected accounts
- Status correctly updates from REJECTED to PENDING to APPROVED
- Final approval allows access

---

### TC-INT006: Company Rep Creates Multiple Internships
**Priority:** Medium
**Preconditions:** Company rep logged in
**Test Steps:**
1. Create internship 1
2. Create internship 2
3. Create internship 3
4. Create internship 4
5. Create internship 5
6. Attempt to create internship 6

**Expected Result:**
- First 5 internships created successfully
- 6th internship rejected with max limit error
- All 5 internships visible in company rep's listing
- All 5 saved to CSV

---

### TC-INT007: Student Application Limit Enforcement
**Priority:** High
**Preconditions:**
- Student logged in
- 4+ approved internships available

**Test Steps:**
1. Apply for internship 1 (total: 1)
2. Apply for internship 2 (total: 2)
3. Apply for internship 3 (total: 3)
4. Attempt to apply for internship 4

**Expected Result:**
- First 3 applications successful
- 4th application rejected
- Error: "Cannot apply for more than 3 internships at a time"

---

### TC-INT008: Password Update and Re-login
**Priority:** Medium
**Test Steps:**
1. Login as any user type
2. Update password
3. Automatic logout
4. Attempt login with old password (fail)
5. Login with new password (success)

**Expected Result:**
- Password update successful
- Automatic logout after update
- Old password no longer works
- New password saved to CSV
- New password works for subsequent logins

---

### TC-INT009: Data Persistence Across Sessions
**Priority:** High
**Test Steps:**
1. Login as student, apply for internship
2. Logout and exit application
3. Re-launch application
4. Login as same student
5. Check application status

**Expected Result:**
- Application persists across sessions
- Data loaded from CSV correctly
- Application status maintained
- All user data intact

---

### TC-INT010: Concurrent User Type Operations
**Priority:** Medium
**Test Steps:**
1. Login as Staff → Approve internship → Logout
2. Login as Student → View internships → Apply → Logout
3. Login as Company Rep → View applications (if feature exists) → Logout
4. Verify data consistency

**Expected Result:**
- All operations execute correctly
- No data corruption
- Each user sees correct view of data
- CSV files updated correctly for each operation

---

## Validation and Business Rules Test Cases

### TC-VAL001: Student Year Eligibility - Year 1 Student, BASIC Internship
**Priority:** High
**Test Data:** Year 1 student, BASIC level internship
**Expected Result:** Application allowed

---

### TC-VAL002: Student Year Eligibility - Year 1 Student, INTERMEDIATE Internship
**Priority:** High
**Test Data:** Year 1 student, INTERMEDIATE level internship
**Expected Result:** Application denied with error message

---

### TC-VAL003: Student Year Eligibility - Year 3 Student, ADVANCED Internship
**Priority:** High
**Test Data:** Year 3 student, ADVANCED level internship
**Expected Result:** Application allowed

---

### TC-VAL004: Major Matching - Student Major Matches Internship
**Priority:** High
**Test Data:** CS student, internship requires CS major
**Expected Result:** Application allowed

---

### TC-VAL005: Major Matching - Student Major Does Not Match
**Priority:** High
**Test Data:** DSAI student, internship requires CS major
**Expected Result:** Application denied with error message

---

### TC-VAL006: Date Validation - Current Date Within Application Period
**Priority:** High
**Test Data:**
- Current date: 2025-02-01
- Open date: 2025-01-15
- Close date: 2025-02-28

**Expected Result:** Application allowed

---

### TC-VAL007: Date Validation - Current Date Before Open Date
**Priority:** High
**Test Data:**
- Current date: 2025-01-10
- Open date: 2025-01-15
- Close date: 2025-02-28

**Expected Result:** Application denied, period not open yet

---

### TC-VAL008: Date Validation - Current Date After Close Date
**Priority:** High
**Test Data:**
- Current date: 2025-03-01
- Open date: 2025-01-15
- Close date: 2025-02-28

**Expected Result:** Application denied, period closed

---

### TC-VAL009: Slot Availability - Slots Available
**Priority:** High
**Test Data:** Internship with slots > 0
**Expected Result:** Application allowed

---

### TC-VAL010: Slot Availability - No Slots Available
**Priority:** High
**Test Data:** Internship with slots = 0
**Expected Result:** Application denied, internship fully booked

---

### TC-VAL011: Email Uniqueness - New Email
**Priority:** High
**Test Data:** Email not in companyreps.csv
**Expected Result:** Registration allowed

---

### TC-VAL012: Email Uniqueness - Duplicate Email (APPROVED)
**Priority:** High
**Test Data:** Email exists with APPROVED status
**Expected Result:** Registration denied

---

### TC-VAL013: Empty Field Validation - Name Field
**Priority:** Medium
**Test Data:** Empty name field in any registration form
**Expected Result:** Error message, field required

---

### TC-VAL014: Empty Field Validation - Email Field
**Priority:** Medium
**Test Data:** Empty email field
**Expected Result:** Error message, field required

---

### TC-VAL015: Numeric Input Validation - Year of Study
**Priority:** Low
**Test Data:** Non-numeric input for year
**Expected Result:** Error message, numeric input required

---

### TC-VAL016: Numeric Input Validation - Number of Slots
**Priority:** Low
**Test Data:** Non-numeric input for slots
**Expected Result:** Error message, numeric input required

---

### TC-VAL017: Date Format Validation - Invalid Date Format
**Priority:** Medium
**Test Data:** Date entered as "15/01/2025" instead of "2025-01-15"
**Expected Result:** Error message, correct format required

---

### TC-VAL018: Boundary Testing - Maximum Slots (10)
**Priority:** Low
**Test Data:** Slots = 10
**Expected Result:** Accepted

---

### TC-VAL019: Boundary Testing - Minimum Slots (1)
**Priority:** Low
**Test Data:** Slots = 1
**Expected Result:** Accepted

---

### TC-VAL020: Boundary Testing - Slots Out of Range (11)
**Priority:** Low
**Test Data:** Slots = 11
**Expected Result:** Error message, max 10 slots

---

### TC-VAL021: CSV File Handling - File Not Found
**Priority:** High
**Test Steps:**
1. Delete one of the CSV files
2. Launch application
3. Observe error handling

**Expected Result:**
- Error message: "CSV file not found"
- Application handles gracefully
- Does not crash

---

### TC-VAL022: CSV File Handling - Corrupted CSV Data
**Priority:** Medium
**Test Steps:**
1. Corrupt data in CSV file (missing columns, invalid data)
2. Launch application
3. Observe error handling

**Expected Result:**
- Error message displayed
- Application handles gracefully
- Does not crash
- May skip corrupted records

---

### TC-VAL023: Invalid User Type Selection
**Priority:** Low
**Test Steps:**
1. At main menu, enter invalid number (e.g., 99)
2. Observe result

**Expected Result:**
- Error message: "Invalid user type"
- Prompt to enter valid option
- Return to main menu

---

### TC-VAL024: Special Characters in Text Fields
**Priority:** Low
**Test Data:** Name with special characters: "John@Doe#123"
**Expected Result:**
- Either: Accepted if no restrictions
- Or: Error message if special characters not allowed

---

### TC-VAL025: Maximum Length Validation
**Priority:** Low
**Test Data:** Very long strings (1000+ characters) in text fields
**Expected Result:**
- Either: Accepted if no length restriction
- Or: Error/truncation if length restricted
- No system crash

---

## Test Execution Summary

### Test Execution Template

| Test ID | Test Case Name | Priority | Status | Executed By | Date | Result | Defects | Notes |
|---------|----------------|----------|--------|-------------|------|--------|---------|-------|
| TC-S001 | Student Login - Valid | High | | | | PASS/FAIL | | |
| TC-S002 | Student Login - Invalid Password | High | | | | PASS/FAIL | | |
| TC-S003 | View Internships - No Filters | High | | | | PASS/FAIL | | |
| ... | ... | ... | | | | | | |

### Test Execution Status
- **Not Started:** Test not yet executed
- **In Progress:** Test execution underway
- **Passed:** Test executed successfully, expected results met
- **Failed:** Test executed, defect found
- **Blocked:** Test cannot execute due to blocker

### Test Coverage

| Module | Total Test Cases | Executed | Passed | Failed | Blocked | Coverage % |
|--------|------------------|----------|--------|--------|---------|------------|
| Student Module | 20 | | | | | |
| Career Center Staff Module | 12 | | | | | |
| Company Representative Module | 19 | | | | | |
| Integration Tests | 10 | | | | | |
| Validation & Business Rules | 25 | | | | | |
| **TOTAL** | **86** | **0** | **0** | **0** | **0** | **0%** |

---

## Defect Tracking

### Defect Log Template

| Defect ID | Test Case ID | Priority | Severity | Description | Steps to Reproduce | Status | Assigned To | Resolution |
|-----------|--------------|----------|----------|-------------|-------------------|--------|-------------|------------|
| DEF-001 | | | | | | Open | | |
| DEF-002 | | | | | | Open | | |

### Defect Severity Levels
- **Critical:** System crash, data loss, security breach
- **High:** Major functionality broken, no workaround
- **Medium:** Functionality impaired, workaround available
- **Low:** Minor issue, cosmetic, enhancement

### Defect Priority Levels
- **High:** Fix immediately, blocks testing
- **Medium:** Fix in current iteration
- **Low:** Fix in future iteration, if time permits

### Defect Status
- **Open:** Defect logged, not yet assigned
- **Assigned:** Assigned to developer
- **In Progress:** Developer working on fix
- **Fixed:** Fix implemented, awaiting verification
- **Verified:** Fix tested and confirmed
- **Closed:** Defect resolved and closed
- **Deferred:** Fix postponed to future release

---

## Conclusion

### Test Summary
This comprehensive testing document covers 86 test cases across all modules of the Internship Management System, including:
- 20 Student module test cases
- 12 Career Center Staff module test cases
- 19 Company Representative module test cases
- 10 Integration test cases
- 25 Validation and business rules test cases

### Testing Goals
1. Ensure all functional requirements are met
2. Validate business rules and constraints
3. Verify data persistence and integrity
4. Confirm proper error handling
5. Test user experience across all roles

### Recommendations
1. Execute all High priority test cases first
2. Automate repetitive test cases for regression testing
3. Maintain test data in separate CSV files for testing
4. Document all defects with screenshots
5. Perform regression testing after each defect fix
6. Conduct user acceptance testing with stakeholders

### Sign-off
- **Test Plan Approved By:** ___________________ Date: ___________
- **Test Execution Completed By:** ___________________ Date: ___________
- **Test Results Reviewed By:** ___________________ Date: ___________

---

## Appendix

### A. Test Data Samples

#### Sample Student Data
```csv
StudentID,Name,Major,Year,Email
S001,Alice Tan,Computer Science,2,alice.tan@student.ntu.edu.sg
S002,Bob Lee,Data Science and AI,3,bob.lee@student.ntu.edu.sg
S003,Charlie Ng,Computer Engineering,1,charlie.ng@student.ntu.edu.sg
```

#### Sample Staff Data
```csv
StaffID,Name,Role,Department,Email
STAFF001,Dr. Sarah Wong,Advisor,Career Services,sarah.wong@ntu.edu.sg
STAFF002,Mr. David Lim,Coordinator,Student Affairs,david.lim@ntu.edu.sg
```

#### Sample Company Rep Data
```csv
RepID,Name,CompanyName,Department,Position,Email,Status
REP001,John Smith,Tech Innovations,HR,Recruiter,john.smith@techinnov.com,APPROVED
REP002,Mary Johnson,Data Solutions,Talent Acquisition,Manager,mary.j@datasol.com,PENDING
REP003,Peter Brown,Cloud Systems,People Ops,Lead,peter.b@cloud.com,REJECTED
```

#### Sample Internship Data
```csv
InternshipID,Title,Description,Level,PreferredMajor,OpenDate,CloseDate,Status,CompanyName,CompanyRep,Slots
I001,Software Intern,Backend development,INTERMEDIATE,Computer Science,2025-01-15,2025-02-28,APPROVED,Tech Innovations,REP001,5
I002,Data Analyst Intern,Data analysis and visualization,BASIC,Data Science and AI,2025-02-01,2025-03-15,PENDING,Data Solutions,REP002,3
```

### B. Test Environment Setup Instructions
1. Install Java JDK 8 or higher
2. Create directory structure: `InternshipSystem/src/team5/`
3. Place CSV files in appropriate directory
4. Configure `application.properties` with correct file paths
5. Compile Java files: `javac team5/*.java team5/**/*.java`
6. Run application: `java team5.App`

### C. CSV File Locations
- Students: `data/students.csv`
- Staff: `data/staff.csv`
- Company Reps: `data/companyreps.csv`
- Internships: `data/internships.csv`

### D. Useful Commands
- Compile: `javac -d bin src/team5/**/*.java`
- Run: `java -cp bin team5.App`
- View CSV: `cat data/students.csv`
- Backup CSV: `cp data/*.csv backup/`

---

**Document Version:** 1.0
**Last Updated:** November 2025
**Document Owner:** Team 5, CT2003
**Status:** Draft / Under Review / Approved

