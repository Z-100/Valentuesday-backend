package com.z100.valentuesday.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "question")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column
	private String question;

	@Column
	private Long solution;

	@Column
	private String answerOne;

	@Column
	private String answerTwo;

	@Column
	private String answerThree;

	@ManyToOne
	@JoinColumn(name = "account")
	private Account account;
}
