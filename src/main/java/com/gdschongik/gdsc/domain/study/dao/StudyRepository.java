package com.gdschongik.gdsc.domain.study.dao;

import static com.gdschongik.gdsc.global.exception.ErrorCode.STUDY_NOT_FOUND;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.global.exception.CustomException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {

    default Study getById(Long id){ return findById(id).orElseThrow(()-> new CustomException(STUDY_NOT_FOUND));}
    List<Study> findAllByMentor(Member mentor);
}
