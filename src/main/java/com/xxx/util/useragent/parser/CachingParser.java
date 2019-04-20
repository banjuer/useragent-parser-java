package com.xxx.util.useragent.parser;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.xxx.util.useragent.bean.UserAgent;
import com.xxx.util.useragent.bean.Device;
import com.xxx.util.useragent.bean.OS;
import com.xxx.util.useragent.bean.Browser;
import org.apache.commons.collections4.map.LRUMap;

public class CachingParser extends Parser {

	private static final int DEFAULT_CACHE_SIZE = 100;

	private int cacheSize;
	private Map<String, Browser> cacheBrowser;
	private ReentrantReadWriteLock browserLock = new ReentrantReadWriteLock();
	private Map<String, Device> cacheDevice;
	private ReentrantReadWriteLock deviceLock = new ReentrantReadWriteLock();
	private Map<String, OS> cacheOS;
	private ReentrantReadWriteLock osLock = new ReentrantReadWriteLock();
	private Map<String, UserAgent> cacheUserAgent;
	private ReentrantReadWriteLock userLock = new ReentrantReadWriteLock();

	private void init() {
		cacheBrowser = new LRUMap<>(cacheSize);
		cacheDevice = new LRUMap<>(cacheSize);
		cacheOS = new LRUMap<>(cacheSize);
		cacheUserAgent = new LRUMap<>(cacheSize);
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

		UserAgent userAgent = getWithLock(cacheUserAgent, agentString, userLock);
		if (userAgent != null)
			return userAgent;
		userAgent = new UserAgent(this.parseBrowser(agentString), this.parseOS(agentString), this.parseDevice(agentString));
		putWithLock(cacheUserAgent, agentString, userAgent, userLock);
		return userAgent;
	}

	@Override
	public Browser parseBrowser(String agentString) {
		if (agentString == null) {
			return null;
		}
		Browser browser = getWithLock(cacheBrowser, agentString, browserLock);
		if (browser != null) {
			return browser;
		}
		browser = super.parseBrowser(agentString);
		putWithLock(cacheBrowser, agentString, browser, browserLock);
		return browser;
	}

	@Override
	public Device parseDevice(String agentString) {
		if (agentString == null) {
			return null;
		}
		Device device = getWithLock(cacheDevice, agentString, deviceLock);
		if (device != null) {
			return device;
		}
		device = super.parseDevice(agentString);
		putWithLock(cacheDevice, agentString, device, deviceLock);
		return device;
	}

	@Override
	public OS parseOS(String agentString) {
		if (agentString == null) {
			return null;
		}
		OS os = getWithLock(cacheOS, agentString, osLock);
		if (os != null) {
			return os;
		}
		os = super.parseOS(agentString);
		putWithLock(cacheOS, agentString, os, osLock);
		return os;
	}

	private <E> E getWithLock(Map<String, E> map, String key, ReentrantReadWriteLock lock) {
		lock.readLock().lock();
		E e;
		try {
			e = map.get(key);
		} finally {
			lock.readLock().unlock();
		}
		return e;
	}

	private <E> void putWithLock(Map<String, E> map, String key, E v, ReentrantReadWriteLock lock) {
		lock.writeLock().lock();
		try {
			map.put(key, v);
		} finally {
			lock.writeLock().unlock();
		}
	}

}
