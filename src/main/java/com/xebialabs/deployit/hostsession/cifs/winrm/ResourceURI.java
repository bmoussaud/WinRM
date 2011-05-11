/*
 * This file is part of Maven Deployit plugin.
 *
 * WinRM Java implementation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WinRM Java implementation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Maven Deployit plugin.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.xebialabs.deployit.hostsession.cifs.winrm;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;

public enum ResourceURI {

	RESOURCE_URI_CMD("http://schemas.microsoft.com/wbem/wsman/1/windows/shell/cmd");

	private final String uri;

	ResourceURI(String uri) {
		this.uri = uri;
	}

	public Element getElement() {
		return DocumentHelper.createElement(QName.get("ResourceURI", WinRMURI.NS_WSMAN_DMTF)).addAttribute("mustUnderstand", "true").addText(uri);
	}
}
