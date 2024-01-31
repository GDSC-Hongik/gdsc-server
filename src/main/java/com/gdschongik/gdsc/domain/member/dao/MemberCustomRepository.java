package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
    Page<Member> findAll(String keyword, String type, Pageable pageable);
}
