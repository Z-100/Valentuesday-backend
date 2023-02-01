package com.z100.valentuesdaybackend.api.controller;

import com.z100.valentuesdaybackend.api.dto.AccountDTO;
import com.z100.valentuesdaybackend.api.dto.PreferencesDTO;
import com.z100.valentuesdaybackend.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController("/api/v1/account")
public class AccountController {

	private final AccountService accountService;

	@PostMapping
	public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO account) {
		return ResponseEntity.ok(accountService.createAccount(account));
	}

	@RequestMapping
	public ResponseEntity<AccountDTO> checkActivationKey(@RequestHeader("activation-key") String actKey) {
		return ResponseEntity.ok(accountService.checkActivationKey(actKey));
	}

	@PutMapping("/preferences")
	public ResponseEntity<PreferencesDTO> updatePreferences(PreferencesDTO preferences) {
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
