package com.gdschongik.gdsc.repository;

import com.gdschongik.gdsc.config.TestQuerydslConfig;
import com.gdschongik.gdsc.config.TestRedisConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({TestQuerydslConfig.class, TestRedisConfig.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public interface RepositoryTest {}
