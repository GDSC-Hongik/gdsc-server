package com.gdschongik.gdsc.domain.test.dao;

import com.gdschongik.gdsc.domain.test.domain.MyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyMemberRepository extends JpaRepository<MyMember, Long> {}
