package com.gdschongik.gdsc.domain.email.dao;

import com.gdschongik.gdsc.domain.email.domain.UnivEmailVerification;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UnivEmailVerificationRepository extends CrudRepository<UnivEmailVerification, String> {

    Optional<UnivEmailVerification> findByUnivEmail(String univEmail);
}
