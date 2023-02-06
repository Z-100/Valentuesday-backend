package com.z100.valentuesday.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private String activationKey;

	@Column
	private Long questionProgress = 0L;

	@OneToOne(cascade = CascadeType.ALL,
			mappedBy = "account")
	private Preferences preferences;

	@OneToMany(cascade = CascadeType.ALL,
			orphanRemoval = true,
			mappedBy = "account")
	private Set<Question> questions = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> roles = Set.of("ROLE_USER");

	public void addRole(String name) {
		if (roles.contains(name)) return;
		roles.add(name);
	}

	public void removeRole(String name) {
		roles.removeIf(role -> role.equals(name));
	}
}
