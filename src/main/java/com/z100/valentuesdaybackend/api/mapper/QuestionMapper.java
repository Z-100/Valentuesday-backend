package com.z100.valentuesdaybackend.api.mapper;

import com.z100.valentuesdaybackend.api.dto.QuestionDTO;
import com.z100.valentuesdaybackend.api.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

	QuestionDTO toDTO(Question entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "account", ignore = true)
	Question toEntity(QuestionDTO dto);
}
