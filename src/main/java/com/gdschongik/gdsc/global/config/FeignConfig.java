package com.gdschongik.gdsc.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.gdschongik.gdsc.infra")
public class FeignConfig {}
