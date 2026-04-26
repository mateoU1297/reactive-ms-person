package com.pragma.ms_person.domain.spi;

import com.pragma.ms_person.domain.model.Enrollment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEnrollmentPersistencePort {
    Mono<Enrollment> save(Enrollment enrollment);

    Flux<Enrollment> findByPersonId(Long personId);

    Mono<Boolean> existsByPersonIdAndBootcampId(Long personId, Long bootcampId);

    Mono<Long> countByPersonId(Long personId);
}
