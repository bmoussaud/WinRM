package com.xebialabs.winrm;

import com.xebialabs.winrm.connector.JdkHttpConnector;
import com.xebialabs.winrm.connector.JdkHttpsConnector;
import com.xebialabs.winrm.connector.JdkLazyHttpsConnector;

/**
 */
public class HttpConnectorFactory {

	public static HttpConnector newHttpConnector(WinRMHost host) {
		switch (host.getProtocol()) {
			case HTTP:
				return new JdkHttpConnector(host);
			case HTTPS:
				return new JdkHttpsConnector(host);
			case HTTPS_LAZY:
				return new JdkLazyHttpsConnector(host);
		}
		throw new IllegalArgumentException("Unsupported protocol " + host.getProtocol());
	}
}
