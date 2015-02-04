package org.poverty_action.github;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.json.JsonObject;

import com.jcabi.github.Commit;

class CommitAuthor {
	private final String name;
	private final Date date;
	private final Commit commit;

	public CommitAuthor(Commit commit) throws IOException {
		if (commit == null)
			throw new NullPointerException();
		JsonObject author = commit.json().getJsonObject("author");
		name = author.getString("name");
		try {
			date =
				GitHubDateFormat.get().parse(author.getString("date"));
		}
		catch (ParseException e) {
			throw new AssertionError("invalid author:date", e);
		}
		this.commit = commit;
	}

	public String name() { return name; }
	public Date date() { return date; }
	public Commit commit() { return commit; }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CommitAuthor))
			return false;
		CommitAuthor that = (CommitAuthor) o;
		return this.name.equals(that.name) && this.date.equals(that.date) &&
			this.commit.equals(that.commit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + name.hashCode();
		result = prime * result + date.hashCode();
		result = prime * result + commit.hashCode();
		return result;
	}
}
