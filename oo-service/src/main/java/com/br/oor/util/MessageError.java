package com.br.oor.util;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by maiconoliveira on 30/11/15.
 */
public class MessageError implements Serializable {


    private static final long serialVersionUID = 2024056850367688139L;

    private String field;
    private String message;


    public MessageError( String field, String message){
        setField(field);
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
