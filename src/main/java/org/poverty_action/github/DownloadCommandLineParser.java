package org.poverty_action.github;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.poverty_action.github.GitHubDownload.Target;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;
import com.jcabi.http.wire.RetryWire;

class DownloadCommandLineParser {
	private final Repo repo;
	private final Path to;
	private final Set<Target> targets;

	private static final Options options;
	@SuppressWarnings("static-access")
	private static Options options() {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("repo").
			withDescription("full repository name, e.g., PovertyAction/github-download").
			hasArg().withArgName("name").create('r'));
		options.addOption(OptionBuilder.
			withDescription("file that contains OAuth token").
			hasArg().withArgName("filename").create("token"));
		options.addOption(OptionBuilder.
			withDescription("directory in which to save downloads").
			hasArg().withArgName("directory").create("to"));
		options.addOption(new Option("issues", "download issues"));
		options.addOption(new Option("cc", "download commit comments"));
		options.addOption(new Option("help", "display this help and exit"));
		return options;
	}
	static {
		options = options();
	}

	private static void directToHelp() {
		System.out.println("use -help for list of options");
	}

	public DownloadCommandLineParser(String args[]) throws IOException {
		CommandLine cl = null;
		try {
			cl = new GnuParser().parse(options, args);
		}
		catch (ParseException e) {
			e.printStackTrace();
			System.out.println("invalid syntax");
			directToHelp();
			System.exit(1);
		}

		// -help
		if (cl.hasOption("help")) {
			new HelpFormatter().printHelp("GitHubDownload", options);
			System.exit(0);
		}

		// Check required options.
		for (String opt : new String[] {"repo", "token", "to"})
			if (!cl.hasOption(opt)) {
				System.out.printf("-%s required%n", opt);
				directToHelp();
				System.exit(1);
			}

		if (!(cl.hasOption("issues") || cl.hasOption("cc"))) {
			System.out.println("specify -issues or -cc");
			directToHelp();
			System.exit(1);
		}

		// -repo
		String token = new OAuthToken(cl.getOptionValue("token")).token();
		Github gh = new RtGithub(new RtGithub(token).entry().
			through(RetryWire.class));
		Coordinates coords = new Coordinates.Simple(cl.getOptionValue("repo"));
		repo = gh.repos().get(coords);

		// -to
		to = Paths.get(cl.getOptionValue("to"));

		// -targets
		targets = new HashSet<>(2);
		if (cl.hasOption("issues"))
			targets.add(Target.ISSUES);
		if (cl.hasOption("cc"))
			targets.add(Target.COMMIT_COMMENTS);
	}

	public Repo repo() { return repo; }
	public Path to() { return to; }
	public SortedSet<Target> targets() { return new TreeSet<>(targets); }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DownloadCommandLineParser))
			return false;
		DownloadCommandLineParser that = (DownloadCommandLineParser) o;
		return this.repo.equals(that.repo) && this.to.equals(that.to) &&
			this.targets.equals(that.targets);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + repo.hashCode();
		result = prime * result + to.hashCode();
		result = prime * result + targets.hashCode();
		return result;
	}
}
