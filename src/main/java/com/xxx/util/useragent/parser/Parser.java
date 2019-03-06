package com.xxx.util.useragent.parser;

import com.xxx.util.useragent.bean.UserAgent;
import com.xxx.util.useragent.bean.Device;
import com.xxx.util.useragent.bean.OS;
import com.xxx.util.useragent.bean.Browser;
import com.xxx.util.useragent.helper.Constants;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Parser {

	public static Parser build() {
		return new Parser();
	}

	Parser() {

	}

	private BrowserParser browserParser = BrowserParser.PARSER;
	private OSParser osParser = OSParser.PARSER;
	private DeviceParser deviceParser = DeviceParser.PARSER;

	static {
		initialize(Parser.class.getResourceAsStream(Constants.REGEX_YAML_PATH));
	}

	public UserAgent parse(String agentString) {
		Browser browser = parseBrowser(agentString);
		OS os = parseOS(agentString);
		Device device = parseDevice(agentString);
		return new UserAgent(browser, os, device);
	}

	public Browser parseBrowser(String agentString) {
		return browserParser.parse(agentString);
	}

	public Device parseDevice(String agentString) {
		return deviceParser.parse(agentString);
	}

	public OS parseOS(String agentString) {
		return osParser.parse(agentString);
	}

	private static void initialize(InputStream regexYaml) {
		Yaml yaml = new Yaml(new SafeConstructor());
		Map<String, List<Map<String, String>>> regexConfig = yaml.load(regexYaml);

		List<Map<String, String>> uaParserConfigs = regexConfig.get("user_agent_parsers");
		if (uaParserConfigs == null) {
			throw new IllegalArgumentException("user_agent_parsers is missing from yaml");
		}
		BrowserParser.fromList(uaParserConfigs);

		List<Map<String, String>> osParserConfigs = regexConfig.get("os_parsers");
		if (osParserConfigs == null) {
			throw new IllegalArgumentException("os_parsers is missing from yaml");
		}
		OSParser.fromList(osParserConfigs);

		List<Map<String, String>> deviceParserConfigs = regexConfig.get("device_parsers");
		if (deviceParserConfigs == null) {
			throw new IllegalArgumentException("device_parsers is missing from yaml");
		}
		DeviceParser.fromList(deviceParserConfigs);
	}
}
