package com.gdschongik.gdsc.domain.common.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseSemesterEntity extends BaseTimeEntity {

    private Integer academicYear;

    @Enumerated(EnumType.STRING)
    private SemesterType semesterType;
}
