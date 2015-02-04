package org.poverty_action.github;

import com.jcabi.github.Commit;

// A commit as a commit comment subject
class CommitAsSubject implements CommitCommentSubject {
	private final Commit commit;

	public CommitAsSubject(CommitComment comment) {
		commit = comment.commit();
	}

	@Override public Commit commit() { return commit; }

	@Override
	public void accept(SubjectVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CommitAsSubject))
			return false;
		return commit.equals(((CommitAsSubject) o).commit);
	}

	@Override
	public int hashCode() {
		return commit.hashCode();
	}

	@Override
	public String toString() {
		return commit.sha();
	}
}
