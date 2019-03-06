package com.xxx.util.useragent.parser;

import java.util.Collections;
import java.util.Map;

import com.xxx.util.useragent.bean.UserAgent;
import com.xxx.util.useragent.bean.Device;
import com.xxx.util.useragent.bean.OS;
import com.xxx.util.useragent.bean.Browser;
import org.apache.commons.collections4.map.LRUMap;

public class CachingParser extends Parser {

	private static final int DEFAULT_CACHE_SIZE = 100;

	private int cacheSize;
	private Map<String, Browser> cacheBrowser;
	private Map<String, Device> cacheDevice;
	private Map<String, OS> cacheOS;
	private Map<String, UserAgent> cacheUserAgent;

	private void init() {
		cacheBrowser = Collections.synchronizedMap(new LRUMap<>(cacheSize));
		cacheDevice = Collections.synchronizedMap(new LRUMap<>(cacheSize));
		cacheOS = Collections.synchronizedMap(new LRUMap<>(cacheSize));
		cacheUserAgent = Collections.synchronizedMap(new LRUMap<>(cacheSize));
	}

	public CachingParser(int cacheSize){
		super();
		this.cacheSize = cacheSize;
		init();
	}

	public CachingParser(){
		this(DEFAULT_CACHE_SIZE);
	}

	@Override
	public UserAgent parse(String agentString) {
		if (agentString == null) {
			return null;
		}
		UserAgent userAgent = cacheUserAgent.get(agentString);
		if (userAgent != null)
			return userAgent;
		userAgent = new UserAgent(this.parseBrowser(agentString), this.parseOS(agentString), this.parseDevice(agentString));
		cacheUserAgent.put(agentString, userAgent);
		return userAgent;
	}

	@Override
	public Browser parseBrowser(String agentString) {
		if (agentString == null) {
			return null;
		}
		Browser browser = cacheBrowser.get(agentString);
		if (browser != null) {
			return browser;
		}
		browser = super.parseBrowser(agentString);
		cacheBrowser.put(agentString, browser);
		return browser;
	}

	@Override
	public Device parseDevice(String agentString) {
		if (agentString == null) {
			return null;
		}
		Device device = cacheDevice.get(agentString);
		if (device != null) {
			return device;
		}
		device = super.parseDevice(agentString);
		cacheDevice.put(agentString, device);
		return device;
	}

	@Override
	public OS parseOS(String agentString) {
		if (agentString == null) {
			return null;
		}
		OS os = cacheOS.get(agentString);
		if (os != null) {
			return os;
		}
		os = super.parseOS(agentString);
		cacheOS.put(agentString, os);
		return os;
	}

}
