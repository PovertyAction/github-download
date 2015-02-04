package org.poverty_action.github;

import java.io.File;
import java.io.IOException;

import com.jcabi.github.Issue;

class IssueCSV extends DownloadCSV<Issue.Smart, IssueSubrecord> {
	public IssueCSV(File file) throws IOException {
		super(file);
	}

	@Override
	public void printHeader(Issue.Smart issue) throws IOException {
		printer().printRecord(String.format("#%d %s%s (%s)", issue.number(),
			issue.isPull() ? "Pull request: " : "", issue.title(),
			issue.isOpen() ? "open" : "closed"));
		printer().printRecord("Author", "Time", "Body");
	}

	@Override
	public void printSubrecord(IssueSubrecord is) throws IOException {
		printer().printRecord(is.author(),
			CSVDateFormat.get().format(is.createdAt()),
			new CSVString(is.body()));
	}
}
