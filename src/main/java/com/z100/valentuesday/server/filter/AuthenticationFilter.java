package com.z100.valentuesday.server.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.z100.valentuesday.api.dto.JwtDTO;
import com.z100.valentuesday.api.exception.ApiException;
import com.z100.valentuesday.api.repository.AccountRepository;
import com.z100.valentuesday.server.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final Predicate<HttpServletRequest> isPost = req -> req.getMethod().equals("POST");

	private final AccountRepository accountRepository;

	private final JwtUtil tokenUtil;

	public AuthenticationFilter(@Lazy AuthenticationManager authenticationManager,
			JwtUtil tokenUtil, AccountRepository accountRepository) {
		super.setAuthenticationManager(authenticationManager);
		this.tokenUtil = tokenUtil;
		this.accountRepository = accountRepository;
	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		if (!isPost.test(request))
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		UsernamePasswordAuthenticationToken authRequest =
				UsernamePasswordAuthenticationToken.unauthenticated(username, password);

		this.setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, Authentication authentication) throws IOException {

		User user = (User) authentication.getPrincipal();

		String subject = user.getUsername();
		String issuer = request.getRequestURI();
		List<String> roles = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		String actkey = accountRepository.findByUsername(subject)
				.orElseThrow(() -> new ApiException("Account not found", INTERNAL_SERVER_ERROR))
				.getActivationKey();

		JwtDTO accessToken = tokenUtil.generateNewAccessToken(subject, issuer, roles, actkey);

		new ObjectMapper().writeValue(response.getOutputStream(), accessToken);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
		this.logger.trace("Failed to process authentication request", failed);
		this.logger.trace("Cleared SecurityContextHolder");
		this.logger.trace("Handling authentication failure");
		throw new ApiException("Login failed", UNAUTHORIZED);
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		return request.getHeader("username");
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		return request.getHeader("password");
	}
}
