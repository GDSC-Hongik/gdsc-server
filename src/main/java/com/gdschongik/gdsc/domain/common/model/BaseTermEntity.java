package com.gdschongik.gdsc.domain.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseTermEntity {

    @Column
    private int year;

    @Column
    @Enumerated(EnumType.STRING)
    private Semester semester;
}
