package com.hellochange.tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.exception.InsufficientCashBalanceException;
import com.exception.InvalidOperationException;
import com.model.DenominationNotes;

public class DenominationTest {
	
	DenominationNotes note = new DenominationNotes(10);

	@Test
	public void testAddQuantity() throws Exception{
		note.addDenominationQuantity(5);
        Assert.assertEquals(5,note.getQuantity());
	}
	
	@Test(expected=InvalidOperationException.class)
	public void testAddQuantityWithException() throws Exception{
		note.addDenominationQuantity(-5);
        Assert.fail("Invalid quantity added.");
	}
	
	@Test
	public void testRemoveQuantity() throws Exception{
		note.addDenominationQuantity(5);
		note.removeDenominationQuantity(5);
        Assert.assertEquals(0,note.getQuantity());
	}
	
	@Test(expected=InsufficientCashBalanceException.class)
	public void testRemoveQuantityWithException() throws Exception{
		note.addDenominationQuantity(0);
		note.removeDenominationQuantity(5);
        Assert.fail("Insufficient Cash Denomination balance, Cannot remove");
	}

}
