package com.pragma.ms_person.application.handler.impl;

import com.pragma.ms_person.application.dto.EnrollmentResponse;
import com.pragma.ms_person.application.dto.PersonRequest;
import com.pragma.ms_person.application.dto.PersonResponse;
import com.pragma.ms_person.application.handler.IPersonHandler;
import com.pragma.ms_person.application.mapper.IEnrollmentMapper;
import com.pragma.ms_person.application.mapper.IPersonMapper;
import com.pragma.ms_person.domain.api.IPersonServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PersonHandlerImpl implements IPersonHandler {

    private final IPersonServicePort personServicePort;
    private final IPersonMapper personMapper;
    private final IEnrollmentMapper enrollmentMapper;

    @Override
    public Mono<PersonResponse> save(PersonRequest request) {
        return personServicePort.save(personMapper.toDomain(request))
                .map(personMapper::toResponse);
    }

    @Override
    public Mono<EnrollmentResponse> enroll(Long personId, Long bootcampId) {
        return personServicePort.enroll(personId, bootcampId)
                .map(enrollmentMapper::toResponse);
    }
}