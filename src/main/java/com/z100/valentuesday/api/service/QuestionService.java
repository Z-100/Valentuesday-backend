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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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
		Question byId = questionRepository.findByIdAndAccountId(Long.valueOf(id), getAccountFromSecurity().getId())
				.orElseThrow(() -> new RuntimeException("No question found with id: " + id));

		return questionMapper.toDTO(byId);
	}

	@Transactional(rollbackFor = Throwable.class)
	public QuestionDTO update(QuestionDTO dto) {
		Question update = questionMapper.toEntity(dto);

		Question fromDB = questionRepository.findByIdAndAccountId(update.getId(), getAccountFromSecurity().getId())
				.orElseThrow(() -> new RuntimeException("No question found with id: " + dto.getId()));

		questionMapper.updateEntity(update, fromDB);

		return questionMapper.toDTO(questionRepository.save(fromDB));
	}

	@Transactional(rollbackFor = Throwable.class)
	public Boolean delete(String id) {
		Question fromDB = questionRepository.findByIdAndAccountId(Long.valueOf(id), getAccountFromSecurity().getId())
				.orElseThrow(() -> new RuntimeException("No question found with id: " + id));

		questionRepository.delete(fromDB);

		return questionRepository.findById(Long.valueOf(id)).isEmpty();
	}

	@Transactional(rollbackFor = Throwable.class)
	public QuestionDTO getNextFor() {
		List<QuestionDTO> questionDTOS = questionRepository
				.findAllByAccountActivationKey(activationKeyFromSecurity()).stream()
				.map(questionMapper::toDTO)
				.toList();

		Long progress = getProgress();

		if (progress >= questionDTOS.size())
			throw new ApiException("Limit of questions reached", NO_CONTENT);

		QuestionDTO questionDTO = questionDTOS.get(progress.intValue());

		updateProgress();

		return questionDTO;
	}

	public List<QuestionDTO> getAllForActKey() {
		return questionRepository.findAllByAccountActivationKey(activationKeyFromSecurity()).stream()
				.map(questionMapper::toDTO)
				.toList();
	}

	public Long getProgress() {
		return findByActivationKey().getQuestionProgress();
	}

	@Transactional(rollbackFor = Throwable.class)
	public Long updateProgress() {

		Account account = findByActivationKey();

		account.setQuestionProgress(account.getQuestionProgress() + 1);

		return accountRepository.save(account).getQuestionProgress();
	}

	@Transactional(rollbackFor = Throwable.class)
	public Long resetProgress() {
		Account account = findByActivationKey();

		account.setQuestionProgress(0L);

		return accountRepository.save(account).getQuestionProgress();
	}

	private Account findByActivationKey() {
		String activationKey = activationKeyFromSecurity();
		return accountRepository.findByActivationKey(activationKey)
				.orElseThrow(() -> new ApiException("No account found with act-key: " + activationKey, NOT_FOUND));
	}

	private String activationKeyFromSecurity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (String) authentication.getCredentials();
	}

	private Account getAccountFromSecurity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return accountRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new ApiException("No user found with name " + authentication.getName(), NOT_FOUND));
	}
}
