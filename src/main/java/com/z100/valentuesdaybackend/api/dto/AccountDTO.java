package com.z100.valentuesdaybackend.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AccountDTO {

	private String username;

	private String password;

	private String activationKey;

	private PreferencesDTO preferences;

	private Set<QuestionDTO> questions;
}
