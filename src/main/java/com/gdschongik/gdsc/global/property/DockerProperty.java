package com.gdschongik.gdsc.global.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "docker")
public class DockerProperty {
	private final String tag;
}
