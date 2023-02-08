package com.z100.valentuesday.api.repository;

import com.z100.valentuesday.api.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {

	List<Question> findAllByAccountActivationKey(String activationKey);

	Optional<Question> findByIdAndAccountId(Long id, Long accountId);
}
