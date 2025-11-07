# Testing Quick Guide
**Internship Management System - CT2003 Team 5**

---

## Overview

This guide provides quick instructions on how to use the testing materials for the final report and presentation.

---

## Testing Documents

### 1. TESTING_DOCUMENT.md
**Purpose:** Comprehensive testing documentation with all test cases
**Use for:**
- Understanding complete test coverage
- Reference during test execution
- Include in final report
- Presentation slides preparation

**Key Sections:**
- Executive Summary (for presentation)
- 86 detailed test cases organized by module
- Integration tests
- Validation tests
- Test strategy and plan

### 2. TEST_EXECUTION_TRACKER.csv
**Purpose:** Track execution status of all test cases
**Use for:**
- Recording test results during execution
- Tracking progress (Not Started → In Progress → Passed/Failed)
- Generating test execution statistics
- Include in final report

**How to use:**
1. Open in Excel or Google Sheets
2. Update "Status" column as tests are executed:
   - Not Started
   - In Progress
   - Passed
   - Failed
   - Blocked
3. Fill in "Executed By", "Date", "Result" columns
4. Add "Defect ID" if test fails
5. Add any relevant "Notes"

### 3. DEFECT_TRACKER.csv
**Purpose:** Log and track defects found during testing
**Use for:**
- Recording bugs and issues
- Tracking defect resolution
- Generating defect statistics
- Include in final report

**How to use:**
1. When a test fails, create a new defect entry
2. Assign unique Defect ID (DEF-001, DEF-002, etc.)
3. Link to Test Case ID
4. Fill in all columns with relevant information
5. Update status as defect is worked on

---

## Test Execution Process

### Step 1: Preparation
1. Review TESTING_DOCUMENT.md to understand all test cases
2. Set up test environment (CSV files with test data)
3. Create backup of original CSV files
4. Open TEST_EXECUTION_TRACKER.csv in Excel/Google Sheets

### Step 2: Test Execution Order
**Recommended execution sequence:**

**Phase 1 - Smoke Testing (High Priority):**
- TC-S001: Student Login
- TC-CS001: Staff Login
- TC-CR008: Company Rep Login (Approved)
- TC-CR001: Company Rep Registration

**Phase 2 - Core Functionality:**
- All Student test cases (TC-S001 to TC-S020)
- All Staff test cases (TC-CS001 to TC-CS012)
- All Company Rep test cases (TC-CR001 to TC-CR019)

**Phase 3 - Integration Testing:**
- TC-INT001 to TC-INT010

**Phase 4 - Validation Testing:**
- TC-VAL001 to TC-VAL025

### Step 3: Executing a Test Case
1. Read test case from TESTING_DOCUMENT.md
2. Note preconditions and set up test data
3. Execute test steps one by one
4. Compare actual result with expected result
5. Record result in TEST_EXECUTION_TRACKER.csv:
   - PASS if actual = expected
   - FAIL if actual ≠ expected
6. If FAIL, create defect in DEFECT_TRACKER.csv
7. Take screenshots for evidence (optional but recommended)

### Step 4: Defect Logging
When a test fails:
1. Open DEFECT_TRACKER.csv
2. Create new entry with unique ID
3. Fill in all fields:
   - **Defect ID:** DEF-XXX
   - **Test Case ID:** TC-XXX
   - **Module:** Student/Staff/Company Rep/Integration/Validation
   - **Priority:** High/Medium/Low
   - **Severity:** Critical/High/Medium/Low
   - **Description:** Brief description of the issue
   - **Steps to Reproduce:** How to replicate the bug
   - **Expected Result:** What should happen
   - **Actual Result:** What actually happened
   - **Status:** Open/In Progress/Fixed/Verified/Closed
   - **Assigned To:** Team member name
   - **Date Found:** Date of discovery
4. Save and share with team

---

## Test Data Setup

### Sample Test Data
Create test CSV files with the following data:

**students.csv:**
```csv
StudentID,Name,Major,Year,Email
S001,Alice Tan,Computer Science,1,alice.tan@student.ntu.edu.sg
S002,Bob Lee,Data Science and AI,2,bob.lee@student.ntu.edu.sg
S003,Charlie Ng,Computer Science,3,charlie.ng@student.ntu.edu.sg
S004,Diana Wong,Computer Engineering,4,diana.wong@student.ntu.edu.sg
```

**staff.csv:**
```csv
StaffID,Name,Role,Department,Email
STAFF001,Dr. Sarah Wong,Advisor,Career Services,sarah.wong@ntu.edu.sg
STAFF002,Mr. David Lim,Coordinator,Student Affairs,david.lim@ntu.edu.sg
```

**companyreps.csv:**
```csv
RepID,Name,CompanyName,Department,Position,Email,Status
REP001,John Smith,Tech Innovations,HR,Recruiter,john.smith@techinnov.com,APPROVED
REP002,Mary Johnson,Data Solutions,Talent,Manager,mary.j@datasol.com,PENDING
REP003,Peter Brown,Cloud Systems,People Ops,Lead,peter.b@cloud.com,REJECTED
```

**internships.csv:**
```csv
InternshipID,Title,Description,Level,PreferredMajor,OpenDate,CloseDate,Status,CompanyName,CompanyRep,Slots
I001,Software Intern,Backend development,BASIC,Computer Science,01 11 2025,28 02 2026,APPROVED,Tech Innovations,REP001,5
I002,Data Analyst Intern,Data analysis,INTERMEDIATE,Data Science and AI,01 12 2025,15 03 2026,PENDING,Data Solutions,REP002,3
I003,Cloud Engineer,Cloud infrastructure,ADVANCED,Computer Engineering,15 11 2025,31 01 2026,APPROVED,Cloud Systems,REP001,2
```

**Default Passwords:** All accounts use "password" as default

---

## Generating Test Reports

### For Final Report

**Test Summary Statistics:**
1. Open TEST_EXECUTION_TRACKER.csv
2. Count:
   - Total test cases: 86
   - Executed: (count of Passed + Failed)
   - Passed: (count of "Passed")
   - Failed: (count of "Failed")
   - Pass rate: (Passed / Executed) × 100%

**Example Table for Report:**
```
Module                    | Total Tests | Passed | Failed | Pass Rate
--------------------------|-------------|--------|--------|----------
Student Module            |     20      |   18   |   2    |   90%
Career Center Staff       |     12      |   12   |   0    |  100%
Company Representative    |     19      |   17   |   2    |   89%
Integration Tests         |     10      |    9   |   1    |   90%
Validation Tests          |     25      |   23   |   2    |   92%
--------------------------|-------------|--------|--------|----------
TOTAL                     |     86      |   79   |   7    |   92%
```

**Defect Summary:**
1. Open DEFECT_TRACKER.csv
2. Count defects by:
   - Severity (Critical/High/Medium/Low)
   - Status (Open/Fixed/Closed)
   - Module

**Example Table:**
```
Severity  | Total | Open | Fixed | Closed
----------|-------|------|-------|-------
Critical  |   0   |  0   |   0   |   0
High      |   2   |  0   |   2   |   2
Medium    |   3   |  1   |   2   |   2
Low       |   2   |  0   |   1   |   2
```

### For Presentation

**Key Slides to Include:**

**Slide 1: Testing Overview**
- Total test cases: 86
- Test coverage: All modules covered
- Test types: Functional, Integration, Validation

**Slide 2: Test Execution Summary**
- Pie chart: Passed vs Failed
- Pass rate: X%
- Execution timeline

**Slide 3: Test Coverage by Module**
- Bar chart showing tests per module
- Pass rates per module

**Slide 4: Key Findings**
- List 3-5 major test scenarios executed
- Highlight critical path testing
- Show end-to-end workflow validation

**Slide 5: Defects Found and Resolved**
- Total defects: X
- Resolved: X
- Outstanding: X
- Defect severity breakdown

**Slide 6: Quality Metrics**
- Pass rate: X%
- Defect density: X defects per module
- Critical defects: 0 (ideally)

---

## Tips for Effective Testing

### Do's:
- ✓ Test high-priority cases first
- ✓ Create backups before testing
- ✓ Record actual results accurately
- ✓ Take screenshots of failures
- ✓ Test both positive and negative scenarios
- ✓ Verify data persists in CSV files
- ✓ Re-test after bug fixes (regression testing)

### Don'ts:
- ✗ Skip test steps
- ✗ Mark test as passed without verification
- ✗ Test with production data
- ✗ Ignore minor defects
- ✗ Rush through test execution

---

## Common Issues and Solutions

### Issue 1: CSV File Not Found
**Solution:** Check application.properties file path configuration

### Issue 2: Data Not Persisting
**Solution:** Verify CSV file write permissions

### Issue 3: Application Crashes
**Solution:** Check for corrupted CSV data or invalid input format

### Issue 4: Cannot Login After Password Change
**Solution:** Check if new password was correctly saved to CSV

---

## Test Completion Checklist

Before finalizing the report:

- [ ] All 86 test cases executed
- [ ] TEST_EXECUTION_TRACKER.csv completely filled out
- [ ] All defects logged in DEFECT_TRACKER.csv
- [ ] Test summary statistics calculated
- [ ] Screenshots captured for key tests
- [ ] Defect resolution status updated
- [ ] Pass rate meets acceptance criteria (aim for >90%)
- [ ] Critical and high-priority defects resolved
- [ ] Regression testing completed for fixed defects
- [ ] Test report section written for final report
- [ ] Presentation slides prepared with test metrics

---

## Contact and Support

For questions about testing:
- Review TESTING_DOCUMENT.md for detailed test cases
- Check test case preconditions carefully
- Ensure test data is correctly set up
- Verify CSV file formats match expected structure

---

## Appendix: Quick Reference

### Test Priority Definitions
- **High:** Core functionality, must pass
- **Medium:** Important features, should pass
- **Low:** Minor features, nice to pass

### Test Result Codes
- **PASS:** Test executed successfully, actual = expected
- **FAIL:** Test executed, actual ≠ expected, defect logged
- **BLOCKED:** Cannot execute due to blocker (environment, dependency)
- **SKIPPED:** Test intentionally not executed

### Severity vs Priority
- **Severity:** Impact of defect (Critical/High/Medium/Low)
- **Priority:** Urgency of fix (High/Medium/Low)

Example:
- Critical Severity + High Priority = Fix immediately
- Low Severity + Low Priority = Fix if time permits

---

**Team 5, CT2003**
