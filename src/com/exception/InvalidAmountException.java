package com.exception;

public class InvalidAmountException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAmountException() {
		super("Amount Specified Invalid. No change can be made");
	}

}
