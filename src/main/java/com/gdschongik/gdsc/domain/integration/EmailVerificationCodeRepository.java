package com.gdschongik.gdsc.domain.integration;

import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationCodeRepository
    extends CrudRepository<EmailVerificationCode, String> {

}
