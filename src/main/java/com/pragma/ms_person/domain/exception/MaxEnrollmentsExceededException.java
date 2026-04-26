package com.pragma.ms_person.domain.exception;

public class MaxEnrollmentsExceededException extends RuntimeException {
    public MaxEnrollmentsExceededException() {
        super("Person cannot be enrolled in more than 5 bootcamps at the same time");
    }
}
