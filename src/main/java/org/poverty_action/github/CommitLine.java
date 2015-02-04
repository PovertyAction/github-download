package org.poverty_action.github;

import java.io.IOException;

import com.jcabi.github.Commit;

// Line of a file in a commit
class CommitLine implements CommitCommentSubject, Comparable<CommitLine> {
	private final Commit commit;
	private final String path;
	private final int line;

	private final CommitAuthor author;

	public CommitLine(CommitComment comment) throws IOException {
		commit = comment.commit();
		path   = comment.json().getString("path");
		line   = comment.json().getInt("line");

		author = new CommitAuthor(commit);
	}

	@Override public Commit commit() { return commit; }
	public String path() { return path; }
	public int line() { return line; }

	@Override
	public void accept(SubjectVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CommitLine))
			return false;
		CommitLine that = (CommitLine) o;
		return this.commit.equals(that.commit) && this.path.equals(that.path) &&
			this.line == that.line;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + commit.hashCode();
		result = prime * result + path.hashCode();
		result = prime * result + line;
		return result;
	}

	@Override
	public int compareTo(CommitLine commitLine) {
		int compare = author.date().compareTo(commitLine.author.date());
		if (compare == 0) {
			compare = path.compareTo(commitLine.path);
			if (compare == 0)
				compare = line - commitLine.line;
		}
		return compare;
	}

	@Override
	public String toString() {
		return String.format("%s, %s, line %d", commit.sha(), path, line);
	}
}
