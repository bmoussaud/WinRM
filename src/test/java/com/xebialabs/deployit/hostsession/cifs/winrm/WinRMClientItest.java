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


import com.xebialabs.deployit.hostsession.cifs.winrm.exception.BlankValueRuntimeException;
import com.xebialabs.deployit.hostsession.cifs.winrm.exception.InvalidFilePathRuntimeException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.Assert.*;

public class WinRMClientItest {

	private static Logger logger = LoggerFactory.getLogger(WinRMClientItest.class);

	static final String DEFAULT_SERVER = "WIN-2MGY3RY6XSH.deployit.local";
	static final int DEFAULT_PORT = 5985;
	static final String DEFAULT_USERNAME = "hilversum";
	static final String DEFAULT_PASSWORD = "Xe%%bia";

	private String server;
	private int port;
	private String username;
	private String password;

	@Before
	public void setup() {
		server = DEFAULT_SERVER;
		port = DEFAULT_PORT;
		username = DEFAULT_USERNAME;
		password = DEFAULT_PASSWORD;

		System.setProperty("java.security.krb5.conf", "src/main/resources/krb5.conf");
		System.setProperty("java.security.auth.login.config", "src/main/resources/login.conf");
		System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");

	}

	@After
	public void tearDown() {
		System.setProperty("java.security.krb5.conf", "");
		System.setProperty("java.security.auth.login.config", "");
		System.setProperty("javax.security.auth.useSubjectCredsOnly", "");

	}

	@Test
	public void testWinRMClient() {

		WinRMClient client = new WinRMClient(server, port, username, password);
		client.runCmd("ipconfig");
		assertEquals(0, client.getExitCode());
		assertEquals(1, client.getChunk());
		assertTrue(client.getStdout().toString().contains("172.16.74.129"));
	}


	@Test(expected = BlankValueRuntimeException.class)
	public void testWinRMClientMisConfigurationBlankForLogin() {

		tearDown();

		WinRMClient client = new WinRMClient(server, port, username, password);
		client.runCmd("ipconfig");
	}

	@Test(expected = InvalidFilePathRuntimeException.class)
	public void testWinRMClientMisConfigurationWrongPathForLogin() {
		tearDown();

		System.setProperty("java.security.auth.login.config", "/not/exist/file.conf");

		WinRMClient client = new WinRMClient(server, port, username, password);
		client.runCmd("ipconfig");
	}

	@Test(expected = BlankValueRuntimeException.class)
	public void testWinRMClientMisConfigurationBlankForKerberos() {
		tearDown();
		System.setProperty("java.security.auth.login.config", "src/main/resources/login.conf");

		System.setProperty("java.security.krb5.conf", "");

		WinRMClient client = new WinRMClient(server, port, username, password);
		client.runCmd("ipconfig");
	}

	@Test(expected = InvalidFilePathRuntimeException.class)
	public void testWinRMClientMisConfigurationWrongPathForKerberos() {
		tearDown();
		System.setProperty("java.security.auth.login.config", "src/main/resources/login.conf");

		System.setProperty("java.security.krb5.conf", "/path/to/nowhere/file.conf");

		WinRMClient client = new WinRMClient(server, port, username, password);
		client.runCmd("ipconfig");
	}


	//only http & basic auth is supported
	//@Test(expected = RuntimeException.class)
	public void testWinRMClientSecure() {
		try {
			WinRMClient client = new WinRMClient(server, 443, username, password);
			client.runCmd("ipconfig");
			assertFalse(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWinRMClientWrongCommandLine() {
		WinRMClient client = new WinRMClient(server, port, username, password);
		client.runCmd("ifconfig");
		assertEquals(1, client.getExitCode());
		assertEquals(1, client.getChunk());
		assertTrue(client.getStderr().toString().contains("'ifconfig' is not recognized as an internal or external command"));

	}

	@Test
	public void testWinRMClientVerboseDir() {
		WinRMClient client = new WinRMClient(server, port, username, password);
		client.runCmd("dir", "/s ");
		assertEquals(0, client.getExitCode());
		assertTrue(client.getChunk() > 1);
		System.out.println(client.getStdout());
	}


}
