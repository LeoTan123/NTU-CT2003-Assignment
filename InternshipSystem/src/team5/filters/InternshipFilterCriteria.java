package team5.filters;

import java.time.LocalDate;

import team5.enums.InternshipLevel;
import team5.enums.InternshipStatus;
import team5.enums.StudentMajor;

// for internship filtering

public class InternshipFilterCriteria {

	private StudentMajor preferredMajor;
	private InternshipLevel internshipLevel;
	private InternshipStatus internshipStatus;
	private LocalDate applicationOpenFrom;
	private LocalDate applicationCloseTo;

	public StudentMajor getPreferredMajor() {
		return preferredMajor;
	}

	public void setPreferredMajor(StudentMajor preferredMajor) {
		this.preferredMajor = preferredMajor;
	}

	public InternshipLevel getInternshipLevel() {
		return internshipLevel;
	}

	public void setInternshipLevel(InternshipLevel internshipLevel) {
		this.internshipLevel = internshipLevel;
	}

	public InternshipStatus getInternshipStatus() {
		return internshipStatus;
	}

	public void setInternshipStatus(InternshipStatus internshipStatus) {
		this.internshipStatus = internshipStatus;
	}

	public LocalDate getApplicationOpenFrom() {
		return applicationOpenFrom;
	}

	public void setApplicationOpenFrom(LocalDate applicationOpenFrom) {
		this.applicationOpenFrom = applicationOpenFrom;
	}

	public LocalDate getApplicationCloseTo() {
		return applicationCloseTo;
	}

	public void setApplicationCloseTo(LocalDate applicationCloseTo) {
		this.applicationCloseTo = applicationCloseTo;
	}

	public boolean isEmpty() {
		return preferredMajor == null
				&& internshipLevel == null
				&& internshipStatus == null
				&& applicationOpenFrom == null
				&& applicationCloseTo == null;
	}
}
