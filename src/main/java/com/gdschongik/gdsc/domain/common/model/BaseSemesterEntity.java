package com.gdschongik.gdsc.domain.common.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseSemesterEntity extends BaseEntity {

    private Integer academicYear;

    @Enumerated(EnumType.STRING)
    private SemesterType semesterType;

    protected void updateAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    protected void updateSemesterType(SemesterType semesterType) {
        this.semesterType = semesterType;
    }
}
