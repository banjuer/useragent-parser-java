package com.xxx.util.useragent.parser;

import com.xxx.util.useragent.bean.Browser;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BrowserParser {

	static final BrowserParser PARSER = new BrowserParser();

	private BrowserParser() {

	}

	private static final List<BrowserPattern> UA_PATTERNS = new LinkedList<>();

	static void fromList(List<Map<String, String>> configList) {
		for (Map<String, String> configMap : configList) {
			UA_PATTERNS.add(BrowserParser.patternFromMap(configMap));
		}
	}

	Browser parse(String agentString) {
		if (agentString == null) {
			return null;
		}

		Browser agent;
		for (BrowserPattern p : UA_PATTERNS) {
			if ((agent = p.match(agentString)) != null) {
				return agent;
			}
		}
		return new Browser("Other", null, null, null);
	}

	private static BrowserPattern patternFromMap(Map<String, String> configMap) {
		String regex = configMap.get("regex");
		if (regex == null) {
			throw new IllegalArgumentException("User agent is missing regex");
		}

		return (new BrowserPattern(Pattern.compile(regex), configMap.get("family_replacement"),
				configMap.get("v1_replacement"), configMap.get("v2_replacement")));
	}

	protected static class BrowserPattern {
		private final Pattern pattern;
		private final String familyReplacement, v1Replacement, v2Replacement;

		BrowserPattern(Pattern pattern, String familyReplacement, String v1Replacement, String v2Replacement) {
			this.pattern = pattern;
			this.familyReplacement = familyReplacement;
			this.v1Replacement = v1Replacement;
			this.v2Replacement = v2Replacement;
		}

		Browser match(String agentString) {
			String family = null, v1 = null, v2 = null, v3 = null;
			Matcher matcher = pattern.matcher(agentString);

			if (!matcher.find()) {
				return null;
			}

			int groupCount = matcher.groupCount();

			if (familyReplacement != null) {
				if (familyReplacement.contains("$1") && groupCount >= 1 && matcher.group(1) != null) {
					family = familyReplacement.replaceFirst("\\$1", Matcher.quoteReplacement(matcher.group(1)));
				} else {
					family = familyReplacement;
				}
			} else if (groupCount >= 1) {
				family = matcher.group(1);
			}

			if (v1Replacement != null) {
				v1 = v1Replacement;
			} else if (groupCount >= 2) {
				v1 = matcher.group(2);
			}

			if (v2Replacement != null) {
				v2 = v2Replacement;
			} else if (groupCount >= 3) {
				v2 = matcher.group(3);
				if (groupCount >= 4) {
					v3 = matcher.group(4);
				}
			}
			return family == null ? null : new Browser(family, v1, v2, v3);
		}
	}
}
