package com.xxx.util.useragent.bean;

import com.xxx.util.useragent.helper.Constants;
import com.xxx.util.useragent.helper.Helper;

public class Browser {
	private final String family, major, minor, patch, version;

	public Browser(String family, String major, String minor, String patch) {
		this.family = family;
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.version = Constants.EMPTY_STRING;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof Browser))
			return false;

		Browser o = (Browser) other;
		return ((this.family != null && this.family.equals(o.family)) || this.family == o.family)
				&& ((this.major != null && this.major.equals(o.major)) || this.major == o.major)
				&& ((this.minor != null && this.minor.equals(o.minor)) || this.minor == o.minor)
				&& ((this.patch != null && this.patch.equals(o.patch)) || this.patch == o.patch);
	}

	@Override
	public int hashCode() {
		int h = family == null ? 0 : family.hashCode();
		h += major == null ? 0 : major.hashCode();
		h += minor == null ? 0 : minor.hashCode();
		h += patch == null ? 0 : patch.hashCode();
		return h;
	}

	@Override
	public String toString() {
		return String.format("{\"family\": %s, \"major\": %s, \"version\": %s}",
				family == null ? Constants.EMPTY_STRING : '"' + family + '"',
				major == null ? Constants.EMPTY_STRING : '"' + major + '"',
				Helper.isEmpty(getVersion() )? Constants.EMPTY_STRING : '"' + getVersion() + '"' );
	}

	public String getFamily() {
		return family;
	}

	public String getMajor() {
		return major;
	}

	public String getVersion() {
		return Helper.version(major, minor, patch);
	}
}