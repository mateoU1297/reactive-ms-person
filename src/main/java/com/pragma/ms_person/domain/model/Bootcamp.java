package com.pragma.ms_person.domain.model;

import java.time.LocalDate;

public class Bootcamp {
    private Long id;
    private String name;
    private LocalDate launchDate;
    private Integer durationMonths;

    public Bootcamp() {
    }

    public Bootcamp(Long id, String name, LocalDate launchDate, Integer durationMonths) {
        this.id = id;
        this.name = name;
        this.launchDate = launchDate;
        this.durationMonths = durationMonths;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }
}
