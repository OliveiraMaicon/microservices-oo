package com.br.oor.exception;

/**
 * Created by agomes on 17/01/16.
 */
public class AddressRequiredException extends RuntimeException {
    public AddressRequiredException(String message) {
        super(message);
    }
}
