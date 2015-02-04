package org.poverty_action.github;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jcabi.github.Issue;
import com.jcabi.github.Repo;

// All issues of a repository
class SmartIssues implements Iterable<Issue.Smart> {
	private final SortedSet<Issue.Smart> issues;

	private static final Comparator<Issue.Smart> issueNumberComparator =
		new Comparator<Issue.Smart>() {
			@Override
			public int compare(Issue.Smart issue1, Issue.Smart issue2) {
				return issue1.number() - issue2.number();
			}
		};

	private static final Map<String, String> iterateParams;
	static {
		iterateParams = new HashMap<>(3);
		iterateParams.put("filter", "all");
		iterateParams.put("state",  "all");
		// This parameter doesn't seem to be working, hence issues is
		// a SortedSet.
		iterateParams.put("direction", "asc");
	}

	public SmartIssues(Repo repo) {
		if (repo == null)
			throw new NullPointerException();

		issues = new TreeSet<>(issueNumberComparator);
		for (Issue issue : repo.issues().iterate(iterateParams))
			issues.add(new Issue.Smart(issue));
	}

	@Override
	// Iterates over issues according to their number.
	public Iterator<Issue.Smart> iterator() {
		return new NoRemoveIterator<>(issues.iterator());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SmartIssues))
			return false;
		return issues.equals(((SmartIssues) o).issues);
	}

	@Override
	public int hashCode() {
		return issues.hashCode();
	}
}
