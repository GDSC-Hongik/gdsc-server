package com.gdschongik.gdsc.domain.common.vo;

import com.gdschongik.gdsc.domain.common.model.SemesterType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Semester {

    private Integer academicYear;

    @Enumerated(EnumType.STRING)
    private SemesterType semesterType;

    @Builder(access = AccessLevel.PRIVATE)
    private Semester(int academicYear, SemesterType semesterType) {
        this.academicYear = academicYear;
        this.semesterType = semesterType;
    }

    public static Semester of(int academicYear, SemesterType semesterType) {
        return Semester.builder()
                .academicYear(academicYear)
                .semesterType(semesterType)
                .build();
    }
}
