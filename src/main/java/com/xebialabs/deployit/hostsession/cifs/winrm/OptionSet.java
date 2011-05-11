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

import com.google.common.collect.Lists;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;

import java.util.List;

import static com.xebialabs.deployit.hostsession.cifs.winrm.WinRMURI.NS_WSMAN_DMTF;

/**
 */
public enum OptionSet {

	OPEN_SHELL(Lists.newArrayList(new KeyValuePair("WINRS_NOPROFILE", "FALSE"), new KeyValuePair("WINRS_CODEPAGE", "437"))),
	RUN_COMMAND(Lists.newArrayList(new KeyValuePair("WINRS_CONSOLEMODE_STDIN", "TRUE")));

	private final List<KeyValuePair> keyValuePairs;


	OptionSet(List<KeyValuePair> keyValuePairs) {
		this.keyValuePairs = keyValuePairs;
	}

	public Element getElement() {
		final Element optionSet = DocumentHelper.createElement(QName.get("OptionSet", NS_WSMAN_DMTF));
		for (KeyValuePair p : keyValuePairs) {
			optionSet.addElement(QName.get("Option", NS_WSMAN_DMTF)).addAttribute("Name", p.getKey()).addText(p.getValue());
		}
		return optionSet;
	}
}
