package org.poverty_action.github;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

// The comments of a commit comment subject
class SubjectComments implements Iterable<CommitComment> {
	private final CommitCommentSubject subject;
	private final SortedSet<CommitComment> comments;

	private static final Comparator<CommitComment> commentTimeComparator =
		new Comparator<CommitComment>() {
			@Override
			public int compare(CommitComment c0, CommitComment c1) {
				return c0.createdAt().compareTo(c1.createdAt());
			}
		};

	public SubjectComments(CommitCommentSubject subject,
		Set<CommitComment> comments) throws IOException {
		if (subject == null || comments == null)
			throw new NullPointerException();

		this.subject = subject;

		this.comments = new TreeSet<>(commentTimeComparator);
		this.comments.addAll(comments);
		for (CommitComment comment : comments)
			if (!comment.subject().equals(subject))
				throw new IllegalArgumentException("inconsistent comment subject");
	}

	public CommitCommentSubject subject() { return subject; }

	public int size() { return comments.size(); }

	@Override
	public Iterator<CommitComment> iterator() {
		return new NoRemoveIterator<>(comments.iterator());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SubjectComments))
			return false;
		SubjectComments that = (SubjectComments) o;
		return this.subject.equals(that.subject) &&
			this.comments.equals(that.comments);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + subject.hashCode();
		result = prime * result + comments.hashCode();
		return result;
	}
}
