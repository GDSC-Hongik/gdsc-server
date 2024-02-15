package com.gdschongik.gdsc.domain.email.dao;

import com.gdschongik.gdsc.domain.email.domain.VerificationCodeAndEmail;
import org.springframework.data.repository.CrudRepository;

public interface VerificationCodeAndEmailRepository extends CrudRepository<VerificationCodeAndEmail, String> {}
