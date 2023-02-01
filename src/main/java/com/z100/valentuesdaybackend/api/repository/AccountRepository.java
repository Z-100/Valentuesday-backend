package com.z100.valentuesdaybackend.api.repository;

import com.z100.valentuesdaybackend.api.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	Optional<Account> findByUsername(String username);

	Optional<Account> findByActivationKey(String activationKey);
}
