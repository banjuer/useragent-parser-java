package com.xxx.util.useragent.bean;

public class UserAgent {
	private final Browser browser;
	private final OS os;
	private final Device device;

	public UserAgent(Browser browser, OS os, Device device) {
		this.browser = browser;
		this.os = os;
		this.device = device;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof UserAgent))
			return false;

		UserAgent o = (UserAgent) other;
		return ((this.browser != null && this.browser.equals(o.browser)) || this.browser == o.browser)
				&& ((this.os != null && this.os.equals(o.os)) || this.os == o.os)
				&& ((this.device != null && this.device.equals(o.device)) || this.device == o.device);
	}

	@Override
	public int hashCode() {
		int h = browser == null ? 0 : browser.hashCode();
		h += os == null ? 0 : os.hashCode();
		h += device == null ? 0 : device.hashCode();
		return h;
	}

	@Override
	public String toString() {
		return String.format("{\"browser\": %s, \"os\": %s, \"device\": %s}", browser, os, device);
	}

	public Browser getBrowser() {
		return browser;
	}

	public OS getOs() {
		return os;
	}

	public Device getDevice() {
		return device;
	}
}