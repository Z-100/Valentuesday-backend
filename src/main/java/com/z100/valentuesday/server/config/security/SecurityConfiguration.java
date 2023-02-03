package com.z100.valentuesday.server.config.security;

import com.z100.valentuesday.server.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final AuthorizationFilter authorizationFilter;

	private static final String[] AUTH_WHITELIST = {
			// -- Swagger UI v3
			"/v3/api-docs/**",
			"v3/api-docs/**",
			"/swagger-ui/**",
			"swagger-ui/**",
	};

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		return http.csrf(AbstractHttpConfigurer::disable)
//				.authorizeHttpRequests( auth -> auth
//						.requestMatchers(AUTH_WHITELIST).permitAll()
//						.anyRequest().authenticated()
//				)
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.httpBasic(withDefaults())
//				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
//				.build();
//	}
//
//	@Bean
//	public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
//
//		httpSecurity.authorizeHttpRequests(requests -> requests
//						.requestMatchers(AUTH_WHITELIST).permitAll()
//						.anyRequest().authenticated())
//				.httpBasic();
//
//		return httpSecurity.build();
//	}
}
