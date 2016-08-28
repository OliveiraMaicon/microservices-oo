package com.br.oor.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by maiconoliveira on 29/10/15.
 */
public class ValidationException extends Exception {

    private static final long serialVersionUID = 8624939272796440255L;


    private HttpStatus httpStatus;
    private String field;


    public ValidationException(HttpStatus httpStatus, String message){
        this(httpStatus, message, null);
    }


    public ValidationException(HttpStatus httpStatus, String message, String field){
        super(message);
        this.httpStatus = httpStatus;
        this.field = field;
    }
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
