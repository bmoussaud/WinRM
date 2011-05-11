package com.xebialabs.deployit.hostsession.cifs.winrm;

public class KeyValuePair {
	final String key;
	final String value;

	public KeyValuePair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
}
