package org.poverty_action.github;

import com.jcabi.github.Commit;

// The subject of a commit comment: the commit itself or a file line.
interface CommitCommentSubject {
	Commit commit();
	void accept(SubjectVisitor visitor);
}
