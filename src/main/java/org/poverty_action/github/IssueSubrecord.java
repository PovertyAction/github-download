package org.poverty_action.github;

import java.io.IOException;
import java.util.Date;

import com.jcabi.github.Comment;
import com.jcabi.github.Commit;
import com.jcabi.github.Issue;
import com.jcabi.github.User;
import com.jcabi.github.Users;

// Subrecord of an issues .csv file
class IssueSubrecord implements Comparable<IssueSubrecord> {
	private final String author;
	private final Date createdAt;
	private final String body;

	public IssueSubrecord(String author, Date createdAt, String body) {
		if (author == null || createdAt == null || body == null)
			throw new NullPointerException();
		this.author    = author;
		this.createdAt = createdAt;
		this.body      = body;
	}

	private IssueSubrecord(UserName name, Date createdAt, String body) {
		this(name.nameOrLogin(), createdAt, body);
	}

	private IssueSubrecord(User author, Date createdAt, String body)
		throws IOException {
		this(UserNames.get(author), createdAt, body);
	}

	public IssueSubrecord(Comment.Smart comment) throws IOException {
		this(comment.author(), comment.createdAt(), comment.body());
	}

	public IssueSubrecord(Issue.Smart issue) throws IOException {
		this(issue.author(), issue.createdAt(), issue.body());
	}

	private IssueSubrecord(CommitAuthor author, Users users, String body)
		throws IOException {
		this(UserNames.get(author.name(), users), author.date(), body);
	}

	public IssueSubrecord(Commit commit) throws IOException {
		this(new CommitAuthor(commit), commit.repo().github().users(),
			commit.sha());
	}

	public String author() { return author; }
	public Date createdAt() { return createdAt; }
	public String body() { return body; }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof IssueSubrecord))
			return false;
		IssueSubrecord that = (IssueSubrecord) o;
		return this.author.equals(that.author) &&
			this.createdAt.equals(that.createdAt) &&
			this.body.equals(that.body);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + author().hashCode();
		result = prime * result + createdAt().hashCode();
		result = prime * result + body().hashCode();
		return result;
	}

	@Override
	public int compareTo(IssueSubrecord is) {
		return createdAt().compareTo(is.createdAt());
	}
}
