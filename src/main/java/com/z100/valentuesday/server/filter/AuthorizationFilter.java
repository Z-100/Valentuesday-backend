package com.z100.valentuesday.server.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.z100.valentuesday.api.exception.ApiException;
import com.z100.valentuesday.server.Constants;
import com.z100.valentuesday.server.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil tokenUtil;

	private Constants constants;

	private final Predicate<HttpServletRequest> isUnprotectedUrl = (req) ->
			constants.getUriwhitelist().stream().anyMatch(req.getRequestURI()::contains);

	public AuthorizationFilter(JwtUtil tokenUtil, Constants constants) {
		this.tokenUtil = tokenUtil;
		this.constants = constants;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return isUnprotectedUrl.test(request);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		createSessionFrom(request.getHeader(AUTHORIZATION));

		filterChain.doFilter(request, response);
	}

	private void createSessionFrom(String authorizationHeader) {

		tokenUtil.validateBearer(authorizationHeader);

		DecodedJWT decodedJWT;
		try {
			decodedJWT = tokenUtil.extractBearer.apply(authorizationHeader);
		} catch (Exception e) {
			throw new ApiException(e.getMessage(), UNAUTHORIZED);
		}

		tokenUtil.validateTokenExpiration(decodedJWT);

		String username = decodedJWT.getSubject();
		String[] roles = decodedJWT.getClaim(constants.getClaims().get("roles")).asArray(String.class);
		String actKey = decodedJWT.getClaim("act").asString();

		List<SimpleGrantedAuthority> authorities = roles != null ?
				Stream.of(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList()) :
				new ArrayList<>();

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, actKey, authorities);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}
}