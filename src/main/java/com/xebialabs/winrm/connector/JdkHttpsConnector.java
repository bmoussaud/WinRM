package com.xebialabs.winrm.connector;

import com.xebialabs.winrm.WinRMHost;

/**
 * Default HTTPS connector
 * Need to import server certificates in the client's trust store.
 * http://blog.ippon.fr/2008/10/20/certificats-auto-signe-et-communication-ssl-en-java/
 */
public class JdkHttpsConnector extends  JdkHttpConnector {

	public JdkHttpsConnector(WinRMHost host) {
		super(host);
	}
}
