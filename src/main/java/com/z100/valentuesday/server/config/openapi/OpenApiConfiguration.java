package com.z100.valentuesday.server.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@OpenAPIDefinition(security = { @SecurityRequirement(name = "bearer-key") })
public class OpenApiConfiguration {

//	@Bean
//	public OpenApiCustomiser globalHeaderOpenApiCustomizer() {
//
//		SecurityScheme securityScheme = new SecurityScheme()
//				.type(SecurityScheme.Type.HTTP)
//				.scheme("bearer")
//				.bearerFormat("JWT");
//
//		return openApi -> openApi.getComponents()
//				.addSecuritySchemes("bearer-key", securityScheme);
//	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}
