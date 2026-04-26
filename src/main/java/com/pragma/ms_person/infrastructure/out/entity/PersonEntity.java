package com.pragma.ms_person.infrastructure.out.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {
    @Id
    private Long id;
    private String name;
    private String email;
}
