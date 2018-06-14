package com.code.api.advertise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotEnoughCreditException extends RuntimeException {

    public NotEnoughCreditException(String exception) {
        super(exception);
    }

}
