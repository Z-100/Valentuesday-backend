package com.z100.valentuesday;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ValentuesdayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValentuesdayApplication.class, args);
	}

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {

		// This is a hack to force Spring to load all the endpoints at startup.
		event.getApplicationContext().getBean(RequestMappingHandlerMapping.class)
				.getHandlerMethods()
				.forEach((key, value) -> log.debug(key + " - " + value));
	}
}
