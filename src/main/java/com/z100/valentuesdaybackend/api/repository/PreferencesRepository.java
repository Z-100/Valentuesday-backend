package com.z100.valentuesdaybackend.api.repository;

import com.z100.valentuesdaybackend.api.entity.Account;
import com.z100.valentuesdaybackend.api.entity.Preferences;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencesRepository extends CrudRepository<Preferences, Long> {}
