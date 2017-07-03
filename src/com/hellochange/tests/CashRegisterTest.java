package com.hellochange.tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.exception.InsufficientCashBalanceException;
import com.exception.InvalidInputArgumentsException;
import com.model.CashRegister;


public class CashRegisterTest {
	CashRegister cashRegister = new CashRegister();

	@Test
    public void testAddCashNotes() throws Exception {
        cashRegister.addQuantity(1,2,3,4,5);
        Assert.assertEquals(68, cashRegister.getTotalBalanceValue());
    }
	
	@Test(expected=InvalidInputArgumentsException.class)
	public void testAddCashNotesWithInvalidArguments() throws Exception{
		cashRegister.addQuantity(-1);
		Assert.fail("Invalid Arguments. Cannot Process");
	}
	
	@Test
	public void testTakeCashNotesCommand() throws Exception{
		cashRegister.addQuantity(1,2,3,0,5);
		cashRegister.removeQuanity(1,2,0,0,5);
		Assert.assertEquals(15, cashRegister.getTotalBalanceValue());
	}
	
	@Test(expected=InsufficientCashBalanceException.class)
	public void testRemoveCashNotesWithInvalidAmount() throws Exception{
		cashRegister.addQuantity(1,2,3,4,5);
		cashRegister.removeQuanity(1,2,4,5,0);
		Assert.fail("Insufficient Quantity. Cannot dispense");
	}
	
	@Test
	public void testProcessChangeAmount() throws Exception{
		cashRegister.addQuantity(1,0,3,4,0);
		CashRegister result=cashRegister.processChange(11);
		Assert.assertEquals(11, result.getTotalBalanceValue());
	}
	
	@Test(expected = InsufficientCashBalanceException.class)
    public void testProcessChangeInvalidAmount() throws Exception {
		cashRegister.addQuantity(1, 2, 3, 4, 5);
		CashRegister result=cashRegister.processChange(69);
		Assert.fail("Insufficient Cash balance. Cannot dispense");
    }

}
