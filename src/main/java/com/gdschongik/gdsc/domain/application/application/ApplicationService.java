package com.gdschongik.gdsc.domain.application.application;

import com.gdschongik.gdsc.domain.application.dao.ApplicationRepository;
import com.gdschongik.gdsc.domain.application.domain.Application;
import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.global.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    private final MemberUtil memberUtil;
    private final ApplicationRepository applicationRepository;

    public void apply() {
        Member currentMember = memberUtil.getCurrentMember();
        Application application = Application.createApplication(currentMember);
        applicationRepository.save(application);
    }
}
