package com.z100.valentuesday.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "constants")
public class Constants {

	private String tokenPrefix;

	private String secret;

	private List<String> uriwhitelist;

	private Map<String, Integer> expiration;

	private Map<String, String> claims;
}
