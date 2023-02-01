package com.z100.valentuesdaybackend.api.mapper;

import com.z100.valentuesdaybackend.api.dto.AccountDTO;
import com.z100.valentuesdaybackend.api.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { PreferencesMapper.class, QuestionMapper.class })
public interface AccountMapper {

	AccountDTO toDTO(Account entity);

	@Mapping(target = "id", ignore = true)
	Account toEntity(AccountDTO dto);
}
