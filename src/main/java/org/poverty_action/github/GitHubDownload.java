package org.poverty_action.github;

import java.io.IOException;
import java.nio.file.Path;

import com.jcabi.github.Issue;
import com.jcabi.github.Repo;

public class GitHubDownload {
	private final Repo repo;
	private final Path dir;

	public enum Target {
		ISSUES {
			@Override
			void download(GitHubDownload dl) throws IOException {
				try (IssueWriter writer =
					new IssueWriter(dl.dir.resolve("issues"))) {
					System.out.print("Writing issues...");
					for (Issue.Smart issue : new SmartIssues(dl.repo)) {
						System.out.print(" " + issue.number());
						writer.write(issue);
					}
					System.out.println();
				}
			}
		},

		COMMIT_COMMENTS {
			@Override
			void download(GitHubDownload dl) throws IOException {
				try (CommitCommentsWriter writer =
					new CommitCommentsWriter(dl.dir.
					resolve("commit_comments"))) {
					System.out.println("Writing commit comments...");
					RepoSubjectComments repoComments =
						new RepoSubjectComments(dl.repo);
					for (SubjectComments comments : repoComments) {
						System.out.printf("    %s%n", comments.subject());
						writer.write(comments);
					}
					System.out.printf("    (%d notes total)%n",
						repoComments.size());
				}
			}
		};

		abstract void download(GitHubDownload dl) throws IOException;
	}

	public GitHubDownload(Repo repo, Path dir) {
		this.repo = repo;
		this.dir  = dir;
	}

	public void download(Target target) throws IOException {
		target.download(this);
	}

	public static void main(String args[]) throws IOException {
		DownloadCommandLineParser cl = new DownloadCommandLineParser(args);
		GitHubDownload dl = new GitHubDownload(cl.repo(), cl.to());
		for (Target t : cl.targets())
			dl.download(t);
		System.out.println("Done.");
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GitHubDownload))
			return false;
		GitHubDownload that = (GitHubDownload) o;
		return this.repo.equals(that.repo) && this.dir.equals(that.dir);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + repo.hashCode();
		result = prime * result + dir.hashCode();
		return result;
	}
}
