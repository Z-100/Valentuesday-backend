package com.z100.valentuesday.api.repository;

import com.z100.valentuesday.api.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	Optional<Account> findByUsername(String username);

	Optional<Account> findByUsernameOrActivationKey(String username, String activationKey);

	Optional<Account> findByActivationKey(String activationKey);

	Optional<Account> findByUsernameAndPassword(String username, String password);
}
