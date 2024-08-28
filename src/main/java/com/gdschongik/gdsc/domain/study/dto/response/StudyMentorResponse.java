package com.gdschongik.gdsc.domain.study.dto.response;

import com.gdschongik.gdsc.domain.member.domain.Member;

public record StudyMentorResponse(Long mentorId, String mentorName) {
    public static StudyMentorResponse from(Member mentor) {
        return new StudyMentorResponse(mentor.getId(), mentor.getName());
    }
}
