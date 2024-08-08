package com.gdschongik.gdsc.domain.study.dao;

import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentHistoryRepository
        extends JpaRepository<AssignmentHistory, Long>, AssignmentHistoryCustomRepository {}
