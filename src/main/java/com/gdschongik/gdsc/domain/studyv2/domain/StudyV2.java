package com.gdschongik.gdsc.domain.studyv2.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 스터디 애그리거트 루트 엔티티입니다.
 * [메타] 태그가 있는 필드는 수강 신청 시 참고용으로만 사용되며, 스터디회차 엔티티 내부 상태 검증에 사용되지 않습니다.
 * 혼동이 예상되는 경우 코멘트에 해당 태그가 명시되어 있습니다.
 * 그 외 필드의 경우 스터디 애그리거트 내부 상태 검증에 사용될 수 있습니다. (ex: 총 회차 수)
 */
@Getter
@Entity
@Table(name = "study_v2")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyV2 extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_v2_id")
    private Long id;

    private StudyType type;

    @Comment("스터디 이름")
    private String title;

    @Comment("스터디 소개")
    private String description;

    @Comment("스터디 소개 노션 링크")
    @Column(columnDefinition = "TEXT")
    private String descriptionNotionLink;

    @Embedded
    private Semester semester;

    @Comment("총 회차 수")
    private Integer totalRound;

    @Comment("[메타] 스터디 요일")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Comment("[메타] 스터디 시작 시간")
    private LocalTime startTime;

    @Comment("[메타] 스터디 종료 시간")
    private LocalTime endTime;

    @Embedded
    @AttributeOverride(name = "startDate", column = @Column(name = "application_start_date"))
    @AttributeOverride(name = "endDate", column = @Column(name = "application_end_date"))
    private Period applicationPeriod;

    @Comment("디스코드 채널 ID")
    private String discordChannelId;

    @Comment("디스코드 역할 ID")
    private String discordRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member mentor;

    @OneToMany(mappedBy = "studyV2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudySessionV2> studySessions = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private StudyV2(
            StudyType type,
            String title,
            String description,
            String descriptionNotionLink,
            Semester semester,
            Integer totalRound,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Period applicationPeriod,
            String discordChannelId,
            String discordRoleId,
            Member mentor) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.descriptionNotionLink = descriptionNotionLink;
        this.semester = semester;
        this.totalRound = totalRound;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.applicationPeriod = applicationPeriod;
        this.discordChannelId = discordChannelId;
        this.discordRoleId = discordRoleId;
        this.mentor = mentor;
    }

    public static StudyV2 create(
            StudyType type,
            String title,
            String description,
            String descriptionNotionLink,
            Semester semester,
            Integer totalRound,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            Period applicationPeriod,
            String discordChannelId,
            String discordRoleId,
            Member mentor) {
        return StudyV2.builder()
                .type(type)
                .title(title)
                .description(description)
                .descriptionNotionLink(descriptionNotionLink)
                .semester(semester)
                .totalRound(totalRound)
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .applicationPeriod(applicationPeriod)
                .discordChannelId(discordChannelId)
                .discordRoleId(discordRoleId)
                .mentor(mentor)
                .build();
    }
}
