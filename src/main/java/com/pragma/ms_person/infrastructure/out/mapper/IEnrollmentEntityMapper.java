package com.pragma.ms_person.infrastructure.out.mapper;

import com.pragma.ms_person.domain.model.Enrollment;
import com.pragma.ms_person.infrastructure.out.entity.EnrollmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IEnrollmentEntityMapper {
    EnrollmentEntity toEntity(Enrollment enrollment);

    Enrollment toDomain(EnrollmentEntity entity);
}
