package com.gdschongik.gdsc.domain.member.dao;

import com.gdschongik.gdsc.domain.common.model.RequirementStatus;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.domain.member.dto.request.MemberQueryOption;
import jakarta.annotation.Nullable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {

    Page<Member> findAllByRole(
            MemberQueryOption queryOption, Pageable pageable, @Nullable List<MemberRole> memberRoles);

    List<Member> findAllByRole(@Nullable MemberRole role);

    List<Member> findAllByDiscordStatus(RequirementStatus discordStatus);
}
