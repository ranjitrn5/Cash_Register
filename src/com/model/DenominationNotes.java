package com.model;

import java.util.Comparator;

import com.exception.InsufficientCashBalanceException;
import com.exception.InvalidOperationException;

public class DenominationNotes {
	
	private int amount;
	private int quantity;
	private int denominationTotalValue;
	
	public DenominationNotes(int amount){
		this.amount = amount;
		this.quantity = 0;
		this.denominationTotalValue = 0;
	}
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
     * Adds denomination quantity for particular denomination
     * @param  quantity
     * @throws InvalidOperationException
     */
	public void addDenominationQuantity(int quantity)throws InvalidOperationException{
		if(quantity < 0){
			throw new InvalidOperationException();
		}
		this.quantity+=quantity;
		setDenominationTotalValue(this.quantity, this.amount);
	}
	
	/**
     * Removes denomination quantity from particular denomination
     * @param  quantity
     * @throws InvalidOperationException, InsufficientCashBalanceException
     */
	public void removeDenominationQuantity(int quantity) throws InvalidOperationException, InsufficientCashBalanceException{
		if(quantity < 0){
			throw new InvalidOperationException();
		}
		if(this.quantity < quantity){
			throw new InsufficientCashBalanceException();
			
		}
		this.quantity-=quantity;
		setDenominationTotalValue(this.quantity, this.amount);
	}
	
	public void setDenominationTotalValue(int quantity, int amount){
		denominationTotalValue = amount * quantity;
	}
	
	public int getDenominationTotalValue(){
		return denominationTotalValue;
	}
}

 class NotesComparator implements Comparator<DenominationNotes>{

	@Override
	public int compare(DenominationNotes o1, DenominationNotes o2) {
		// TODO Auto-generated method stub
		return (o1.getAmount() < o2.getAmount())? -1 : (o1.getAmount() > o2.getAmount()) ? 1: 0;
	}
	
}
