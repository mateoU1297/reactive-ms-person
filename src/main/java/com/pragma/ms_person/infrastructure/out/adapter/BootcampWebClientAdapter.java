package com.pragma.ms_person.infrastructure.out.adapter;

import com.pragma.ms_person.application.dto.BootcampClientResponse;
import com.pragma.ms_person.domain.exception.BootcampNotFoundException;
import com.pragma.ms_person.domain.model.Bootcamp;
import com.pragma.ms_person.domain.spi.IBootcampClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BootcampWebClientAdapter implements IBootcampClientPort {

    private final WebClient webClient;

    @Override
    public Mono<Bootcamp> findById(Long id) {
        return webClient.get()
                .uri("/api/v1/bootcamps/{id}", id)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response -> Mono.error(new BootcampNotFoundException(id)))
                .bodyToMono(BootcampClientResponse.class)
                .map(r -> new Bootcamp(
                        r.getId(), r.getName(),
                        r.getLaunchDate(), r.getDurationMonths()
                ));
    }
}
