package org.poverty_action.github;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

class GitHubDateFormat {
	private static final DateFormat format =
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

	private GitHubDateFormat() { }

	public static DateFormat get() { return format; }
}
