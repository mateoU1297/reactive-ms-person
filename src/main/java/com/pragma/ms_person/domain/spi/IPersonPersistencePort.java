package com.pragma.ms_person.domain.spi;

import com.pragma.ms_person.domain.model.Person;
import reactor.core.publisher.Mono;

public interface IPersonPersistencePort {
    Mono<Person> save(Person person);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsById(Long id);

    Mono<Person> findById(Long id);
}
