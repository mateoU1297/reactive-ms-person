package com.pragma.ms_person.infrastructure.input.rest;

import com.pragma.ms_person.application.dto.EnrollmentRequest;
import com.pragma.ms_person.application.dto.PersonRequest;
import com.pragma.ms_person.application.handler.IPersonHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PersonRestHandler {

    private final IPersonHandler personHandler;

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(PersonRequest.class)
                .flatMap(personHandler::save)
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> enroll(ServerRequest request) {
        Long personId = Long.parseLong(request.pathVariable("personId"));
        return request.bodyToMono(EnrollmentRequest.class)
                .flatMap(req -> personHandler.enroll(personId, req.getBootcampId()))
                .flatMap(response -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> findEnrolledPersonsByBootcampId(ServerRequest request) {
        Long bootcampId = Long.parseLong(request.pathVariable("bootcampId"));
        return personHandler.findEnrolledPersonsByBootcampId(bootcampId)
                .collectList()
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }
}
