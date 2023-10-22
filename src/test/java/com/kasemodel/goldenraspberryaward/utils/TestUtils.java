package com.kasemodel.goldenraspberryaward.utils;

import java.util.regex.Pattern;

public class TestUtils {
	public static final String UUID_REGEX = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
	public static final Pattern UUID_REGEX_PATTERN = Pattern.compile(UUID_REGEX);
}
