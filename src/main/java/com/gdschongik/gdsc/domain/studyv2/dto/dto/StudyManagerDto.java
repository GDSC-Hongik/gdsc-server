package com.gdschongik.gdsc.domain.studyv2.dto.dto;

import com.gdschongik.gdsc.domain.common.vo.Period;
import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.study.domain.StudyType;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * 스터디 관리자 DTO입니다. 디스코드 관련 ID가 포함되어 있습니다.
 */
public record StudyManagerDto(
        Long studyId,
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
        Long mentorId) {}
