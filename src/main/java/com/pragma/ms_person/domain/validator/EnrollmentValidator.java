package com.pragma.ms_person.domain.validator;

import com.pragma.ms_person.domain.exception.AlreadyEnrolledException;
import com.pragma.ms_person.domain.exception.BootcampDateConflictException;
import com.pragma.ms_person.domain.exception.MaxEnrollmentsExceededException;
import com.pragma.ms_person.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class EnrollmentValidator {

    private static final int MAX_ENROLLMENTS = 5;

    private EnrollmentValidator() {
    }

    public static Mono<Boolean> validateNotAlreadyEnrolled(boolean exists, Long personId, Long bootcampId) {
        if (exists)
            return Mono.error(new AlreadyEnrolledException(personId, bootcampId));
        return Mono.just(true);
    }

    public static Mono<Boolean> validateMaxEnrollments(long count) {
        if (count >= MAX_ENROLLMENTS)
            return Mono.error(new MaxEnrollmentsExceededException());
        return Mono.just(true);
    }

    public static Mono<Boolean> validateNoDateConflict(Bootcamp newBootcamp, List<Bootcamp> enrolledBootcamps) {
        LocalDate newStart = newBootcamp.getLaunchDate();
        LocalDate newEnd = newStart.plusMonths(newBootcamp.getDurationMonths());

        return enrolledBootcamps.stream()
                .sorted(Comparator.comparing(Bootcamp::getLaunchDate))
                .filter(enrolled -> hasDateConflict(newStart, newEnd, enrolled))
                .findFirst()
                .map(conflicting -> Mono.<Boolean>error(new BootcampDateConflictException(conflicting.getId())))
                .orElse(Mono.just(true));
    }

    private static boolean hasDateConflict(LocalDate newStart, LocalDate newEnd, Bootcamp enrolled) {
        LocalDate enrolledStart = enrolled.getLaunchDate();
        LocalDate enrolledEnd = enrolledStart.plusMonths(enrolled.getDurationMonths());
        return newStart.isBefore(enrolledEnd) && newEnd.isAfter(enrolledStart);
    }
}