package com.gdschongik.gdsc.domain.membership.dao;

import com.gdschongik.gdsc.domain.membership.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {}
