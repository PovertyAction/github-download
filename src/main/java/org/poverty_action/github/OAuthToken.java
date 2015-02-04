package org.poverty_action.github;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

class OAuthToken {
	private final String token;

	// Imports OAuth token from file.
	public OAuthToken(String filename) throws IOException {
		if (filename == null)
			throw new NullPointerException();
		try (FileReader fr = new FileReader(filename);
			Scanner sc = new Scanner(fr)) {
			token = sc.next();
		}
	}

	public String token() { return token; }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof OAuthToken))
			return false;
		return token.equals(((OAuthToken) o).token);
	}

	@Override
	public int hashCode() {
		return token.hashCode();
	}
}
