package com.pragma.ms_person.domain.usecase;

import com.pragma.ms_person.domain.api.IPersonServicePort;
import com.pragma.ms_person.domain.exception.BootcampNotFoundException;
import com.pragma.ms_person.domain.exception.PersonAlreadyExistsException;
import com.pragma.ms_person.domain.exception.PersonNotFoundException;
import com.pragma.ms_person.domain.model.Bootcamp;
import com.pragma.ms_person.domain.model.Enrollment;
import com.pragma.ms_person.domain.model.Person;
import com.pragma.ms_person.domain.spi.IBootcampClientPort;
import com.pragma.ms_person.domain.spi.IEnrollmentPersistencePort;
import com.pragma.ms_person.domain.spi.IPersonPersistencePort;
import com.pragma.ms_person.domain.spi.IReportClientPort;
import com.pragma.ms_person.domain.validator.EnrollmentValidator;
import com.pragma.ms_person.domain.validator.PersonValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public class PersonUseCase implements IPersonServicePort {

    private final IPersonPersistencePort personPersistencePort;
    private final IEnrollmentPersistencePort enrollmentPersistencePort;
    private final IBootcampClientPort bootcampClientPort;
    private final IReportClientPort reportClientPort;

    public PersonUseCase(IPersonPersistencePort personPersistencePort,
                         IEnrollmentPersistencePort enrollmentPersistencePort,
                         IBootcampClientPort bootcampClientPort,
                         IReportClientPort reportClientPort) {
        this.personPersistencePort = personPersistencePort;
        this.enrollmentPersistencePort = enrollmentPersistencePort;
        this.bootcampClientPort = bootcampClientPort;
        this.reportClientPort = reportClientPort;
    }

    @Override
    public Mono<Person> save(Person person) {
        return PersonValidator.validate(person)
                .flatMap(p -> personPersistencePort.existsByEmail(p.getEmail()))
                .flatMap(exists -> {
                    if (exists)
                        return Mono.error(new PersonAlreadyExistsException(person.getEmail()));
                    return personPersistencePort.save(person);
                });
    }

    @Override
    public Mono<Enrollment> enroll(Long personId, Long bootcampId) {
        return personPersistencePort.existsById(personId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new PersonNotFoundException(personId));

                    return enrollmentPersistencePort.existsByPersonIdAndBootcampId(personId, bootcampId);
                })
                .flatMap(alreadyEnrolled ->
                        EnrollmentValidator.validateNotAlreadyEnrolled(alreadyEnrolled, personId, bootcampId)
                )
                .flatMap(valid -> enrollmentPersistencePort.countByPersonId(personId))
                .flatMap(EnrollmentValidator::validateMaxEnrollments)
                .flatMap(valid -> bootcampClientPort.findById(bootcampId)
                        .switchIfEmpty(Mono.error(new BootcampNotFoundException(bootcampId)))
                )
                .flatMap(newBootcamp ->
                        getEnrolledBootcamps(personId)
                                .flatMap(enrolledBootcamps ->
                                        EnrollmentValidator.validateNoDateConflict(newBootcamp, enrolledBootcamps)
                                                .thenReturn(newBootcamp)
                                )
                )
                .flatMap(newBootcamp ->
                        enrollmentPersistencePort.save(new Enrollment(null, personId, bootcampId, LocalDate.now()))
                )
                .doOnSuccess(saved -> reportClientPort.notifyPersonEnrolled(bootcampId));
    }

    @Override
    public Flux<Person> findEnrolledPersonsByBootcampId(Long bootcampId) {
        return enrollmentPersistencePort.findByBootcampId(bootcampId)
                .flatMap(enrollment -> personPersistencePort.findById(enrollment.getPersonId()));
    }

    private Mono<List<Bootcamp>> getEnrolledBootcamps(Long personId) {
        return enrollmentPersistencePort.findByPersonId(personId)
                .flatMap(enrollment -> bootcampClientPort.findById(enrollment.getBootcampId()))
                .collectList();
    }
}