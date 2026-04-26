package com.pragma.ms_person.infrastructure.out.http;

import com.pragma.ms_person.domain.spi.IReportClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
public class ReportWebClientAdapter implements IReportClientPort {

    private final WebClient webClient;

    @Override
    public void notifyPersonEnrolled(Long bootcampId) {
        webClient.patch()
                .uri("/api/v1/reports/bootcamps/{bootcampId}/persons", bootcampId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(e -> log.error("Error notifying report service: {}", e.getMessage()))
                .subscribe();
    }
}
