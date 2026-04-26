package com.pragma.ms_person.domain.model;

import java.time.LocalDate;

public class Enrollment {
    private Long id;
    private Long personId;
    private Long bootcampId;
    private LocalDate enrollmentDate;

    public Enrollment() {
    }

    public Enrollment(Long id, Long personId, Long bootcampId, LocalDate enrollmentDate) {
        this.id = id;
        this.personId = personId;
        this.bootcampId = bootcampId;
        this.enrollmentDate = enrollmentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getBootcampId() {
        return bootcampId;
    }

    public void setBootcampId(Long bootcampId) {
        this.bootcampId = bootcampId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
