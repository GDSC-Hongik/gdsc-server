package com.gdschongik.gdsc.helper;

import com.gdschongik.gdsc.config.TestQuerydslConfig;
import com.gdschongik.gdsc.config.TestRedisConfig;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import com.gdschongik.gdsc.global.security.PrincipalDetails;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({TestQuerydslConfig.class, TestRedisConfig.class, DatabaseCleaner.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class RepositoryTest {

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    protected void logoutAndReloginAs(Long memberId, MemberRole memberRole) {
        PrincipalDetails principalDetails = new PrincipalDetails(memberId, memberRole);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
