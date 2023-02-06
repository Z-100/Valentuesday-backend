package com.z100.valentuesday.api.service;

import com.z100.valentuesday.api.dto.AccountDTO;
import com.z100.valentuesday.api.dto.PreferencesDTO;
import com.z100.valentuesday.api.entity.Account;
import com.z100.valentuesday.api.entity.Preferences;
import com.z100.valentuesday.api.exception.ApiException;
import com.z100.valentuesday.api.mapper.AccountMapper;
import com.z100.valentuesday.api.mapper.PreferencesMapper;
import com.z100.valentuesday.api.repository.AccountRepository;
import com.z100.valentuesday.api.repository.PreferencesRepository;
import com.z100.valentuesday.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;

	private final AccountMapper accountMapper;

	private final PreferencesRepository preferencesRepository;

	private final PreferencesMapper preferencesMapper;

	@Transactional(rollbackFor = ApiException.class)
	public AccountDTO createAccount(AccountDTO accountDTO) {

		Validator.notNull(accountDTO);
		Validator.notNull(accountDTO.getUsername());
		Validator.notNull(accountDTO.getPassword());

		String activationKey = UUID.randomUUID().toString();

		checkUserAlreadyExists(accountDTO.getUsername(), activationKey);

		Account account = accountMapper.toEntity(accountDTO);
		account.setActivationKey(activationKey);

		Preferences preferences = new Preferences();
		preferences.setAccount(account);
		preferences.enableDarkMode();

		account.setPreferences(preferences);

		return accountMapper.toDTO(accountRepository.save(account));
	}

	public AccountDTO checkActivationKey(String actKey) {

		Validator.notNull(actKey);

		Account account = accountRepository.findByActivationKey(actKey)
				.orElseThrow(() -> new ApiException("No account found with actKey " + actKey));

		return accountMapper.toDTO(account);
	}

	@Transactional(rollbackFor = ApiException.class)
	public PreferencesDTO updatePreferences(PreferencesDTO preferencesDTO) {

		Validator.notNull(preferencesDTO);

		Account account = getAccountFromSecurity();

		preferencesMapper.updateEntity(account.getPreferences(), preferencesDTO);

		Account save = accountRepository.save(account);

		return preferencesMapper.toDTO(save.getPreferences());
	}

	@Transactional(rollbackFor = ApiException.class)
	public Boolean grantOrRevokeAdminRights(Boolean grantRights) {

		Account account = getAccountFromSecurity();

		if (grantRights)
			account.addRole("ROLE_ADMIN");
		else
			account.removeRole("ROLE_ADMIN");

		Account save = accountRepository.save(account);

		return save.getRoles() != null;
	}

	private void checkUserAlreadyExists(String username, String activationKey) {
		if (accountRepository.findByUsernameOrActivationKey(username, activationKey).isPresent())
			throw new ApiException("User already exists with username " + username);
	}

	private Account getAccountFromSecurity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return accountRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ApiException("No user found with name " + authentication.getName()));
	}
}
