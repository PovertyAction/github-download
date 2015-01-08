package org.poverty_action.github;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.jcabi.github.Comment;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Issue;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;
import com.jcabi.github.User;
import com.jcabi.http.wire.RetryWire;

class IssuesCSV {
	private final CSVPrinter printer;
	private static final DateFormat dateFormat =
		new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	public IssuesCSV(String filename) throws IOException {
		printer = new CSVPrinter(new PrintWriter(filename), CSVFormat.DEFAULT);
	}

	public void printHeader(Issue.Smart issue) throws IOException {
		printer.printRecord(String.format("#%d %s (%s)",
			issue.number(), issue.title(), issue.isOpen() ? "open" : "closed"));
		printer.printRecord("Author", "Time", "Body");
	}

	// Values of a comment to save to .csv
	private class CommentValues {
		private final User author;
		private final Date createdAt;
		private final String body;

		public CommentValues(User author, Date createdAt, String body) {
			this.author    = author;
			this.createdAt = createdAt;
			this.body      = body;
		}

		public CommentValues(Comment.Smart comment) throws IOException {
			this(comment.author(), comment.createdAt(), comment.body());
		}

		public User author() { return author; }
		public Date createdAt() { return createdAt; }
		public String body() { return body; }
	}

	private void printComment(CommentValues comment) throws IOException {
		printer.printRecord(comment.author().login(),
			dateFormat.format(comment.createdAt()), comment.body());
	}

	public void add(Issue.Smart issue) throws IOException {
		printHeader(issue);
		printComment(new CommentValues(issue.author(), issue.createdAt(),
			issue.body()));
		for (Comment comment : issue.comments().iterate())
			printComment(new CommentValues(new Comment.Smart(comment)));
		printer.println();
	}

	public void close() throws IOException {
		printer.close();
	}
}

public class IssuesToCSV {
	private static String getOAuthKey(String filename)
		throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader(filename));
		String key = in.next();
		in.close();
		return key;
	}

	public static void main(String args[])
		throws FileNotFoundException, IOException {
		if (args.length != 3)
			throw new IllegalArgumentException("incorrect number of arguments");
		final String oAuthKey = getOAuthKey(args[0]);
		final String repoName = args[1];
		final String csvName  = args[2];

		// Get the repository.
		Github gh = new RtGithub(new RtGithub(oAuthKey).entry().
			through(RetryWire.class));
		Repo repo = gh.repos().get(new Coordinates.Simple(repoName));

		// Initialize the .csv.
		IssuesCSV csv = new IssuesCSV(csvName);

		// Get the repo's issues.
		SortedSet<Issue.Smart> issues =
			new TreeSet<>(new Comparator<Issue.Smart>() {
				@Override
				public int compare(Issue.Smart issue1, Issue.Smart issue2) {
					return issue1.number() - issue2.number();
				}
			});
		Map<String, String> params = new HashMap<>();
		params.put("filter", "all");
		params.put("state",  "all");
		// This parameter doesn't seem to be working, hence the TreeSet.
		params.put("direction", "asc");
		for (Issue issue : repo.issues().iterate(params))
			issues.add(new Issue.Smart(issue));

		// Add the issues' comments to the .csv.
		System.out.print("Adding issues to .csv...");
		for (Issue.Smart issue : issues) {
			System.out.print(" " + issue.number());
			csv.add(issue);
		}
		System.out.println();

		csv.close();

		System.out.println("Done.");
	}
}
