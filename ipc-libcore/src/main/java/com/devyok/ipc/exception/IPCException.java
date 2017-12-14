package com.devyok.ipc.exception;


public class IPCException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IPCException() {
		super();
	}

	public IPCException(String message, Throwable cause) {
		super(message, cause);
	}

	public IPCException(Throwable cause) {
		super(cause);
	}

	public IPCException(String message) {
		super(message);
	}

	
}
