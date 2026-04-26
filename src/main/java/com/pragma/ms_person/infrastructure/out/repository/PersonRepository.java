package com.pragma.ms_person.infrastructure.out.repository;

import com.pragma.ms_person.infrastructure.out.entity.PersonEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveCrudRepository<PersonEntity, Long> {
    Mono<Boolean> existsByEmail(String email);
}
