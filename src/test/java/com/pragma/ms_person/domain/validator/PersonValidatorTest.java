package com.pragma.ms_person.domain.validator;

import com.pragma.ms_person.domain.exception.InvalidFieldException;
import com.pragma.ms_person.domain.model.Person;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class PersonValidatorTest {

    @Test
    void validate_validPerson_success() {
        Person person = new Person(null, "John Doe", "john@example.com");
        StepVerifier.create(PersonValidator.validate(person))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void validate_nullName_throwsInvalidField() {
        Person person = new Person(null, null, "john@example.com");
        StepVerifier.create(PersonValidator.validate(person))
                .expectErrorMatches(e -> e instanceof InvalidFieldException &&
                        e.getMessage().equals("Name is required"))
                .verify();
    }

    @Test
    void validate_blankName_throwsInvalidField() {
        Person person = new Person(null, "   ", "john@example.com");
        StepVerifier.create(PersonValidator.validate(person))
                .expectErrorMatches(e -> e instanceof InvalidFieldException &&
                        e.getMessage().equals("Name is required"))
                .verify();
    }

    @Test
    void validate_nameTooLong_throwsInvalidField() {
        Person person = new Person(null, "A".repeat(101), "john@example.com");
        StepVerifier.create(PersonValidator.validate(person))
                .expectError(InvalidFieldException.class)
                .verify();
    }

    @Test
    void validate_nullEmail_throwsInvalidField() {
        Person person = new Person(null, "John", null);
        StepVerifier.create(PersonValidator.validate(person))
                .expectErrorMatches(e -> e instanceof InvalidFieldException &&
                        e.getMessage().equals("Email is required"))
                .verify();
    }

    @Test
    void validate_invalidEmailFormat_throwsInvalidField() {
        Person person = new Person(null, "John", "invalid-email");
        StepVerifier.create(PersonValidator.validate(person))
                .expectErrorMatches(e -> e instanceof InvalidFieldException &&
                        e.getMessage().equals("Email format is invalid"))
                .verify();
    }

    @Test
    void validate_validEmail_success() {
        Person person = new Person(null, "John", "john.doe@example.com");
        StepVerifier.create(PersonValidator.validate(person))
                .expectNextCount(1)
                .verifyComplete();
    }
}
