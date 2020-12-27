package com.udacity.jdnd.course3.critter.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,
                reason = "Employee not avaliable for requested activity/time combination.")
public class EmployeeNotAvaliableException extends RuntimeException {

    public EmployeeNotAvaliableException() {
    }

    public EmployeeNotAvaliableException(String message) {
        super(message);
    }
}
