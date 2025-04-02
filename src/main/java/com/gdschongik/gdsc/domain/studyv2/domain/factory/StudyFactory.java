package com.gdschongik.gdsc.domain.studyv2.domain.factory;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import com.gdschongik.gdsc.domain.studyv2.domain.service.AttendanceNumberGenerator;
import com.gdschongik.gdsc.domain.studyv2.domain.StudySessionV2;
import com.gdschongik.gdsc.domain.studyv2.domain.StudyV2;
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
        if (type.isLive()) {
            return createLive(
                    type,
                    title,
                    semester,
                    totalRound,
                    dayOfWeek,
                    startTime,
                    endTime,
                    applicationPeriod,
                    discordChannelId,
                    discordRoleId,
                    mentor,
                    attendanceNumberGenerator);
        } else {
            return createAssignment(
                    title, semester, totalRound, applicationPeriod, discordChannelId, discordRoleId, mentor);
        }
    }

    private StudyV2 createLive(
            StudyType type,
            String title,
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
        StudyV2 study = StudyV2.createLive(
                type,
                title,
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
                .forEach(
                        round -> StudySessionV2.createEmptyForLive(round, attendanceNumberGenerator.generate(), study));

        return study;
    }

    private StudyV2 createAssignment(
            String title,
            Semester semester,
            Integer totalRound,
            Period applicationPeriod,
            String discordChannelId,
            String discordRoleId,
            Member mentor) {
        StudyV2 study = StudyV2.createAssignment(
                title, semester, totalRound, applicationPeriod, discordChannelId, discordRoleId, mentor);

        IntStream.rangeClosed(1, totalRound).forEach(round -> StudySessionV2.createEmptyForAssignment(round, study));

        return study;
    }
}
