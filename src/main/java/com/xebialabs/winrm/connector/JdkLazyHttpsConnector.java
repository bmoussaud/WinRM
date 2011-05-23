package com.xebialabs.winrm.connector;

import com.xebialabs.winrm.WinRMHost;

import javax.net.ssl.HttpsURLConnection;

/**
 * Lazy SSL Connection ....
 */
public class JdkLazyHttpsConnector extends JdkHttpConnector {

	static {
		HttpsURLConnection.setDefaultHostnameVerifier(new LazyHostnameVerifier());
		HttpsURLConnection.setDefaultSSLSocketFactory(new LazySSLSocketFactory());
	}

	public JdkLazyHttpsConnector(WinRMHost host) {
		super(host);
	}
}
