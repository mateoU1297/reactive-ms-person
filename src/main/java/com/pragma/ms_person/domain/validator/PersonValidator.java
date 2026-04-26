package com.pragma.ms_person.domain.validator;

import com.pragma.ms_person.domain.exception.InvalidFieldException;
import com.pragma.ms_person.domain.model.Person;
import reactor.core.publisher.Mono;

public class PersonValidator {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_EMAIL_LENGTH = 100;

    private PersonValidator() {}

    public static Mono<Person> validate(Person person) {
        return validateName(person.getName())
                .then(validateEmail(person.getEmail()))
                .thenReturn(person);
    }

    private static Mono<Boolean> validateName(String name) {
        if (name == null || name.isBlank())
            return Mono.error(new InvalidFieldException("Name is required"));
        if (name.length() > MAX_NAME_LENGTH)
            return Mono.error(new InvalidFieldException(
                    "Name must not exceed " + MAX_NAME_LENGTH + " characters"));
        return Mono.just(true);
    }

    private static Mono<Boolean> validateEmail(String email) {
        if (email == null || email.isBlank())
            return Mono.error(new InvalidFieldException("Email is required"));
        if (email.length() > MAX_EMAIL_LENGTH)
            return Mono.error(new InvalidFieldException("Email must not exceed " + MAX_EMAIL_LENGTH + " characters"));
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            return Mono.error(new InvalidFieldException("Email format is invalid"));
        return Mono.just(true);
    }
}