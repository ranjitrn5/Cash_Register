package com.exception;

public class InvalidOperationException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOperationException() {
		super("Invalid Quantity Entered to perform remove operation");
	}

}
