package com.gdschongik.gdsc.domain.discord.dao;

import com.gdschongik.gdsc.domain.discord.domain.DiscordVerificationCode;
import org.springframework.data.repository.CrudRepository;

public interface DiscordVerificationCodeRepository extends CrudRepository<DiscordVerificationCode, String> {}
