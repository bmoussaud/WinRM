package com.xebialabs.deployit.hostsession.cifs.winrm.exception;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;

public class WinRMRuntimeIOException extends RuntimeException {

	final Document in;
	final Document out;

	public WinRMRuntimeIOException(String message, Document in, Document out, Throwable cause) {
		super(message, cause);
		this.in = in;
		this.out = out;
	}

	public WinRMRuntimeIOException(String message) {
		this(message, null, null, null);

	}

	public WinRMRuntimeIOException(String message, Throwable throwable) {
		this(message, null, null, throwable);
	}

	@Override
	public String getMessage() {
		return String.format("%s, document in %s, document out %s,", super.getMessage(), toString(in), toString(out));
	}

	private String toString(Document doc) {
		if (doc == null) {
			return "[EMPTY]";
		}

		StringWriter stringWriter = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(stringWriter, OutputFormat.createPrettyPrint());
		try {
			xmlWriter.write(doc);
			xmlWriter.close();
		} catch (IOException e) {
			throw new RuntimeException("error ", e);
		}
		return stringWriter.toString();
	}

}
