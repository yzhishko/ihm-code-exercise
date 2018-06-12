package com.code.api.advertise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AdvertiserAlreadyExistsException extends RuntimeException{

    public AdvertiserAlreadyExistsException(String exception) {
        super(exception);
    }

}
