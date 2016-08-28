package com.br.oor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by maiconoliveira on 21/12/15.
 */
@Component
public abstract class MessageUtil {

    private static final String CUSTOM_FIELD_ERROR_FORMAT = "%s.%s.%s";

    public final static String LOGIN_ALREADY = "Login já existe.";
    public final static String INVALID_EMAIL = "Digite um email válido";
    public static final String ID_REQUIRED = "Id é obrigatório";
    public static final String USER_NOT_FOUND = "Usuário não existe.";
    public static final String OBJECT_NEED_UPDATE = "Objeto está com versão antiga";
    public static final String EMAIL_REQUIRED = "Email é obrigatório";
    public static final String CITY_REQUIRED = "Cidade é obrigatória";
    public static final String ADDRESS_REQUIRED = "Endereço é obrigatório";
    public static final String LATITUDE_REQUIRED = "Latitude é obrigatória";
    public static final String LONGITUDE_REQUIRED = "Longitude é obrigatória";

    private static MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        MessageUtil.messageSource = messageSource;
    }


    public static String getMessage(String code) {
        try {
            return messageSource.getMessage(code, null, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            return null;
        }
    }

    public static String getMessage(String code, Object[] args) {
        try {
            return messageSource.getMessage(code, args, Locale.getDefault());
        } catch (NoSuchMessageException e) {
            return null;
        }
    }


    public static List<MessageError> generateMessageErrorList(List<FieldError> fieldErrorList) {
        List<MessageError> msgErrorList = new ArrayList<MessageError>();

        if (fieldErrorList != null && fieldErrorList.size() > 0) {
            for (FieldError fieldError : fieldErrorList) {
                String customMessage = MessageUtil.getMessage(String.format(CUSTOM_FIELD_ERROR_FORMAT,
                        fieldError.getCode(), fieldError.getObjectName(), fieldError.getField()), fieldError
                        .getArguments());

                String fieldErrorName = fieldError.getField().replaceAll("\\.", "_");

                if (customMessage != null) {
                    msgErrorList.add(new MessageError(fieldErrorName, customMessage));
                } else {
                    msgErrorList.add(new MessageError(fieldErrorName, fieldError.getDefaultMessage()));
                }
            }
        }

        return msgErrorList;
    }


}
