package com.xxx.util.useragent.parser;

import com.xxx.util.useragent.bean.Device;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DeviceParser {
	static final DeviceParser PARSER = new DeviceParser();

	private DeviceParser() {

	}

	private  static  final List<DevicePattern> DEVICE_PATTERNS = new LinkedList<>();

	Device parse(String agentString) {
		if (agentString == null) {
			return null;
		}

		String device = null;
		for (DevicePattern p : DEVICE_PATTERNS) {
			if ((device = p.match(agentString)) != null) {
				break;
			}
		}
		if (device == null)
			device = "Other";

		return new Device(device);
	}

	static void fromList(List<Map<String, String>> configList) {
		for (Map<String, String> configMap : configList) {
			DEVICE_PATTERNS.add(DeviceParser.patternFromMap(configMap));
		}
	}

	private static DevicePattern patternFromMap(Map<String, String> configMap) {
		String regex = configMap.get("regex");
		if (regex == null) {
			throw new IllegalArgumentException("Device is missing regex");
		}
		Pattern pattern = "i".equals(configMap.get("regex_flag")) ? Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
				: Pattern.compile(regex);
		return new DevicePattern(pattern, configMap.get("device_replacement"));
	}

	protected static class DevicePattern {
		private static final Pattern SUBSTITUTIONS_PATTERN = Pattern.compile("\\$\\d");
		private final Pattern pattern;
		private final String deviceReplacement;

		DevicePattern(Pattern pattern, String deviceReplacement) {
			this.pattern = pattern;
			this.deviceReplacement = deviceReplacement;
		}

		String match(String agentString) {
			Matcher matcher = pattern.matcher(agentString);
			if (!matcher.find()) {
				return null;
			}
			String device = null;
			if (deviceReplacement != null) {
				if (deviceReplacement.contains("$")) {
					device = deviceReplacement;
					for (String substitution : getSubstitutions(deviceReplacement)) {
						int i = Integer.valueOf(substitution.substring(1));
						String replacement = matcher.groupCount() >= i && matcher.group(i) != null
								? Matcher.quoteReplacement(matcher.group(i))
								: "";
						device = device.replaceFirst("\\" + substitution, replacement);
					}
					device = device.trim();
				} else {
					device = deviceReplacement;
				}
			} else if (matcher.groupCount() >= 1) {
				device = matcher.group(1);
			}

			return device;
		}

		private List<String> getSubstitutions(String deviceReplacement) {
			Matcher matcher = SUBSTITUTIONS_PATTERN.matcher(deviceReplacement);
			List<String> substitutions = new LinkedList<>();
			while (matcher.find()) {
				substitutions.add(matcher.group());
			}
			return substitutions;
		}

	}

}