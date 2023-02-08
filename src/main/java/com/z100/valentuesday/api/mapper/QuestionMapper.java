package com.z100.valentuesday.api.mapper;

import com.z100.valentuesday.api.dto.QuestionDTO;
import com.z100.valentuesday.api.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

	QuestionDTO toDTO(Question entity);

	@Mapping(target = "account", ignore = true)
	Question toEntity(QuestionDTO dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "account", ignore = true)
	void updateEntity(Question source, @MappingTarget Question target);
}
