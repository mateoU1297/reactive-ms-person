package com.pragma.ms_person.application.mapper;

import com.pragma.ms_person.application.dto.PersonRequest;
import com.pragma.ms_person.application.dto.PersonResponse;
import com.pragma.ms_person.domain.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPersonMapper {
    Person toDomain(PersonRequest request);

    PersonResponse toResponse(Person person);
}
