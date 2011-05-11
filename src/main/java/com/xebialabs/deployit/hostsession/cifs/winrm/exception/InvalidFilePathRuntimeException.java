package com.xebialabs.deployit.hostsession.cifs.winrm.exception;

public class InvalidFilePathRuntimeException extends RuntimeException {
	public InvalidFilePathRuntimeException(String key, String file) {
		super(String.format("the file %s set by the %s property does not exist", file, key));
	}
}
