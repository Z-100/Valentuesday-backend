package com.z100.valentuesday.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.z100.valentuesday.api.dto.JwtDTO;
import com.z100.valentuesday.api.entity.Account;
import com.z100.valentuesday.api.exception.ApiException;
import com.z100.valentuesday.server.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * Util to handle JWT tokens
 *
 * @author z-100
 */
@Component
public class JwtUtil {

	private Constants constants;

	public Function<String, DecodedJWT> extractBearer = authHeaderIn -> {
		if (authHeaderIn == null)
			throw new ApiException("Bearer token was null");

		String token = authHeaderIn.substring(constants.getTokenPrefix().length());
		JWTVerifier verifier = JWT.require(algorithm()).build();

		return verifier.verify(token);
	};

	public JwtUtil(Constants constants) {
		this.constants = constants;
	}

	public JwtDTO generateNewAccessToken(String subject, String issuer, List<?> roles) {
		String jwt = JWT.create().withSubject(subject)
				.withExpiresAt(new Date(System.currentTimeMillis() + constants.getExpiration().get("access_token")))
				.withIssuer(issuer)
				.withClaim("rls", roles)
				.sign(algorithm());

		return new JwtDTO(jwt);
	}

	public JwtDTO generateNewAccessToken(Account account) {

		String subject = account.getUsername();
		String issuer = "Refresh token";
		List<String> rolesClaim = account.getRoles().stream().toList();

		return generateNewAccessToken(subject, issuer, rolesClaim);
	}

	public String getUsernameFromToken(DecodedJWT token) {

		return token.getSubject();
	}

	public Date getExpirationDateFromToken(DecodedJWT token) {

		return token.getExpiresAt();
	}

	public void validateTokenExpiration(DecodedJWT token) {

		final Date expiration = getExpirationDateFromToken(token);

		if (new Date().after(expiration))
			throw new ApiException("Token expired. Please login again", UNAUTHORIZED);
	}

	public void validateBearer(String token) {

		if (token != null && !token.startsWith(constants.getTokenPrefix()))
			throw new ApiException("Invalid bearer token", UNAUTHORIZED);
	}

	public void addNewTokenToSecurity(Account account) {

		List<SimpleGrantedAuthority> authorities = account.getRoles().stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(account.getUsername(), null, authorities);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	private Map<String, Claim> getAllClaimsFromToken(DecodedJWT token) {

		return token.getClaims();
	}

	private Algorithm algorithm() {

		return Algorithm.HMAC512(constants.getSecret().getBytes(StandardCharsets.UTF_8));
	}
}
