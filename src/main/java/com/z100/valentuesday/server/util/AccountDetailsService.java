package com.z100.valentuesday.server.util;

import com.z100.valentuesday.api.entity.Account;
import com.z100.valentuesday.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

	private final AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = accountRepository.findByUsername(username)
				.orElseThrow(() -> new AuthenticationServiceException("No user found"));

		List<SimpleGrantedAuthority> authorities = account.getRoles().stream()
				.map(SimpleGrantedAuthority::new)
				.toList();

		return new User(account.getUsername(), account.getPassword(), authorities);
	}
}
