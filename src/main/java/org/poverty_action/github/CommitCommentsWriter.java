package org.poverty_action.github;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class CommitCommentsWriter implements Closeable {
	private final Path commentsDir;
	private final CommitCommentCSV csv;

	public CommitCommentsWriter(Path dir) throws IOException {
		if (dir == null)
			throw new NullPointerException();
		commentsDir = dir;

		Files.createDirectories(commentsDir);
		csv = new CommitCommentCSV(commentsDir.
			resolve("All commit comments.csv").toFile());
	}

	public void write(SubjectComments comments) throws IOException {
		Path jsonDir = commentsDir.resolve("json");
		csv.printHeader(comments.subject());
		for (CommitComment comment : comments) {
			new JsonFile(jsonDir.resolve(String.format("%d.json",
				comment.id())), comment).write();
			csv.printSubrecord(comment);
		}
		csv.printFooter();
	}

	@Override
	public void close() throws IOException {
		csv.close();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CommitCommentsWriter))
			return false;
		return commentsDir.equals(((CommitCommentsWriter) o).commentsDir);
	}

	@Override
	public int hashCode() {
		return commentsDir.hashCode();
	}
}
