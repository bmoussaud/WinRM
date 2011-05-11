package com.xebialabs.deployit.hostsession.cifs.winrm;

import com.xebialabs.deployit.hostsession.cifs.winrm.exception.BlankValueRuntimeException;
import com.xebialabs.deployit.hostsession.cifs.winrm.exception.InvalidFilePathRuntimeException;
import com.xebialabs.deployit.hostsession.cifs.winrm.exception.WinRMRuntimeIOException;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 */
public class JdkHttpConnector implements HttpConnector {

	private final URL targetURL;

	private final TokenGenerator tokenGenerator;

	enum AuthenticationMode {
		BASIC, KERBEROS

	};

	private AuthenticationMode authenticationMode = AuthenticationMode.KERBEROS;


	public JdkHttpConnector(String host, int port, String username, String password) {
		targetURL = getURL(host, port);
		tokenGenerator = new KerberosTokenGenerator(host, username, password);
	}


	private URL getURL(String host, int port) {
		try {
			//Only http is supported....
			return new URL("http", host, port, "/wsman");
		} catch (MalformedURLException e) {
			throw new WinRMRuntimeIOException("Cannot build a new URL using host " + host + " and port " + port, e);
		}
	}

	@Override
	public Document sendMessage(Document requestDocument, SoapAction soapAction) {
		Document responseDocument = null;
		try {
			final URLConnection urlConnection = targetURL.openConnection();
			HttpURLConnection con = (HttpURLConnection) urlConnection;
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
			final String authToken = tokenGenerator.generateToken();
			if (authToken != null)
				con.addRequestProperty("Authorization", authToken);

			if (soapAction != null) {
				con.setRequestProperty("SOAPAction", soapAction.getValue());
			}

			final String requestDocAsString = toString(requestDocument);
			logger.debug("send message:request {}", requestDocAsString);
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(
							con.getOutputStream()));
			bw.write(requestDocAsString, 0, requestDocAsString.length());
			bw.flush();
			bw.close();

			InputStream is = urlConnection.getInputStream();
			Writer writer = new StringWriter();
			try {
				int n;
				char[] buffer = new char[1024];
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				IOUtils.closeQuietly(is);
			}

			if (logger.isDebugEnabled()) {
				for (int i = 0; i < con.getHeaderFields().size(); i++) {
					logger.debug("Header {} --> {}", con.getHeaderFieldKey(i), con.getHeaderField(i));
				}
			}

			final String text = writer.toString();
			logger.debug("send message:response {}", text);
			responseDocument = DocumentHelper.parseText(text);
			return responseDocument;

		}
		catch (BlankValueRuntimeException bvrte) {
			throw bvrte;
		}
		catch (InvalidFilePathRuntimeException ifprte ) {
			throw ifprte;
		}
		catch (Exception e) {
			throw new WinRMRuntimeIOException("send message on " + targetURL + " error ", requestDocument, responseDocument, e);
		}
	}


	private String toString(Document doc) {
		StringWriter stringWriter = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(stringWriter, OutputFormat.createPrettyPrint());
		try {
			xmlWriter.write(doc);
			xmlWriter.close();
		} catch (IOException e) {
			throw new WinRMRuntimeIOException("error ", e);
		}
		return stringWriter.toString();
	}

	private static Logger logger = LoggerFactory.getLogger(JdkHttpConnector.class);
}