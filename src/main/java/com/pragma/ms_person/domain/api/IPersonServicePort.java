package com.pragma.ms_person.domain.api;

import com.pragma.ms_person.domain.model.Enrollment;
import com.pragma.ms_person.domain.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPersonServicePort {
    Mono<Person> save(Person person);

    Mono<Enrollment> enroll(Long personId, Long bootcampId);

    Flux<Person> findEnrolledPersonsByBootcampId(Long bootcampId);
}
