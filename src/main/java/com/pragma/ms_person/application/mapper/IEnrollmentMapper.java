package com.pragma.ms_person.application.mapper;

import com.pragma.ms_person.application.dto.EnrollmentResponse;
import com.pragma.ms_person.domain.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IEnrollmentMapper {
    EnrollmentResponse toResponse(Enrollment enrollment);
}