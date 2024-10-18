package com.gdschongik.gdsc.domain.test.dao;

import com.gdschongik.gdsc.domain.test.domain.MyMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyMembershipRepository extends JpaRepository<MyMembership, Long> {}
