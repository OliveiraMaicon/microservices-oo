package com.br.oor.exception;

/**
 * Created by agomes on 17/01/16.
 */
public class AddressNotFoundException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5078682943297334919L;

	public AddressNotFoundException(String message){
        super(message);
    }
}
