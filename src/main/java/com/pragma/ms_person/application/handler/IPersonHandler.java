package com.pragma.ms_person.application.handler;

import com.pragma.ms_person.application.dto.EnrollmentResponse;
import com.pragma.ms_person.application.dto.PersonRequest;
import com.pragma.ms_person.application.dto.PersonResponse;
import reactor.core.publisher.Mono;

public interface IPersonHandler {
    Mono<PersonResponse> save(PersonRequest request);

    Mono<EnrollmentResponse> enroll(Long personId, Long bootcampId);
}
