package com.gdschongik.gdsc.global.property.email;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "email")
public record EmailProperty(Gmail gmail, String host, int port, String encoding, JavaMailProperty javaMailProperty) {}
