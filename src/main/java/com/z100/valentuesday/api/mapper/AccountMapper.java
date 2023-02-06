package com.z100.valentuesday.api.mapper;

import com.z100.valentuesday.api.dto.AccountDTO;
import com.z100.valentuesday.api.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = { PreferencesMapper.class, QuestionMapper.class })
public abstract class AccountMapper {

	@Autowired
	private PasswordEncoder encoder;

	@Mapping(target = "password", ignore = true)
	@Mapping(target = "activationKey", ignore = true)
	public abstract AccountDTO toDTO(Account entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(source = "password", target = "password", qualifiedByName = "mapPassword")
	public abstract Account toEntity(AccountDTO dto);

	@Named("mapPassword")
	protected String mapPassword(String password) {
		return encoder.encode(password);
	}
}
