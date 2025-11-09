package team5.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import team5.Internship;

/**
 * Applies {@link InternshipFilterCriteria} to a list of internships.
 */
public final class InternshipFilter {

	private InternshipFilter() {
	}

	public static List<Internship> apply(List<Internship> internships, InternshipFilterCriteria criteria) {
		if (internships == null) {
			return new ArrayList<>();
		}
		if (criteria == null || criteria.isEmpty()) {
			return new ArrayList<>(internships);
		}

		return internships.stream()
				.filter(i -> criteria.getPreferredMajor() == null
						|| (i.getPreferredMajor() != null && i.getPreferredMajor() == criteria.getPreferredMajor()))
				.filter(i -> criteria.getInternshipLevel() == null
						|| (i.getInternshipLevel() != null && i.getInternshipLevel() == criteria.getInternshipLevel()))
				.filter(i -> criteria.getInternshipStatus() == null
						|| (i.getInternshipStatus() != null && i.getInternshipStatus() == criteria.getInternshipStatus()))
				.filter(i -> criteria.getApplicationOpenFrom() == null
						|| (i.getApplicationOpenDate() != null
								&& !i.getApplicationOpenDate().isBefore(criteria.getApplicationOpenFrom())))
				.filter(i -> criteria.getApplicationCloseTo() == null
						|| (i.getApplicationCloseDate() != null
								&& !i.getApplicationCloseDate().isAfter(criteria.getApplicationCloseTo())))
				.collect(Collectors.toList());
	}
}
