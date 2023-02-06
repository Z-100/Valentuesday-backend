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
import com.z100.valentuesday.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

	private final AccountRepository accountRepository;

	private final AccountMapper accountMapper;

	private final PreferencesRepository preferencesRepository;

	private final QuestionRepository questionRepository;

	private final QuestionMapper questionMapper;

	@Transactional(rollbackFor = Throwable.class)
	public QuestionDTO create(QuestionDTO questionDTO) {

		Validator.notNull(questionDTO);

		Account account = getAccountFromSecurity();

		Question question = questionMapper.toEntity(questionDTO);
		question.setAccount(account);

		return questionMapper.toDTO(questionRepository.save(question));
	}

	public QuestionDTO get(String id) {
		Question byId = questionRepository.findById(Long.valueOf(id))
				.orElseThrow(() -> new RuntimeException("No question found with id: " + id));

		return questionMapper.toDTO(byId);
	}

	@Transactional(rollbackFor = Throwable.class)
	public QuestionDTO update(QuestionDTO dto) {
		Question update = questionMapper.toEntity(dto);

		Question fromDB = questionRepository.findById(dto.getId())
				.orElseThrow(() -> new RuntimeException("No question found with id: " + dto.getId()));
//TODO
		update.setAccount(fromDB.getAccount());

		return questionMapper.toDTO(questionRepository.save(update));
	}

	@Transactional(rollbackFor = Throwable.class)
	public Boolean delete(String id) {
		Question fromDB = questionRepository.findById(Long.valueOf(id))
				.orElseThrow(() -> new RuntimeException("No question found with id: " + id));

		questionRepository.delete(fromDB);

		return questionRepository.findById(Long.valueOf(id)).isEmpty();
	}

	@Transactional(rollbackFor = Throwable.class)
	public QuestionDTO getNextFor(String actKey) {
		List<QuestionDTO> questionDTOS = questionRepository.findAllByAccountActivationKey(actKey).stream()
				.map(questionMapper::toDTO)
				.toList();

		Long progress = getProgress(actKey);
		QuestionDTO questionDTO = questionDTOS.get(progress.intValue()); //TODO fix out of bounds

		updateProgress(actKey);

		return questionDTO;
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

	@Transactional(rollbackFor = Throwable.class)
	public Long updateProgress(String actKey) {
		Account account = accountRepository.findByActivationKey(actKey)
				.orElseThrow(() -> new ApiException("No account found with act-key: " + actKey));

		account.setQuestionProgress(account.getQuestionProgress() + 1);

		return accountRepository.save(account).getQuestionProgress();
	}

	@Transactional(rollbackFor = Throwable.class)
	public Long resetProgress(String actKey) {
		Account account = accountRepository.findByActivationKey(actKey)
				.orElseThrow(() -> new ApiException("No account found with act-key: " + actKey));

		account.setQuestionProgress(0L);

		return accountRepository.save(account).getQuestionProgress();
	}

	private Account getAccountFromSecurity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return accountRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ApiException("No user found with name " + authentication.getName()));
	}
}
