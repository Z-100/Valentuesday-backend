package com.z100.valentuesdaybackend.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDTO {

	private Long id;

	private String question;

	private Long solution;

	private String answerOne;

	private String answerTwo;

	private String answerThree;
}
