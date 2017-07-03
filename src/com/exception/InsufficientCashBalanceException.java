package com.exception;

public class InsufficientCashBalanceException extends Exception {
	
	public InsufficientCashBalanceException(){
		super("Insufficient cash register balance. Cannot dispense");
	}

}
