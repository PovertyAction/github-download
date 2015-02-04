package org.poverty_action.github;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

class CSVDateFormat {
	private static final DateFormat format =
		new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private CSVDateFormat() { }

	public static DateFormat get() { return format; }

}
