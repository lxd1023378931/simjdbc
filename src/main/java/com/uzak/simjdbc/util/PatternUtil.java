package com.uzak.simjdbc.util;

import java.util.regex.Pattern;

public class PatternUtil {
	public static final String DECIMAL = "^\\d+(.\\d+)*$";
	public static final String DIGITAL = "^\\d+$";

	public static boolean decide(String s, String pattern) {
		return Pattern.matches(pattern, s);
	}
}
