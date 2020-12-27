package com.udacity.jdnd.course3.critter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Employee skills were missing from the request.")
public class MissingParameterException extends RuntimeException {

    public MissingParameterException() {
    }

    public MissingParameterException(String message) {
        super(message);
    }
}
