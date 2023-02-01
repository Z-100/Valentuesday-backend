package com.z100.valentuesdaybackend.api.service;

import com.z100.valentuesdaybackend.api.dto.AccountDTO;
import com.z100.valentuesdaybackend.api.dto.PreferencesDTO;
import com.z100.valentuesdaybackend.api.entity.Account;
import com.z100.valentuesdaybackend.api.entity.Preferences;
import com.z100.valentuesdaybackend.api.exception.ApiException;
import com.z100.valentuesdaybackend.api.mapper.AccountMapper;
import com.z100.valentuesdaybackend.api.repository.AccountRepository;
import com.z100.valentuesdaybackend.api.repository.PreferencesRepository;
import com.z100.valentuesdaybackend.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;

	private final AccountMapper accountMapper;

	private final PreferencesRepository preferencesRepository;


	public AccountDTO createAccount(AccountDTO accountDTO) {

		Validator.validate(accountDTO);
		Validator.validate(accountDTO.getUsername());
		Validator.validate(accountDTO.getPassword());

		Account account = accountMapper.toEntity(accountDTO);
		account.setActivationKey(UUID.randomUUID().toString());
		account.setPreferences(new Preferences());

		return accountMapper.toDTO(accountRepository.save(account));
	}

	public AccountDTO checkActivationKey(String actKey) {

		Validator.validate(actKey);

		Account account = accountRepository.findByActivationKey(actKey)
				.orElseThrow(() -> new ApiException("No account found with actKey " + actKey));

		return accountMapper.toDTO(account);
	}

	public PreferencesDTO updatePreferences(PreferencesDTO preferences) {
		return null;
	}

	public Boolean grantOrRevokeAdminRights(Boolean grantRights) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Account account = accountRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ApiException("No user found with name " + authentication.getName()));

		if (grantRights)
			account.addRole("ROLE_ADMIN");
		else
			account.removeRole("ROLE_ADMIN");

		Account save = accountRepository.save(account);

		return save.getRoles() != null;
	}
}
