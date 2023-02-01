package com.z100.valentuesdaybackend.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "activation_key")
	private String activationKey;

	@OneToOne(mappedBy = "account")
	private Preferences preferences;

	@OneToMany(cascade = {CascadeType.ALL},
			orphanRemoval = true,
			mappedBy = "account")
	private Set<Question> questions = Set.of();

	@ElementCollection
	private Set<String> roles = Set.of("ROLE_USER");

	public void addRole(String name) {
		if (roles.contains(name))
			return;
		roles.add(name);
	}

	public void removeRole(String name) {
		roles.forEach(role -> {
			if (role.equals(name)) role = null;
		});
	}
}
