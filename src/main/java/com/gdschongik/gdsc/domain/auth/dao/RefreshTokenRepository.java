package com.gdschongik.gdsc.domain.auth.dao;

import com.gdschongik.gdsc.domain.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long aLong);
}
