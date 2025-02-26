package com.gdschongik.gdsc.domain.studyv2.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "attendance_v2",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "study_session_v2_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceV2 extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_v2_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_session_v2_id")
    private StudySessionV2 studySession;

    @Builder(access = AccessLevel.PRIVATE)
    private AttendanceV2(Member student, StudySessionV2 studySession) {
        this.student = student;
        this.studySession = studySession;
    }

    public static AttendanceV2 of(Member student, StudySessionV2 studySession) {
        return AttendanceV2.builder()
                .student(student)
                .studySession(studySession)
                .build();
    }
}
