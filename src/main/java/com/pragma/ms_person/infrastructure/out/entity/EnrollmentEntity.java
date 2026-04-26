package com.pragma.ms_person.infrastructure.out.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("enrollment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentEntity {
    @Id
    private Long id;
    private Long personId;
    private Long bootcampId;
    private LocalDate enrollmentDate;
}
