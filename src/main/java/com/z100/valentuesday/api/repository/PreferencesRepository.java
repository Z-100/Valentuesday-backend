package com.z100.valentuesday.api.repository;

import com.z100.valentuesday.api.entity.Preferences;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencesRepository extends CrudRepository<Preferences, Long> {}
