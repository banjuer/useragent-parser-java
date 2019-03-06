package com.xxx.util.useragent.bean;

import com.xxx.util.useragent.helper.Constants;

public class Device {
	private final String name;

	public Device(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof Device))
			return false;

		Device o = (Device) other;
		return (this.name != null && this.name.equals(o.name)) || this.name == o.name;
	}

	@Override
	public int hashCode() {
		return name == null ? 0 : name.hashCode();
	}

	@Override
	public String toString() {
		return String.format("{\"name\": %s}", name == null ? Constants.EMPTY_STRING : '"' + name + '"');
	}

	public String getName() {
		return name;
	}
}