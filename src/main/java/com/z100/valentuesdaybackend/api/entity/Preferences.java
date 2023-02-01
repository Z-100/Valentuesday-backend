package com.z100.valentuesdaybackend.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "preferences")
public class Preferences {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "dark_mode_enabled")
	private Boolean darkModeEnabled;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account", referencedColumnName = "id")
	private Account account;
}
