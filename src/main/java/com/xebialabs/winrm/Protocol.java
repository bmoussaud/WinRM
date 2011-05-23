package com.xebialabs.winrm;

/**
 * Define the protocol used to connect the remote WinRM server.
 * HTTPS_LAZY is a dummy implementation of SSL, without certificats checking and hostname verifiers. For test only.
 */
public enum Protocol {

	HTTP("http"), HTTPS("https"), HTTPS_LAZY("https");

	private final String protocol;

	Protocol(String protocol) {
		this.protocol = protocol;
	}

	public String get() {
		return protocol;
	}
}
