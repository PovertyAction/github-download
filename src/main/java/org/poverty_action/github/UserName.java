package org.poverty_action.github;

import java.io.IOException;

import com.jcabi.github.User;
import com.jcabi.github.Users;

class UserName {
	private final String login, name;

	UserName(String login, String name) {
		this.login = login;
		this.name  = name;
	}

	public String login() { return login; }
	public String name() { return name; }

	public boolean hasLogin() {
		return login != null;
	}

	public boolean hasName() {
		return name != null;
	}

	public String nameOrLogin() {
		return hasName() ? name : login;
	}
}

class UserNames {
	private UserNames() { }

	public static UserName get(User.Smart user) throws IOException {
		return new UserName(user.login(), user.hasName() ? user.name() : null);
	}

	public static UserName get(User user) throws IOException {
		return get(new User.Smart(user));
	}

	public static UserName get(String nameOrLogin, Users users)
		throws IOException {
		if (nameOrLogin == null || users == null)
			throw new NullPointerException();

		if (nameOrLogin.indexOf(' ') >= 0)
			return new UserName(null, nameOrLogin);
		else {
			boolean found = true;
			User.Smart user = new User.Smart(users.get(nameOrLogin));
			try {
				user.json();
			}
			// This is thrown if the user is not found on GitHub.
			catch (AssertionError e) {
				found = false;
			}
			return found ? get(user) : new UserName(null, nameOrLogin);
		}
	}
}
