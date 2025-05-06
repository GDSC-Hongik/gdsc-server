package com.gdschongik.gdsc.domain.recruitment.dao;

import com.gdschongik.gdsc.domain.common.vo.Semester;
import com.gdschongik.gdsc.domain.recruitment.domain.RecruitmentRound;
import java.util.List;

public interface RecruitmentRoundCustomRepository {

    List<RecruitmentRound> findAllBySemester(Semester semester);
}
