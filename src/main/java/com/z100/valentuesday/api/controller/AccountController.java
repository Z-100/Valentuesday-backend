package com.z100.valentuesday.api.controller;

import com.z100.valentuesday.api.dto.AccountDTO;
import com.z100.valentuesday.api.dto.PreferencesDTO;
import com.z100.valentuesday.api.service.AccountService;
import com.z100.valentuesday.util.annotation.ForMobile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController//("/api/v1")
public class AccountController {

	private final AccountService accountService;

	@PostMapping("/account")
	public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO account) {
		return ResponseEntity.ok(accountService.createAccount(account));
	}

	@ForMobile
	@GetMapping("/account")
	public ResponseEntity<AccountDTO> checkActivationKey(@RequestHeader("activation-key") String actKey) {
		return ResponseEntity.ok(accountService.checkActivationKey(actKey));
	}

	@ForMobile
	@PutMapping("/preferences")
	public ResponseEntity<PreferencesDTO> updatePreferences(@RequestBody PreferencesDTO preferences) {
		return ResponseEntity.ok(accountService.updatePreferences(preferences));
	}

	@PutMapping("/role")
	public ResponseEntity<Boolean> grantAdminRights() {
		return ResponseEntity.ok(accountService.grantOrRevokeAdminRights(true));
	}

	@DeleteMapping("/role")
	public ResponseEntity<Boolean> revokeAdminRights() {
		return ResponseEntity.ok(accountService.grantOrRevokeAdminRights(false));
	}
}
