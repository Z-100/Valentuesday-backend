package com.z100.valentuesday.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "preferences")
public class Preferences {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column
	private Boolean darkModeEnabled;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account", referencedColumnName = "id")
	private Account account;

	public void enableDarkMode() {
		this.darkModeEnabled = true;
	}

	public void disableDarkMode() {
		this.darkModeEnabled = false;
	}
}
