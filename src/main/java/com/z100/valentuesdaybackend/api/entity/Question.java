package com.z100.valentuesdaybackend.api.entity;

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
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "question")
	private String question;

	@Column(name = "solution")
	private Long solution;

	@Column(name = "answer_one")
	private String answerOne;

	@Column(name = "answer_two")
	private String answerTwo;

	@Column(name = "answer_three")
	private String answerThree;

	@ManyToOne
	@JoinColumn(name = "account")
	private Account account;
}
