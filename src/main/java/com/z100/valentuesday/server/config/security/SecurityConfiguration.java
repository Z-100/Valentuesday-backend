package com.z100.valentuesday.server.config.security;

import com.z100.valentuesday.server.filter.AuthenticationFilter;
import com.z100.valentuesday.server.filter.AuthorizationFilter;
import com.z100.valentuesday.server.util.AccountDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final AuthorizationFilter authorizationFilter;

	private final AuthenticationFilter authenticationFilter;

	private final AccountDetailsService accountDetailsService;

	private static final String[] AUTH_WHITELIST = {
			"/api-docs",
			"/swagger-resources/",
			"/swagger-ui",
			"/swagger-ui.html",
			"/account/register",
			"/check-activation-key",
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		authenticationFilter.setFilterProcessesUrl("/account/login");

		return http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(AUTH_WHITELIST).permitAll()
						.anyRequest().authenticated()
				)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.httpBasic(withDefaults())
				.addFilter(authenticationFilter)
				.addFilterAfter(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authorizeHttpRequests(requests -> requests
						.requestMatchers(AUTH_WHITELIST).permitAll()
						.anyRequest().authenticated())
				.httpBasic();

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
