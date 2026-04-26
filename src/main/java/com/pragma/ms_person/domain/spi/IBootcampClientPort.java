package com.pragma.ms_person.domain.spi;

import com.pragma.ms_person.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampClientPort {
    Mono<Bootcamp> findById(Long id);
}
