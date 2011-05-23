package com.xebialabs.winrm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Return always YES
 */
public class LazyHostnameVerifier implements HostnameVerifier {
	@Override
	public boolean verify(String s, SSLSession sslSession) {
		logger.debug("verify {} on session  {}", s, sslSession);
		return true;

	}
	private static Logger logger = LoggerFactory.getLogger(LazyHostnameVerifier.class);
}
