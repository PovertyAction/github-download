package org.poverty_action.github;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

import com.jcabi.github.Comment;
import com.jcabi.github.Commit;
import com.jcabi.github.Issue;
import com.jcabi.github.Pull;

class IssueWriter implements Closeable {
	private final Path issuesDir;
	private final IssueCSV csv;

	public IssueWriter(Path dir) throws IOException {
		if (dir == null)
			throw new NullPointerException();
		issuesDir  = dir;

		Files.createDirectories(issuesDir);
		csv = new IssueCSV(issuesDir.resolve("All issues.csv").toFile());
	}

	// Write an issue's raw JSON to file, and add the issue to
	// the issues .csv file.
	public void write(Issue.Smart issue) throws IOException {
		Set<IssueSubrecord> recs = new TreeSet<>();

		// The issue itself
		Path issueDir = issuesDir.resolve(String.valueOf(issue.number()));
		new JsonFile(issueDir.resolve("issue.json"), issue).write();
		recs.add(new IssueSubrecord(issue));

		// Issue comments
		Path commentsDir = issueDir.resolve("comments");
		for (Comment comment : issue.comments().iterate()) {
			Comment.Smart smart = new Comment.Smart(comment);
			Path commentPath =
				commentsDir.resolve(String.valueOf(smart.number()) + ".json");
			new JsonFile(commentPath, smart).write();
			recs.add(new IssueSubrecord(smart));
		}

		// Pull request commits
		if (issue.isPull()) {
			Pull.Smart pull = new Pull.Smart(issue.pull());
			new JsonFile(issueDir.resolve("pr.json"), pull).write();

			for (Commit commit : pull.commits())
				recs.add(new IssueSubrecord(commit));
		}

		csv.printHeader(issue);
		for (IssueSubrecord rec : recs)
			csv.printSubrecord(rec);
		csv.printFooter();
	}

	@Override
	public void close() throws IOException {
		csv.close();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof IssueWriter))
			return false;
		return issuesDir.equals(((IssueWriter) o).issuesDir);
	}

	@Override
	public int hashCode() {
		return issuesDir.hashCode();
	}
}
