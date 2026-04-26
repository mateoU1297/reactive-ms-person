package com.pragma.ms_person.infrastructure.out.mapper;

import com.pragma.ms_person.domain.model.Person;
import com.pragma.ms_person.infrastructure.out.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPersonEntityMapper {
    PersonEntity toEntity(Person person);

    Person toDomain(PersonEntity entity);
}
