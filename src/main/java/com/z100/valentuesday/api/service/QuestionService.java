package com.z100.valentuesday.api.service;

import com.z100.valentuesday.api.dto.QuestionDTO;
import com.z100.valentuesday.api.entity.Account;
import com.z100.valentuesday.api.entity.Question;
import com.z100.valentuesday.api.exception.ApiException;
import com.z100.valentuesday.api.mapper.AccountMapper;
import com.z100.valentuesday.api.mapper.QuestionMapper;
import com.z100.valentuesday.api.repository.AccountRepository;
import com.z100.valentuesday.api.repository.PreferencesRepository;
import com.z100.valentuesday.api.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mapping.model.MappingInstantiationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

	private final AccountRepository accountRepository;

	private final AccountMapper accountMapper;

	private final PreferencesRepository preferencesRepository;

	private final QuestionRepository questionRepository;

	private final QuestionMapper questionMapper;

	public QuestionDTO create(QuestionDTO preferences) {
		return null;
	}

	public QuestionDTO get(String id) {
		Question byId = questionRepository.findById(Long.valueOf(id))
				.orElseThrow(() -> new RuntimeException("No question found with id: " + id));

		return questionMapper.toDTO(byId);
	}

	public QuestionDTO update(QuestionDTO dto) {
		Question update = questionMapper.toEntity(dto);

		Question fromDB = questionRepository.findById(dto.getId())
				.orElseThrow(() -> new RuntimeException("No question found with id: " + dto.getId()));

		update.setAccount(fromDB.getAccount());

		Question save = questionRepository.save(update);

		return questionMapper.toDTO(save);
	}

	public Boolean delete(String id) {
		Question fromDB = questionRepository.findById(Long.valueOf(id))
				.orElseThrow(() -> new RuntimeException("No question found with id: " + id));

		questionRepository.delete(fromDB);

		return questionRepository.findById(Long.valueOf(id)).isEmpty();
	}

	public QuestionDTO getNextFor(String actKey) {
		List<QuestionDTO> questionDTOS = questionRepository
				.findAllByAccountActivationKey(actKey).stream()
				.map(questionMapper::toDTO)
				.toList();

		Long progress = getProgress(actKey);

		return questionDTOS.get(progress.intValue());
	}

	public List<QuestionDTO> getAllForActKey(String actKey) {
		return questionRepository.findAllByAccountActivationKey(actKey).stream()
				.map(questionMapper::toDTO)
				.toList();
	}

	public Long getProgress(String actKey) {
		return accountRepository.findByActivationKey(actKey)
				.orElseThrow(() -> new ApiException("No account found with act-key: " + actKey))
				.getQuestionProgress();
	}

	public Long updateProgress(String actKey) {
		Account account = accountRepository.findByActivationKey(actKey)
				.orElseThrow(() -> new ApiException("No account found with act-key: " + actKey));

		account.setQuestionProgress(account.getQuestionProgress() + 1);

		return accountRepository.save(account).getQuestionProgress();
	}
}
