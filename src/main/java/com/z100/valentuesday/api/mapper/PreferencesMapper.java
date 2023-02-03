package com.z100.valentuesday.api.mapper;

import com.z100.valentuesday.api.dto.PreferencesDTO;
import com.z100.valentuesday.api.entity.Preferences;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PreferencesMapper {

	PreferencesDTO toDTO(Preferences entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "account", ignore = true)
	Preferences toEntity(PreferencesDTO dto);
}
