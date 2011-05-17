% WinRM Plugin Manual
%
% Mai, 2011

# Preface #

This manual describes the WinRM plugin.

# Introduction #

The Windows Remote Management (WinRM) is the Microsoft implementation of WS-Management Protocol, a standard Simple Object Access Protocol (SOAP)-based, firewall-friendly protocol that allows hardware and operating systems, from different vendors, to interoperate.
This plugin allows Deployit to become a WinRM client to run remote commands on the server.

# Plugin Requirements #

In addition to the requirements for Deployit, the Dictionary Plugin has the following additional requirements:

# Configuration #
1. Create (or edit) the JAAS configuration file. Add the following entry

	WinRMClient {
	  com.sun.security.auth.module.Krb5LoginModule required
		doNotPrompt=false
		useTicketCache=false
		debug=true;
	};

2. Create a file called krb5.conf or krb5.ini with the following content
	[libdefaults]
		default_realm = DEPLOYIT.LOCAL


	[realms]
		DEPLOYIT.LOCAL = {
			kdc = WIN-2MGY3RY6XSH.deployit.local
		}

	[domain_realm]
		.deployit.local = DEPLOYIT.LOCAL
		deployit.local = DEPLOYIT.LOCAL

	Replace the values of
	* the 'kdc' by your key domain controler name,
	* the '.deployit.local' and 'deployit.local' by your domain name

More information {http://support.microsoft.com/kb/555092}


3. Add the following property to the JVM -Djava.security.auth.login.config=/path/to/login.conf
4. Add the following property to the JVM -Djava.security.krb5.conf=/path/to/krb5.conf
5. Add the following property to the JVM -Djavax.security.auth.useSubjectCredsOnly=false


# Known Limitations #

* This implementation does not support encrypted communication between the client and the WinRM server. You need to switch it off by running this command
	winrm set winrm/config/service  @{AllowUnencrypted="true"}

* This implementaition has not been test using https

# TroubleShooting #
1. javax.security.auth.login.LoginException: KDC has no support for encryption type (14)
* See http://download.oracle.com/javase/6/docs/technotes/guides/security/jgss/tutorials/Troubleshooting.html
* Add the option 'Use Kerberos DES type for this account' to the account used to connect to the remote Windows.

# Thanks #
* ZendChild to provide me a ruby implementation and wise advices https://github.com/zenchild/WinRM
* JNA and http://code.google.com/p/jnaerator/ mvn clean com.jnaerator:maven-jnaerator-plugin:jnaerate
http://download.oracle.com/docs/cd/E19963-01/html/819-2145/gssclient-3.html
http://download.oracle.com/docs/cd/E19082-01/819-2145/overview-22/index.html