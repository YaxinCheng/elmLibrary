package com.example.library.backend;

public class FormatCheckFailedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FormatCheckFailedException() {
		super();
	}
	public FormatCheckFailedException(String msg) {
		super(msg);
	}
	public FormatCheckFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public FormatCheckFailedException(Throwable cause) {
		super(cause);
	}
}
