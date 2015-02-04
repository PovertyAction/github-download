package org.poverty_action.github;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.json.JsonObject;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Repo;
import com.jcabi.github.RtPagination;

// The commit comments of a repository
class CommitComments implements Iterable<CommitComment> {
	private final Set<CommitComment> comments;

	public CommitComments(Repo repo) throws IOException {
		comments = new HashSet<>();
		Coordinates coords = repo.coordinates();
		Iterable<JsonObject> jsonComments =
			new RtPagination<>(repo.github().entry().uri()
				.path("/repos")
				.path(coords.user())
				.path(coords.repo())
				.path("comments")
			.back(), RtPagination.COPYING);
		for (JsonObject comment : jsonComments)
			comments.add(new CommitComment(repo, comment));
	}

	@Override
	public Iterator<CommitComment> iterator() {
		return new NoRemoveIterator<>(comments.iterator());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CommitComments))
			return false;
		return comments.equals(((CommitComments) o).comments);
	}

	@Override
	public int hashCode() {
		return comments.hashCode();
	}
}
