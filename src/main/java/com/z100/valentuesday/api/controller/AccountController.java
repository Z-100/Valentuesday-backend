package com.z100.valentuesday.api.controller;

import com.z100.valentuesday.api.dto.AccountDTO;
import com.z100.valentuesday.api.dto.PreferencesDTO;
import com.z100.valentuesday.api.service.AccountService;
import com.z100.valentuesday.util.annotation.ForMobile;
import jakarta.annotation.security.PermitAll;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController("/api/v1")
public class AccountController {

	private final AccountService accountService;

	@Secured("ROLE_ADMIN")
	@GetMapping("/account/register")
	public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO account) {
		return ResponseEntity.ok(accountService.createAccount(account));
	}

	@ForMobile
	@PermitAll
	@GetMapping("/check-act-key")
	public ResponseEntity<AccountDTO> checkActivationKey(@RequestHeader("activation-key") String actKey) {
		return ResponseEntity.ok(accountService.checkActivationKey(actKey));
	}

	@ForMobile
	@Secured("ROLE_USER")
	@GetMapping("/update-preferences")
	public ResponseEntity<PreferencesDTO> updatePreferences(@RequestBody PreferencesDTO preferences) {
		return ResponseEntity.ok(accountService.updatePreferences(preferences));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/grant-role")
	public ResponseEntity<Boolean> grantAdminRights() {
		return ResponseEntity.ok(accountService.grantOrRevokeAdminRights(true));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/revoke-role")
	public ResponseEntity<Boolean> revokeAdminRights() {
		return ResponseEntity.ok(accountService.grantOrRevokeAdminRights(false));
	}
}
