package com.pragma.ms_person.infrastructure.out.repository;

import com.pragma.ms_person.infrastructure.out.entity.EnrollmentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EnrollmentRepository extends ReactiveCrudRepository<EnrollmentEntity, Long> {
    Flux<EnrollmentEntity> findByPersonId(Long personId);

    Mono<Boolean> existsByPersonIdAndBootcampId(Long personId, Long bootcampId);

    Mono<Long> countByPersonId(Long personId);
}
