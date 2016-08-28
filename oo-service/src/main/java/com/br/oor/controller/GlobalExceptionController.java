package com.br.oor.controller;

import com.br.oor.exception.ValidationException;
import com.br.oor.util.MessageError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by maiconoliveira on 05/12/15.
 */
@ControllerAdvice
public class GlobalExceptionController {

    //TODO criar logs

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> handlerRegisterExpeption(ValidationException validationException){
        return new ResponseEntity<MessageError>(new MessageError(validationException.getField(), validationException.getMessage()), validationException.getHttpStatus());
    }
}
