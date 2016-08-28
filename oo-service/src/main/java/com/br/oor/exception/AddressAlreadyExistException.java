package com.br.oor.exception;

public class AddressAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1168564646158734140L;
	
	public AddressAlreadyExistException(String message){
		super(message);
	}

}
