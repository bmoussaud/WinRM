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

import org.dom4j.Namespace;

/**
 */
public class WinRMURI {

	public static final Namespace NS_SOAP_ENV = Namespace.get("env", "http://www.w3.org/2003/05/soap-envelope");
	public static final Namespace NS_ADDRESSING = Namespace.get("a", "http://schemas.xmlsoap.org/ws/2004/08/addressing");
	public static final Namespace NS_CIMBINDING = Namespace.get("b", "http://schemas.dmtf.org/wbem/wsman/1/cimbinding.xsd");
	public static final Namespace NS_ENUM = Namespace.get("n", "http://schemas.xmlsoap.org/ws/2004/09/enumeration");
	public static final Namespace NS_TRANSFER = Namespace.get("x", "http://schemas.xmlsoap.org/ws/2004/09/transfer");
	public static final Namespace NS_WSMAN_DMTF = Namespace.get("w", "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd");
	public static final Namespace NS_WSMAN_MSFT = Namespace.get("p", "http://schemas.microsoft.com/wbem/wsman/1/wsman.xsd");
	public static final Namespace NS_SCHEMA_INST = Namespace.get("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	public static final Namespace NS_WIN_SHELL = Namespace.get("rsp", "http://schemas.microsoft.com/wbem/wsman/1/windows/shell");
	public static final Namespace NS_WSMAN_FAULT = Namespace.get("f", "http://schemas.microsoft.com/wbem/wsman/1/wsmanfault");

}
