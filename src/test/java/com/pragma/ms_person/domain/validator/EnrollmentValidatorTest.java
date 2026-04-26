package com.pragma.ms_person.domain.validator;

import com.pragma.ms_person.domain.exception.AlreadyEnrolledException;
import com.pragma.ms_person.domain.exception.BootcampDateConflictException;
import com.pragma.ms_person.domain.exception.MaxEnrollmentsExceededException;
import com.pragma.ms_person.domain.model.Bootcamp;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

class EnrollmentValidatorTest {

    @Test
    void validateMaxEnrollments_zero_success() {
        StepVerifier.create(EnrollmentValidator.validateMaxEnrollments(0L))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateMaxEnrollments_belowMax_success() {
        StepVerifier.create(EnrollmentValidator.validateMaxEnrollments(4L))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateMaxEnrollments_atMax_throwsMaxExceeded() {
        StepVerifier.create(EnrollmentValidator.validateMaxEnrollments(5L))
                .expectError(MaxEnrollmentsExceededException.class)
                .verify();
    }

    @Test
    void validateMaxEnrollments_aboveMax_throwsMaxExceeded() {
        StepVerifier.create(EnrollmentValidator.validateMaxEnrollments(6L))
                .expectError(MaxEnrollmentsExceededException.class)
                .verify();
    }

    @Test
    void validateNotAlreadyEnrolled_notEnrolled_success() {
        StepVerifier.create(EnrollmentValidator.validateNotAlreadyEnrolled(
                        false, 1L, 1L))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateNotAlreadyEnrolled_alreadyEnrolled_throwsAlreadyEnrolled() {
        StepVerifier.create(EnrollmentValidator.validateNotAlreadyEnrolled(
                        true, 1L, 1L))
                .expectError(AlreadyEnrolledException.class)
                .verify();
    }

    @Test
    void validateNoDateConflict_emptyList_success() {
        Bootcamp newBootcamp = new Bootcamp(1L, "New",
                LocalDate.of(2026, 6, 1), 3);

        StepVerifier.create(EnrollmentValidator.validateNoDateConflict(
                        newBootcamp, List.of()))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateNoDateConflict_noOverlap_success() {
        Bootcamp newBootcamp = new Bootcamp(1L, "New",
                LocalDate.of(2026, 10, 1), 3);
        Bootcamp enrolled = new Bootcamp(2L, "Old",
                LocalDate.of(2026, 1, 1), 3);

        StepVerifier.create(EnrollmentValidator.validateNoDateConflict(newBootcamp, List.of(enrolled)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateNoDateConflict_overlap_throwsConflict() {
        Bootcamp newBootcamp = new Bootcamp(1L, "New",
                LocalDate.of(2026, 5, 1), 3);
        Bootcamp enrolled = new Bootcamp(2L, "Old",
                LocalDate.of(2026, 4, 1), 4);

        StepVerifier.create(EnrollmentValidator.validateNoDateConflict(newBootcamp, List.of(enrolled)))
                .expectErrorMatches(e -> e instanceof BootcampDateConflictException &&
                        e.getMessage().contains("2"))
                .verify();
    }

    @Test
    void validateNoDateConflict_exactlyAdjacent_success() {
        Bootcamp newBootcamp = new Bootcamp(1L, "New",
                LocalDate.of(2026, 4, 1), 3);
        Bootcamp enrolled = new Bootcamp(2L, "Old",
                LocalDate.of(2026, 1, 1), 3);

        StepVerifier.create(EnrollmentValidator.validateNoDateConflict(newBootcamp, List.of(enrolled)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateNoDateConflict_newStartsBeforeEnrolledEnds_throwsConflict() {
        Bootcamp newBootcamp = new Bootcamp(1L, "New",
                LocalDate.of(2026, 3, 1), 3);
        Bootcamp enrolled = new Bootcamp(2L, "Old",
                LocalDate.of(2026, 1, 1), 4);

        StepVerifier.create(EnrollmentValidator.validateNoDateConflict(newBootcamp, List.of(enrolled)))
                .expectError(BootcampDateConflictException.class)
                .verify();
    }

    @Test
    void validateNoDateConflict_multipleEnrolled_oneConflicts_throwsConflict() {
        Bootcamp newBootcamp = new Bootcamp(1L, "New",
                LocalDate.of(2026, 6, 1), 3);
        Bootcamp noConflict = new Bootcamp(2L, "Past",
                LocalDate.of(2025, 1, 1), 2);
        Bootcamp conflict = new Bootcamp(3L, "Conflict",
                LocalDate.of(2026, 5, 1), 4);

        StepVerifier.create(EnrollmentValidator.validateNoDateConflict(newBootcamp, List.of(noConflict, conflict)))
                .expectError(BootcampDateConflictException.class)
                .verify();
    }
}
