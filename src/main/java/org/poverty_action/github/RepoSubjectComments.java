package org.poverty_action.github;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jcabi.github.Repo;

// The commit comments of a repository, collected by subject
class RepoSubjectComments implements Iterable<SubjectComments> {
	private final Repo repo;
	private final SortedSet<SubjectComments> repoComments;
	private final int size;

	private static class SubjectComparator
		implements Comparator<SubjectComments> {
		private static final ReflectiveSubjectComparator subjectComparator =
			new ReflectiveSubjectComparator() {
			@Override
			public int compare(CommitAsSubject s0, CommitAsSubject s1) {
				return 0;
			}

			@Override
			public int compare(CommitAsSubject s0, CommitLine s1) {
				return -1;
			}

			@Override
			public int compare(CommitLine s0, CommitAsSubject s1) {
				return 1;
			}

			@Override
			public int compare(CommitLine s0, CommitLine s1) {
				return s0.compareTo(s1);
			}
		};

		@Override
		public int compare(SubjectComments sc0, SubjectComments sc1) {
			return subjectComparator.compare(sc0.subject(), sc1.subject());
		}
	}
	private static final Comparator<SubjectComments> subjectComparator =
		new SubjectComparator();

	public RepoSubjectComments(Repo repo) throws IOException {
		if (repo == null)
			throw new NullPointerException();
		this.repo = repo;

		Map<CommitCommentSubject, Set<CommitComment>> commentsBySubject =
			new HashMap<>();
		for (CommitComment comment : new CommitComments(repo)) {
			CommitCommentSubject subject = comment.subject();
			Set<CommitComment> comments = commentsBySubject.get(subject);
			if (comments == null) {
				comments = new HashSet<>();
				commentsBySubject.put(subject, comments);
			}
			comments.add(comment);
		}

		repoComments = new TreeSet<>(subjectComparator);
		int size = 0;
		for (Entry<CommitCommentSubject, Set<CommitComment>> entry :
			commentsBySubject.entrySet()) {
			repoComments.add(new SubjectComments(entry.getKey(),
				entry.getValue()));
			size += entry.getValue().size();
		}
		this.size = size;
	}

	public Repo repo() { return repo; }
	public int size() { return size; }

	@Override
	public Iterator<SubjectComments> iterator() {
		return new NoRemoveIterator<>(repoComments.iterator());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RepoSubjectComments))
			return false;
		RepoSubjectComments that = (RepoSubjectComments) o;
		return this.repo.equals(that.repo) &&
			this.repoComments.equals(that.repoComments);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + repo.hashCode();
		result = prime * result + repoComments.hashCode();
		return result;
	}
}
