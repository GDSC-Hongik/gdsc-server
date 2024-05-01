package com.gdschongik.gdsc.domain.common.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseTermEntity {

    private int year;

    @Enumerated(EnumType.STRING)
    private Semester semester;
}
