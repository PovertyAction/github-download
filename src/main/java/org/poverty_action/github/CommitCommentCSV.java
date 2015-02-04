package org.poverty_action.github;

import java.io.File;
import java.io.IOException;

public class CommitCommentCSV extends
	DownloadCSV<CommitCommentSubject, CommitComment> {
	public CommitCommentCSV(File file) throws IOException {
		super(file);
	}

	private final SubjectVisitor printVisitor = new SubjectVisitor() {
		private void printSha(CommitCommentSubject subject) throws IOException {
			printer().printRecord("Commit: " + subject.commit().sha());
		}

		@Override
		public void visit(CommitAsSubject subject) {
			try {
				printSha(subject);
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void visit(CommitLine subject) {
			try {
				printSha(subject);
				printer().printRecord("Path: " + subject.path());
				printer().printRecord("Line number: " + subject.line());
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	};

	@Override
	public void printHeader(CommitCommentSubject subject) throws IOException {
		subject.accept(printVisitor);
		printer().printRecord("Author", "Time", "Body", "URL");
	}

	@Override
	public void printSubrecord(CommitComment comment) throws IOException {
		printer().printRecord(UserNames.get(comment.author()).nameOrLogin(),
			CSVDateFormat.get().format(comment.createdAt()),
			new CSVString(comment.body()), comment.htmlUrl());
	}
}
