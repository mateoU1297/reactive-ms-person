package com.pragma.ms_person.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcampClientResponse {
    private Long id;
    private String name;
    private LocalDate launchDate;
    private Integer durationMonths;
}
