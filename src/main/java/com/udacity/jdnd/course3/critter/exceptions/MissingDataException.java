package com.udacity.jdnd.course3.critter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Required information was missing from the request.")
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MissingDataException extends RuntimeException {

    public MissingDataException() {
    }

    public MissingDataException(String message) {
        super(message);
    }
}
