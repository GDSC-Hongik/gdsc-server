package com.gdschongik.gdsc.domain.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@MappedSuperclass
public abstract class BaseTermEntity {

    @Column
    private int year;

    @Column
    @Enumerated(EnumType.STRING)
    private Semester semester;

    @RequiredArgsConstructor
    public enum Semester {
        FIRST("1학기"),
        SECOND("2학기");

        private final String value;
    }
}
