package com.xebialabs.deployit.hostsession.cifs.winrm;

import org.dom4j.Document;


public interface HttpConnector {
	Document sendMessage(Document requestDocument, SoapAction soapAction);
}
