/*
 * Copyright (c) 2008-2011 XebiaLabs B.V. All rights reserved.
 *
 * Your use of XebiaLabs Software and Documentation is subject to the Personal
 * License Agreement.
 *
 * http://www.xebialabs.com/deployit-personal-edition-license-agreement
 *
 * You are granted a personal license (i) to use the Software for your own
 * personal purposes which may be used in a production environment and/or (ii)
 * to use the Documentation to develop your own plugins to the Software.
 * "Documentation" means the how to's and instructions (instruction videos)
 * provided with the Software and/or available on the XebiaLabs website or other
 * websites as well as the provided API documentation, tutorial and access to
 * the source code of the XebiaLabs plugins. You agree not to (i) lease, rent
 * or sublicense the Software or Documentation to any third party, or otherwise
 * use it except as permitted in this agreement; (ii) reverse engineer,
 * decompile, disassemble, or otherwise attempt to determine source code or
 * protocols from the Software, and/or to (iii) copy the Software or
 * Documentation (which includes the source code of the XebiaLabs plugins). You
 * shall not create or attempt to create any derivative works from the Software
 * except and only to the extent permitted by law. You will preserve XebiaLabs'
 * copyright and legal notices on the Software and Documentation. XebiaLabs
 * retains all rights not expressly granted to You in the Personal License
 * Agreement.
 */

package com.xebialabs.deployit.hostsession.cifs.winrm;

import com.xebialabs.deployit.hostsession.cifs.winrm.exception.WinRMRuntimeIOException;
import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static com.xebialabs.deployit.hostsession.cifs.winrm.WinRMURI.*;

//import org.apache.commons.httpclient.Credentials;
//import org.apache.commons.httpclient.NTCredentials;


public class WinRMClient {

	public static final int DEFAULT_HTTP_PORT = 5985;
	public static final int DEFAULT_HTTPS_PORT = 5986;

	private static final String DEFAULT_TIMEOUT = "PT60.000S";
	private static final int DEFAULT_MAX_ENV_SIZE = 153600;
	private static final String DEFAULT_LOCALE = "en-US";

	//eg PT60.000S I don't know what is this format ...
	private String timeout = DEFAULT_TIMEOUT;
	//default 153600
	private long envelopSize = DEFAULT_MAX_ENV_SIZE;

	//default en-US
	private String locale = DEFAULT_LOCALE;

	//private final HttpClient httpclient;
	private final URL targetURL;

	private final StringBuffer stdout = new StringBuffer();
	private final StringBuffer stderr = new StringBuffer();

	private String exitCode;
	private String shellId;
	private String commandId;

	private int chunk = 0;

	private final HttpConnector connector;

	public WinRMClient(String host, int port, String username, String password) {
		connector = new JdkHttpConnector(host, port, username, password);
		targetURL = getURL(host, port);
	}

	private URL getURL(String host, int port) {
		try {
			//Only http is supported....
			return new URL("http", host, port, "/wsman");
		} catch (MalformedURLException e) {
			throw new WinRMRuntimeIOException("Cannot build a new URL using host " + host + " and port " + port, e);
		}
	}

	public void runCmd(String... commandLine) {
		StringBuffer cmd = new StringBuffer();
		for (String c : commandLine)
			cmd.append(c).append(" ");
		runCmd(cmd.toString().trim());
	}

	public void runCmd(String command) {
		try {
			shellId = openShell();
			commandId = runCommand(command);
			getCommandOutput();
		} finally {
			cleanUp();
			closeShell();
		}
	}


	public StringBuffer getStdout() {
		return stdout;
	}

	public StringBuffer getStderr() {
		return stderr;
	}

	private void closeShell() {
		if (shellId == null)
			return;
		logger.debug("closeShell shellId {}", shellId);
		final Document requestDocument = getRequestDocument(Action.WS_DELETE, ResourceURI.RESOURCE_URI_CMD, null, shellId, null);
		Document responseDocument = sendMessage(requestDocument, null);
	}

	private void cleanUp() {
		if (commandId == null)
			return;
		logger.debug("cleanUp shellId {} commandId {} ", shellId, commandId);
		final Element bodyContent = DocumentHelper.createElement(QName.get("Signal", NS_WIN_SHELL)).addAttribute("CommandId", commandId);
		bodyContent.addElement(QName.get("Code", NS_WIN_SHELL)).addText("http://schemas.microsoft.com/wbem/wsman/1/windows/shell/signal/terminate");
		final Document requestDocument = getRequestDocument(Action.WS_SIGNAL, ResourceURI.RESOURCE_URI_CMD, null, shellId, bodyContent);
		Document responseDocument = sendMessage(requestDocument, SoapAction.SIGNAL);

	}

	private void getCommandOutput() {
		logger.debug("getCommandOutput shellId {} commandId {} ", shellId, commandId);
		final Element bodyContent = DocumentHelper.createElement(QName.get("Receive", NS_WIN_SHELL));
		bodyContent.addElement(QName.get("DesiredStream", NS_WIN_SHELL)).addAttribute("CommandId", commandId).addText("stdout stderr");
		final Document requestDocument = getRequestDocument(Action.WS_RECEIVE, ResourceURI.RESOURCE_URI_CMD, null, shellId, bodyContent);

		for (; ;) {
			Document responseDocument = sendMessage(requestDocument, SoapAction.RECEIVE);
			stdout.append(handleStream(responseDocument, ResponseExtractor.STDOUT));
			stderr.append(handleStream(responseDocument, ResponseExtractor.STDERR));

			if (chunk == 0) {
				try {
					exitCode = getFirstElement(responseDocument, ResponseExtractor.EXIT_CODE);
					logger.info("exit code {}", exitCode);
				} catch (Exception e) {
					logger.debug("not found");
				}
			}
			chunk++;

			/* We may need to get additional output if the stream has not finished.
			The CommandState will change from Running to Done like so:
			@example

			 from...
			 <rsp:CommandState CommandId="..." State="http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Running"/>
			 to...
			 <rsp:CommandState CommandId="..." State="http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done">
				 <rsp:ExitCode>0</rsp:ExitCode>
			 </rsp:CommandState>
		 */
			final List list = ResponseExtractor.STREAM_DONE.getXPath().selectNodes(responseDocument);
			if (!list.isEmpty()) {
				exitCode = getFirstElement(responseDocument, ResponseExtractor.EXIT_CODE);
				logger.info("exit code {}", exitCode);
				break;
			}
		}

		logger.debug("all the command output has been fetched (chunk={})", chunk);


	}

	private StringBuffer handleStream(Document responseDocument, ResponseExtractor stream) {
		StringBuffer buffer = new StringBuffer();
		final List streams = stream.getXPath().selectNodes(responseDocument);
		if (!streams.isEmpty()) {
			final Base64 base64 = new Base64();
			Iterator<Element> itStreams = streams.iterator();
			while (itStreams.hasNext()) {
				Element e = itStreams.next();
				//TODO check performance with http://www.iharder.net/current/java/base64/
				final byte[] decode = base64.decode(e.getText());
				buffer.append(new String(decode));
			}
		}
		logger.debug("handleStream {} buffer {}", stream, buffer);
		return buffer;

	}


	private String runCommand(String command) {
		logger.debug("runCommand shellId {} command {}", shellId, command);
		final Element bodyContent = DocumentHelper.createElement(QName.get("CommandLine", NS_WIN_SHELL));

		String encoded = command;
		if (!command.startsWith("\""))
			encoded = "\"" + encoded;
		if (!command.endsWith("\""))
			encoded = encoded + "\"";


		logger.info("Encoded command is {}", encoded);

		bodyContent.addElement(QName.get("Command", NS_WIN_SHELL)).addText(encoded);

		final Document requestDocument = getRequestDocument(Action.WS_COMMAND, ResourceURI.RESOURCE_URI_CMD, OptionSet.RUN_COMMAND, shellId, bodyContent);
		Document responseDocument = sendMessage(requestDocument, SoapAction.COMMAND_LINE);

		return getFirstElement(responseDocument, ResponseExtractor.COMMAND_ID);
	}


	private String getFirstElement(Document doc, ResponseExtractor extractor) {
		final List nodes = extractor.getXPath().selectNodes(doc);
		if (nodes.isEmpty())
			throw new RuntimeException("Cannot find " + extractor.getXPath() + " in " + toString(doc));

		final Element next = (Element) nodes.iterator().next();
		return next.getText();
	}

	private String openShell() {
		logger.debug("openShell");

		final Element bodyContent = DocumentHelper.createElement(QName.get("Shell", NS_WIN_SHELL));
		bodyContent.addElement(QName.get("InputStreams", NS_WIN_SHELL)).addText("stdin");
		bodyContent.addElement(QName.get("OutputStreams", NS_WIN_SHELL)).addText("stdout stderr");


		final Document requestDocument = getRequestDocument(Action.WS_ACTION, ResourceURI.RESOURCE_URI_CMD, OptionSet.OPEN_SHELL, null, bodyContent);
		Document responseDocument = sendMessage(requestDocument, SoapAction.SHELL);

		return getFirstElement(responseDocument, ResponseExtractor.SHELL_ID);

	}

	private Document sendMessage(Document requestDocument, SoapAction soapAction) {
		return connector.sendMessage(requestDocument, soapAction);
	}

	private Document getRequestDocument(Action action, ResourceURI resourceURI, OptionSet optionSet, String shelId, Element bodyContent) {
		Document doc = DocumentHelper.createDocument();
		final Element envelope = doc.addElement(QName.get("Envelope", NS_SOAP_ENV));
		envelope.add(getHeader(action, resourceURI, optionSet, shelId));

		final Element body = envelope.addElement(QName.get("Body", NS_SOAP_ENV));

		if (bodyContent != null)
			body.add(bodyContent);

		return doc;
	}


	private Element getHeader(Action action, ResourceURI resourceURI, OptionSet optionSet, String shellId) {
		final Element header = DocumentHelper.createElement(QName.get("Header", NS_SOAP_ENV));
		header.addElement(QName.get("To", NS_ADDRESSING)).addText(targetURL.toString());
		final Element replyTo = header.addElement(QName.get("ReplyTo", NS_ADDRESSING));
		replyTo.addElement(QName.get("Address", NS_ADDRESSING)).addAttribute("mustUnderstand", "true").addText("http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous");
		header.addElement(QName.get("MaxEnvelopeSize", NS_WSMAN_DMTF)).addAttribute("mustUnderstand", "true").addText("" + envelopSize);
		header.addElement(QName.get("MessageID", NS_ADDRESSING)).addText(getUUID());
		header.addElement(QName.get("Locale", NS_WSMAN_DMTF)).addAttribute("mustUnderstand", "false").addAttribute("xml:lang", locale);
		header.addElement(QName.get("DataLocale", NS_WSMAN_MSFT)).addAttribute("mustUnderstand", "false").addAttribute("xml:lang", locale);
		header.addElement(QName.get("OperationTimeout", NS_WSMAN_DMTF)).addText(timeout);
		header.add(action.getElement());
		if (shellId != null) {
			header.addElement(QName.get("SelectorSet", NS_WSMAN_DMTF)).addElement(QName.get("Selector", NS_WSMAN_DMTF)).addAttribute("Name", "ShellId").addText(shellId);
		}
		header.add(resourceURI.getElement());
		if (optionSet != null) {
			header.add(optionSet.getElement());
		}

		return header;
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

	private String getUUID() {
		return "uuid:" + java.util.UUID.randomUUID().toString().toUpperCase();
	}

	public int getExitCode() {
		return Integer.parseInt(exitCode);
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public long getEnvelopSize() {
		return envelopSize;
	}

	public void setEnvelopSize(long envelopSize) {
		this.envelopSize = envelopSize;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public int getChunk() {
		return chunk;
	}

	private static Logger logger = LoggerFactory.getLogger(WinRMClient.class);


}
