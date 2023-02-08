package com.z100.valentuesday.server.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.z100.valentuesday.api.exception.ApiError;
import com.z100.valentuesday.api.exception.ApiException;
import com.z100.valentuesday.server.Constants;
import com.z100.valentuesday.server.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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

		try {
			createSessionFrom(request.getHeader(AUTHORIZATION));
		} catch (Exception e) {
			response.setStatus(UNAUTHORIZED.value());
			response.getWriter().write(createErrorResponse(e.getMessage()));
		}

		filterChain.doFilter(request, response);
	}

	private void createSessionFrom(String authorizationHeader) {

		tokenUtil.validateBearer(authorizationHeader);

		DecodedJWT decodedJWT = tokenUtil.extractBearer.apply(authorizationHeader);

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

	private String createErrorResponse(String message) {
		ApiError error = new ApiError(
				message,
				UNAUTHORIZED,
				UNAUTHORIZED.value(),
				ZonedDateTime.now(ZoneId.of("Z"))
		);

		return error.toString();
	}
}