package org.poverty_action.github;

// String value of a .csv file with inserted characters so that
// the value appears correctly in Excel
class CSVString {
	private final String s;

	public CSVString(String s) {
		if (s == null)
			throw new NullPointerException();

		if (s.equals(""))
			this.s = s;
		else {
			// Excel doesn't like strings that start with '@'.
			String space = s.length() > 0 && s.charAt(0) == '@' ? " " : "";
			StringBuilder sb = new StringBuilder(space);
			sb.append(s);

			this.s = sb.toString();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CSVString))
			return false;
		return s.equals(((CSVString) o).s);
	}

	@Override
	public int hashCode() {
		return s.hashCode();
	}

	@Override
	public String toString() {
		return s;
	}
}
