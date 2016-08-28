package com.br.oor.exception;

public class EventNotFoundException  extends RuntimeException{

	private static final long serialVersionUID = 9175343725821090274L;

	public EventNotFoundException(String message){
		super(message);
	}
}
