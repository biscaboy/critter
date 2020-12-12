package com.udacity.jdnd.course3.critter.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer not found")
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}

