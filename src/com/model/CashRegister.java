package com.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.exception.InsufficientCashBalanceException;
import com.exception.InvalidAmountException;
import com.exception.InvalidInputArgumentsException;
import com.exception.InvalidOperationException;

public class CashRegister {
	
	List<DenominationNotes> changeList = Arrays.asList(
			new DenominationNotes(20),
			new DenominationNotes(10),
			new DenominationNotes(5),
			new DenominationNotes(2),
			new DenominationNotes(1)
			);
	
	public int totalBalanceValue = 0;
	
	
	public List<DenominationNotes> getChangeList() {
		return changeList;
	}

	public void setChangeList(List<DenominationNotes> changeList) {
		this.changeList = changeList;
	}

	public int getTotalBalanceValue() {
		return totalBalanceValue;
	}

	public void setTotalBalanceValue(int totalBalanceValue) {
		this.totalBalanceValue = totalBalanceValue;
	}
	
	/**
     * Add new cash notes sequence to the cash register
     * @param quantity
     * @throws InvalidOPerationException, InvalidArgumentException
     */
	public void addQuantity(int... quantity) throws InvalidOperationException, InvalidInputArgumentsException{
		if (!validateInputArguments(quantity)){
			throw new InvalidInputArgumentsException();
		}
		int i = 0;
		for(DenominationNotes note : changeList){
			note.addDenominationQuantity(quantity[i++]);
		}
		totalBalanceValue=updateTotalBalance(changeList);
	}
	/**
     * Removes cash notes from the cash register
     * @param quantity
     * @throws InvalidOperationException, InvalidInputArgumentsException, InsufficientCashBalanceException
     */
	public void removeQuanity(int... quantity) throws InvalidOperationException, InsufficientCashBalanceException, InvalidInputArgumentsException{
		if (!validateInputArguments(quantity)){
			throw new InvalidInputArgumentsException();
		}
		if(!hasSufficientQuantity(quantity)){
			throw new InsufficientCashBalanceException();
		}
		int i = 0;
		for(DenominationNotes deno: changeList){
			deno.removeDenominationQuantity(quantity[i++]);
		}
		totalBalanceValue=updateTotalBalance(changeList);
	}
	
	private boolean hasSufficientQuantity(int[] quantity) {
		int totalValue = 0;
		for(int i=0;i<quantity.length;i++){
			if(quantity[i] > changeList.get(i).getQuantity()){
				return false;
			}
		}
		
		return true;
	}

	/**
     * Process for money change based on current balance in cash register
     * @param changeAmount
     * @throws InvalidOperationException, InvalidAmountException, InsufficientCashBalanceException InvalidInputArgumentsException
     * @return cashback
     */
	public CashRegister processChange(int... changeAmount)throws InvalidOperationException, InvalidAmountException, InsufficientCashBalanceException, InvalidInputArgumentsException{
		if(changeAmount.length != 1){
			throw new InvalidInputArgumentsException();
		}
		if(!isAmountValid(changeAmount[0])){
			throw new InvalidAmountException();
		}
		
		int leftOverChange = changeAmount[0];
		List<DenominationNotes> changeNotesList = changeList;
		
		CashRegister cashback = new CashRegister();
		
		Collections.sort(cashback.getChangeList(), new NotesComparator());
		Collections.sort(changeNotesList, new NotesComparator());
		
		int minimumChangeNumber = getChange(changeNotesList,leftOverChange, cashback.getChangeList());
		cashback.setTotalBalanceValue(updateTotalBalance(cashback.getChangeList()));
		totalBalanceValue=updateTotalBalance(changeNotesList);
		
		return cashback;

	}
	
	/**
     * Gets minimum change notes based on the amount of change requested and also updates the cash register with latest balance
     * @param  change, amount, cashBackNotesList
     * @throws InvalidOperationException, InsufficientCashBalanceException
     */
	private int getChange(List<DenominationNotes> change, int amount, List<DenominationNotes> cashBackNotesList) throws InvalidOperationException, InsufficientCashBalanceException{
		
		int[] T = new int[amount+1];
		int[] R = new int[amount+1];
		T[0]=0;
		for(int i=1;i<=amount;i++){
			T[i] = Integer.MAX_VALUE-1;
			R[i] = -1;
		}
		for(int j = 0;j<change.size();j++){
			for(int i = 1;i<=amount;i++){
				if(i >= change.get(j).getAmount() && change.get(j).getQuantity() != 0){
					if(T[i-change.get(j).getAmount()]+1 < T[i]){
						T[i] = T[i-change.get(j).getAmount()]+1;
						R[i] = j;
					}
				} 
			}
		}
		int start = R.length-1;
		if(R[start] < 0){
			Collections.reverse(change);
			throw new InsufficientCashBalanceException();
		}
		
		Map<Integer, Integer> requiredChangeMap = new HashMap<Integer, Integer>();
		int count = 0;
		while(start != 0){
			int j = R[start];
			if(requiredChangeMap.containsKey(j)){
				count++;
			}
			else{
				count = 1;
			}
			requiredChangeMap.put(j, count);
			start = start - change.get(j).getAmount();
		}
		
		if(!isQuantitySufficient(requiredChangeMap, change)){
			Collections.reverse(change);
			throw new InsufficientCashBalanceException();
		}
		int index, value;
		for(Entry<Integer, Integer> requiredChangeEntry : requiredChangeMap.entrySet()){
			index = requiredChangeEntry.getKey();
			value = requiredChangeEntry.getValue();
			change.get(index).removeDenominationQuantity(value);
			cashBackNotesList.get(index).addDenominationQuantity(value);
		}

		Collections.reverse(change);
		Collections.reverse(cashBackNotesList);
		
		return T[amount];
	}
	
	/**
     * Displays the current balance of cash register with quantity available for each denomination
     */
	public void displayCurrentState(CashRegister cashRegister){
		StringBuilder sb = new StringBuilder();
		sb.append("$"+cashRegister.getTotalBalanceValue()+" ");
		for(DenominationNotes note : cashRegister.getChangeList()){
			sb.append(note.getQuantity()+" ");
		}
		System.out.println(sb.toString());
	}
	
	private boolean isQuantitySufficient(Map<Integer, Integer> requiredChangeMap, List<DenominationNotes> noteList){
		for(Entry<Integer, Integer> entry: requiredChangeMap.entrySet()){
			int requiredTotalValue = entry.getValue()*noteList.get(entry.getKey()).getAmount();
			if(requiredTotalValue > noteList.get(entry.getKey()).getDenominationTotalValue()){
				return false;
			}
		}
		return true;
	}
	
	private int updateTotalBalance(List<DenominationNotes> changeList){
		totalBalanceValue = 0;
		for(DenominationNotes note : changeList){
			totalBalanceValue+= note.getDenominationTotalValue();
		}
		return totalBalanceValue;
	}
	
	
	private boolean isAmountValid(int changeAmount){
		if(changeAmount < 0 || changeAmount > getTotalBalanceValue()||
				getTotalBalanceValue() <= 0){
			return false;
		}
		
		return true;
	}
	
	private boolean validateInputArguments(int[] quantity){
		if(quantity.length != changeList.size()){
			return false;
		}
		for(int i=0;i<quantity.length;i++){
			if(quantity[i] < 0){
				return false;
			}
		}
		return true;
	}

}
