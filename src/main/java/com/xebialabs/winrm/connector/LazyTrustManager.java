package com.xebialabs.winrm.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 */
public class LazyTrustManager implements X509TrustManager {
	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		X509Certificate[] certs = {};
        return certs;
	}

	private static Logger logger = LoggerFactory.getLogger(LazyTrustManager.class);
}
