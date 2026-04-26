package com.pragma.ms_person.domain.usecase;

import com.pragma.ms_person.domain.exception.AlreadyEnrolledException;
import com.pragma.ms_person.domain.exception.BootcampDateConflictException;
import com.pragma.ms_person.domain.exception.BootcampNotFoundException;
import com.pragma.ms_person.domain.exception.InvalidFieldException;
import com.pragma.ms_person.domain.exception.MaxEnrollmentsExceededException;
import com.pragma.ms_person.domain.exception.PersonAlreadyExistsException;
import com.pragma.ms_person.domain.exception.PersonNotFoundException;
import com.pragma.ms_person.domain.model.Bootcamp;
import com.pragma.ms_person.domain.model.Enrollment;
import com.pragma.ms_person.domain.model.Person;
import com.pragma.ms_person.domain.spi.IBootcampClientPort;
import com.pragma.ms_person.domain.spi.IEnrollmentPersistencePort;
import com.pragma.ms_person.domain.spi.IPersonPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseTest {

    @Mock
    private IPersonPersistencePort personPersistencePort;

    @Mock
    private IEnrollmentPersistencePort enrollmentPersistencePort;
    @Mock
    private IBootcampClientPort bootcampClientPort;

    @InjectMocks
    private PersonUseCase personUseCase;

    private Person person;
    private Bootcamp bootcamp;

    @BeforeEach
    void setUp() {
        person = new Person(null, "John Doe", "john@example.com");
        bootcamp = new Bootcamp(1L, "Java Bootcamp", LocalDate.of(2026, 6, 1), 3);
    }

    @Test
    void save_validPerson_success() {
        when(personPersistencePort.existsByEmail("john@example.com"))
                .thenReturn(Mono.just(false));
        when(personPersistencePort.save(any()))
                .thenReturn(Mono.just(new Person(1L, "John Doe", "john@example.com")));

        StepVerifier.create(personUseCase.save(person))
                .expectNextMatches(p -> p.getName().equals("John Doe"))
                .verifyComplete();
    }

    @Test
    void save_emailExists_throwsAlreadyExists() {
        when(personPersistencePort.existsByEmail("john@example.com"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(personUseCase.save(person))
                .expectError(PersonAlreadyExistsException.class)
                .verify();

        verify(personPersistencePort, never()).save(any());
    }

    @Test
    void save_nullName_throwsInvalidField() {
        person.setName(null);

        StepVerifier.create(personUseCase.save(person))
                .expectError(InvalidFieldException.class)
                .verify();

        verifyNoInteractions(personPersistencePort);
    }

    @Test
    void save_invalidEmail_throwsInvalidField() {
        person.setEmail("invalid-email");

        StepVerifier.create(personUseCase.save(person))
                .expectError(InvalidFieldException.class)
                .verify();

        verifyNoInteractions(personPersistencePort);
    }

    @Test
    void save_nullEmail_throwsInvalidField() {
        person.setEmail(null);

        StepVerifier.create(personUseCase.save(person))
                .expectError(InvalidFieldException.class)
                .verify();

        verifyNoInteractions(personPersistencePort);
    }

    @Test
    void enroll_validEnrollment_success() {
        Enrollment saved = new Enrollment(1L, 1L, 1L, LocalDate.now());

        when(personPersistencePort.existsById(1L)).thenReturn(Mono.just(true));
        when(enrollmentPersistencePort.existsByPersonIdAndBootcampId(1L, 1L))
                .thenReturn(Mono.just(false));
        when(enrollmentPersistencePort.countByPersonId(1L)).thenReturn(Mono.just(0L));
        when(bootcampClientPort.findById(1L)).thenReturn(Mono.just(bootcamp));
        when(enrollmentPersistencePort.findByPersonId(1L)).thenReturn(Flux.empty());
        when(enrollmentPersistencePort.save(any())).thenReturn(Mono.just(saved));

        StepVerifier.create(personUseCase.enroll(1L, 1L))
                .expectNextMatches(e -> e.getPersonId().equals(1L)
                        && e.getBootcampId().equals(1L))
                .verifyComplete();
    }

    @Test
    void enroll_personNotFound_throwsPersonNotFound() {
        when(personPersistencePort.existsById(1L)).thenReturn(Mono.just(false));

        StepVerifier.create(personUseCase.enroll(1L, 1L))
                .expectError(PersonNotFoundException.class)
                .verify();
    }

    @Test
    void enroll_alreadyEnrolled_throwsAlreadyEnrolled() {
        when(personPersistencePort.existsById(1L)).thenReturn(Mono.just(true));
        when(enrollmentPersistencePort.existsByPersonIdAndBootcampId(1L, 1L))
                .thenReturn(Mono.just(true));

        StepVerifier.create(personUseCase.enroll(1L, 1L))
                .expectError(AlreadyEnrolledException.class)
                .verify();
    }

    @Test
    void enroll_maxEnrollmentsReached_throwsMaxExceeded() {
        when(personPersistencePort.existsById(1L)).thenReturn(Mono.just(true));
        when(enrollmentPersistencePort.existsByPersonIdAndBootcampId(1L, 1L))
                .thenReturn(Mono.just(false));
        when(enrollmentPersistencePort.countByPersonId(1L)).thenReturn(Mono.just(5L));

        StepVerifier.create(personUseCase.enroll(1L, 1L))
                .expectError(MaxEnrollmentsExceededException.class)
                .verify();
    }

    @Test
    void enroll_bootcampNotFound_throwsNotFound() {
        when(personPersistencePort.existsById(1L)).thenReturn(Mono.just(true));
        when(enrollmentPersistencePort.existsByPersonIdAndBootcampId(1L, 1L))
                .thenReturn(Mono.just(false));
        when(enrollmentPersistencePort.countByPersonId(1L)).thenReturn(Mono.just(0L));
        when(bootcampClientPort.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(personUseCase.enroll(1L, 1L))
                .expectError(BootcampNotFoundException.class)
                .verify();
    }

    @Test
    void enroll_dateConflict_throwsDateConflict() {
        Bootcamp existing = new Bootcamp(2L, "Existing",
                LocalDate.of(2026, 5, 1), 4);
        Enrollment existingEnrollment = new Enrollment(1L, 1L, 2L, LocalDate.now());

        when(personPersistencePort.existsById(1L)).thenReturn(Mono.just(true));
        when(enrollmentPersistencePort.existsByPersonIdAndBootcampId(1L, 1L))
                .thenReturn(Mono.just(false));
        when(enrollmentPersistencePort.countByPersonId(1L)).thenReturn(Mono.just(1L));
        when(bootcampClientPort.findById(1L)).thenReturn(Mono.just(bootcamp));
        when(enrollmentPersistencePort.findByPersonId(1L))
                .thenReturn(Flux.just(existingEnrollment));
        when(bootcampClientPort.findById(2L)).thenReturn(Mono.just(existing));

        StepVerifier.create(personUseCase.enroll(1L, 1L))
                .expectError(BootcampDateConflictException.class)
                .verify();
    }

    @Test
    void enroll_noDateConflict_success() {
        Bootcamp past = new Bootcamp(2L, "Past",
                LocalDate.of(2025, 1, 1), 2);
        Enrollment existingEnrollment = new Enrollment(1L, 1L, 2L, LocalDate.now());
        Enrollment saved = new Enrollment(2L, 1L, 1L, LocalDate.now());

        when(personPersistencePort.existsById(1L)).thenReturn(Mono.just(true));
        when(enrollmentPersistencePort.existsByPersonIdAndBootcampId(1L, 1L))
                .thenReturn(Mono.just(false));
        when(enrollmentPersistencePort.countByPersonId(1L)).thenReturn(Mono.just(1L));
        when(bootcampClientPort.findById(1L)).thenReturn(Mono.just(bootcamp));
        when(enrollmentPersistencePort.findByPersonId(1L))
                .thenReturn(Flux.just(existingEnrollment));
        when(bootcampClientPort.findById(2L)).thenReturn(Mono.just(past));
        when(enrollmentPersistencePort.save(any())).thenReturn(Mono.just(saved));

        StepVerifier.create(personUseCase.enroll(1L, 1L))
                .expectNextCount(1)
                .verifyComplete();
    }
}