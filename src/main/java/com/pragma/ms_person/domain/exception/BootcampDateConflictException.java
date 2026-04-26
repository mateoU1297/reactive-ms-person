package com.pragma.ms_person.domain.exception;

public class BootcampDateConflictException extends RuntimeException {
    public BootcampDateConflictException(Long bootcampId) {
        super("Bootcamp " + bootcampId + " overlaps with an existing enrollment");
    }
}
