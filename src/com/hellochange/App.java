package com.hellochange;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.exception.InsufficientCashBalanceException;
import com.exception.InvalidAmountException;
import com.exception.InvalidInputArgumentsException;
import com.exception.InvalidOperationException;
import com.model.CashRegister;
import com.util.ChangeConstants;


public class App {
	
	private App(){
		super();
	}
	
	public static int[] createInput(String... arguments){
		List<Integer> input = new ArrayList<Integer>();
		
		for(int i=1;i<arguments.length;i++){
			input.add(Integer.parseInt(arguments[i]));
		}
		
		int[] quantity = input.stream().filter(i -> i != null).mapToInt(i -> i).toArray();
		return quantity;
	}
	

	public static void main(String[] args) {
		CashRegister register = new CashRegister();
		Scanner sc = new Scanner(System.in);
		int[] quantity;
		String command = "";
		do{
			System.out.println("Enter your command");
			String inputArgument = sc.nextLine();
			String[] arguments = inputArgument.split(" ");
			command = arguments[0].toLowerCase();
			quantity = createInput(arguments);
			switch(command){
			case "put":
				try{
					register.addQuantity(quantity);
					register.displayCurrentState(register);
				} catch(InvalidInputArgumentsException e){
					System.out.println((ChangeConstants.INVALID_INPUTS));
				} catch (InvalidOperationException e) {
					System.out.println(ChangeConstants.INVALID_OPERATION);
				}
				
				break;
			case "take":
				try {
					register.removeQuanity(quantity);
					register.displayCurrentState(register);
				} catch (InsufficientCashBalanceException e) {
					System.out.println(ChangeConstants.INSUFFICIENT_BALANCE);
				} catch(InvalidInputArgumentsException invalid){
					System.out.println(ChangeConstants.INVALID_INPUTS);
				} catch (InvalidOperationException e) {
					System.out.println(ChangeConstants.INVALID_OPERATION);
				}
				break;
			case "change":
				try {
					CashRegister result = register.processChange(quantity);
					register.displayCurrentState(result);
				} catch (InsufficientCashBalanceException e) {
					System.out.println(ChangeConstants.INSUFFICIENT_BALANCE);
				} catch (InvalidInputArgumentsException e) {
					System.out.println(ChangeConstants.INVALID_INPUTS);
				} catch (InvalidAmountException e) {
					System.out.println(ChangeConstants.INVALID_AMOUNT);
				} catch (InvalidOperationException e) {
					System.out.println(ChangeConstants.INVALID_OPERATION);
				} 
				break;
			case "show":
				register.displayCurrentState(register);
				break;
			case "quit":
				System.out.println(ChangeConstants.QUIT);
				break;
			default:
				System.out.println(ChangeConstants.INVALID_COMMAND);
				break;
			}
		}while(!command.equalsIgnoreCase("quit"));
		sc.close();
	}

}
