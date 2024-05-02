package com.gdschongik.gdsc.domain.application.dao;

import com.gdschongik.gdsc.domain.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationCustomRepository {}
