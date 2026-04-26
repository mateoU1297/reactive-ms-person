package com.pragma.ms_person.infrastructure.out.adapter;

import com.pragma.ms_person.domain.model.Person;
import com.pragma.ms_person.domain.spi.IPersonPersistencePort;
import com.pragma.ms_person.infrastructure.out.mapper.IPersonEntityMapper;
import com.pragma.ms_person.infrastructure.out.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PersonPersistenceAdapter implements IPersonPersistencePort {

    private final PersonRepository personRepository;
    private final IPersonEntityMapper personEntityMapper;

    @Override
    public Mono<Person> save(Person person) {
        return personRepository.save(personEntityMapper.toEntity(person))
                .map(personEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return personRepository.existsById(id);
    }
}
