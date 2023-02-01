package com.z100.valentuesdaybackend.api.repository;

import com.z100.valentuesdaybackend.api.entity.Account;
import com.z100.valentuesdaybackend.api.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {}
