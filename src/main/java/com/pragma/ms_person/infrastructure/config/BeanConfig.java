package com.pragma.ms_person.infrastructure.config;

import com.pragma.ms_person.domain.api.IPersonServicePort;
import com.pragma.ms_person.domain.spi.IBootcampClientPort;
import com.pragma.ms_person.domain.spi.IEnrollmentPersistencePort;
import com.pragma.ms_person.domain.spi.IPersonPersistencePort;
import com.pragma.ms_person.domain.usecase.PersonUseCase;
import com.pragma.ms_person.infrastructure.out.adapter.BootcampWebClientAdapter;
import com.pragma.ms_person.infrastructure.out.adapter.EnrollmentPersistenceAdapter;
import com.pragma.ms_person.infrastructure.out.adapter.PersonPersistenceAdapter;
import com.pragma.ms_person.infrastructure.out.mapper.IEnrollmentEntityMapper;
import com.pragma.ms_person.infrastructure.out.mapper.IPersonEntityMapper;
import com.pragma.ms_person.infrastructure.out.repository.EnrollmentRepository;
import com.pragma.ms_person.infrastructure.out.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final PersonRepository personRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final IPersonEntityMapper personEntityMapper;
    private final IEnrollmentEntityMapper enrollmentEntityMapper;

    @Bean
    public WebClient bootcampWebClient(
            @Value("${clients.bootcamp.url}") String bootcampUrl) {
        return WebClient.builder()
                .baseUrl(bootcampUrl)
                .build();
    }

    @Bean
    public IBootcampClientPort bootcampClientPort(WebClient bootcampWebClient) {
        return new BootcampWebClientAdapter(bootcampWebClient);
    }

    @Bean
    public IPersonPersistencePort personPersistencePort() {
        return new PersonPersistenceAdapter(personRepository, personEntityMapper);
    }

    @Bean
    public IEnrollmentPersistencePort enrollmentPersistencePort() {
        return new EnrollmentPersistenceAdapter(enrollmentRepository, enrollmentEntityMapper);
    }

    @Bean
    public IPersonServicePort personServicePort(IBootcampClientPort bootcampClientPort) {
        return new PersonUseCase(personPersistencePort(), enrollmentPersistencePort(), bootcampClientPort);
    }
}
