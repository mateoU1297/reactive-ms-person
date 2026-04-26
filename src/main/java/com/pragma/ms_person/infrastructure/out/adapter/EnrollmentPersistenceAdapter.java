package com.pragma.ms_person.infrastructure.out.adapter;

import com.pragma.ms_person.domain.model.Enrollment;
import com.pragma.ms_person.domain.spi.IEnrollmentPersistencePort;
import com.pragma.ms_person.infrastructure.out.mapper.IEnrollmentEntityMapper;
import com.pragma.ms_person.infrastructure.out.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EnrollmentPersistenceAdapter implements IEnrollmentPersistencePort {

    private final EnrollmentRepository enrollmentRepository;
    private final IEnrollmentEntityMapper enrollmentEntityMapper;

    @Override
    public Mono<Enrollment> save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollmentEntityMapper.toEntity(enrollment))
                .map(enrollmentEntityMapper::toDomain);
    }

    @Override
    public Flux<Enrollment> findByPersonId(Long personId) {
        return enrollmentRepository.findByPersonId(personId)
                .map(enrollmentEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByPersonIdAndBootcampId(Long personId, Long bootcampId) {
        return enrollmentRepository.existsByPersonIdAndBootcampId(personId, bootcampId);
    }

    @Override
    public Mono<Long> countByPersonId(Long personId) {
        return enrollmentRepository.countByPersonId(personId);
    }
}
