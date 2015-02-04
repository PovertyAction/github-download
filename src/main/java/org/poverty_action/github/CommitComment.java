package org.poverty_action.github;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.json.JsonObject;

import com.jcabi.github.Commit;
import com.jcabi.github.JsonReadable;
import com.jcabi.github.Repo;
import com.jcabi.github.User;

// A commit comment
class CommitComment implements JsonReadable {
	private final Repo repo;
	private final JsonObject json;

	// Extracted from repo and json
	private final User author;
	private final String body;
	private final Commit commit;
	private final Date createdAt;
	private final String htmlUrl;
	private final int id;
	private final CommitCommentSubject subject;

	CommitComment(Repo repo, JsonObject json) throws IOException {
		if (repo == null || json == null)
			throw new NullPointerException();
		this.repo = repo;
		this.json = json;

		author = repo.github().users().
			get(json.getJsonObject("user").getString("login"));
		body   = json.getString("body");
		commit = repo.git().commits().get(json.getString("commit_id"));
		try {
			createdAt =
				GitHubDateFormat.get().parse(json.getString("created_at"));
		}
		catch (ParseException e) {
			throw new AssertionError("invalid created_at", e);
		}
		htmlUrl = json.getString("html_url");
		id      = json.getInt("id");
		// Use "line" not "path" for the null test: sometimes "path" is "" when
		// it should be null.
		subject = json.isNull("line") ? new CommitAsSubject(this) :
			new CommitLine(this);
	}

	// Getters
	public User author() { return author; }
	public String body() { return body; }
	public Commit commit() { return commit; }
	public Date createdAt() { return createdAt; }
	public String htmlUrl() { return htmlUrl; }
	public int id() { return id; }
	@Override public JsonObject json() { return json; }
	public Repo repo() { return repo; }
	public CommitCommentSubject subject() { return subject; }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CommitComment))
			return false;
		CommitComment that = (CommitComment) o;
		return this.repo.equals(that.repo) && this.json.equals(that.json);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + repo.hashCode();
		result = prime * result + json.hashCode();
		return result;
	}
}
