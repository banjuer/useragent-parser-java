package com.xxx.util.useragent.parser;

import com.xxx.util.useragent.bean.OS;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class OSParser {

	static final OSParser PARSER = new OSParser();

	private OSParser() {

	}

	private static final List<OSPattern> OS_PATTERNS = new LinkedList<>();

	static void fromList(List<Map<String, String>> configList) {
		for (Map<String, String> configMap : configList) {
			OS_PATTERNS.add(OSParser.patternFromMap(configMap));
		}
	}

	OS parse(String agentString) {
		if (agentString == null) {
			return null;
		}

		OS os;
		for (OSPattern p : OS_PATTERNS) {
			if ((os = p.match(agentString)) != null) {
				return os;
			}
		}
		return new OS("Other", null, null, null, null);
	}

	private static OSPattern patternFromMap(Map<String, String> configMap) {
		String regex = configMap.get("regex");
		if (regex == null) {
			throw new IllegalArgumentException("OS is missing regex");
		}

		return (new OSPattern(Pattern.compile(regex), configMap.get("os_replacement"),
				configMap.get("os_v1_replacement"), configMap.get("os_v2_replacement"),
				configMap.get("os_v3_replacement")));
	}

	protected static class OSPattern {
		private final Pattern pattern;
		private final String osReplacement, v1Replacement, v2Replacement, v3Replacement;

		OSPattern(Pattern pattern, String osReplacement, String v1Replacement, String v2Replacement,
				  String v3Replacement) {
			this.pattern = pattern;
			this.osReplacement = osReplacement;
			this.v1Replacement = v1Replacement;
			this.v2Replacement = v2Replacement;
			this.v3Replacement = v3Replacement;
		}

		OS match(String agentString) {
			String family = null, v1 = null, v2 = null, v3 = null, v4 = null;
			Matcher matcher = pattern.matcher(agentString);

			if (!matcher.find()) {
				return null;
			}

			int groupCount = matcher.groupCount();

			if (osReplacement != null) {
				if (groupCount >= 1) {
					family = Pattern.compile("(" + Pattern.quote("$1") + ")").matcher(osReplacement)
							.replaceAll(matcher.group(1));
				} else {
					family = osReplacement;
				}
			} else if (groupCount >= 1) {
				family = matcher.group(1);
			}

			if (v1Replacement != null) {
				v1 = getReplacement(matcher, v1Replacement);
			} else if (groupCount >= 2) {
				v1 = matcher.group(2);
			}
			if (v2Replacement != null) {
				v2 = getReplacement(matcher, v2Replacement);
			} else if (groupCount >= 3) {
				v2 = matcher.group(3);
			}
			if (v3Replacement != null) {
				v3 = getReplacement(matcher, v3Replacement);
			} else if (groupCount >= 4) {
				v3 = matcher.group(4);
			}
			if (groupCount >= 5) {
				v4 = matcher.group(5);
			}

			return family == null ? null : new OS(family, v1, v2, v3, v4);
		}

		private String getReplacement(Matcher matcher, String replacement) {
			if (isBackReference(replacement)) {
				int group = getGroup(replacement);
				return matcher.group(group);
			} else {
				return replacement;
			}
		}

		private boolean isBackReference(String replacement) {
			return replacement.startsWith("$");
		}

		private int getGroup(String backReference) {
			return Integer.valueOf(backReference.substring(1));
		}
	}
}
