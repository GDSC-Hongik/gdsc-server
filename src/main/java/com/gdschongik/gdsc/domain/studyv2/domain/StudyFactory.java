package com.gdschongik.gdsc.domain.studyv2.domain;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.global.annotation.DomainFactory;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.stream.IntStream;

@DomainFactory
public class StudyFactory {

    /**
     * 스터디 및 스터디회차를 생성합니다.
     * 스터디회차의 경우 총 회차 수만큼 생성되며, 생성 순서에 따라 position 값이 지정됩니다.
     */
    public StudyV2 create(
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
            Member mentor,
            AttendanceNumberGenerator attendanceNumberGenerator) {
        StudyV2 study = StudyV2.create(
                type,
                title,
                description,
                descriptionNotionLink,
                semester,
                totalRound,
                dayOfWeek,
                startTime,
                endTime,
                applicationPeriod,
                discordChannelId,
                discordRoleId,
                mentor);

        IntStream.rangeClosed(1, totalRound)
                .forEach(round -> StudySessionV2.createEmpty(round, attendanceNumberGenerator.generate(), study));

        return study;
    }
}
