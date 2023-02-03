package com.z100.valentuesday.api.controller;

import com.z100.valentuesday.api.dto.PreferencesDTO;
import com.z100.valentuesday.api.dto.QuestionDTO;
import com.z100.valentuesday.api.service.QuestionService;
import com.z100.valentuesday.util.annotation.ForMobile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/question")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;

	@PostMapping
	public ResponseEntity<QuestionDTO> create(@RequestBody QuestionDTO preferences) {
		return ResponseEntity.ok(questionService.create(preferences));
	}

	@ForMobile
	@GetMapping("/{id}")
	public ResponseEntity<QuestionDTO> get(@PathVariable("id") String id) {
		return ResponseEntity.ok(questionService.get(id));
	}

	@ForMobile
	@PutMapping
	public ResponseEntity<QuestionDTO> update(@RequestBody QuestionDTO dto) {
		return ResponseEntity.ok(questionService.update(dto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
		return ResponseEntity.ok(questionService.delete(id));
	}

	@ForMobile
	@GetMapping("/next-for/{act-key}")
	public ResponseEntity<QuestionDTO> getNextFor(@PathVariable("act-key") String actKey) {
		return ResponseEntity.ok(questionService.getNextFor(actKey));
	}

	@ForMobile
	@GetMapping("/all-for-act-key/{act-key}")
	public ResponseEntity<List<QuestionDTO>> getAllForActKey(@PathVariable("act-key") String actKey) {
		return ResponseEntity.ok(questionService.getAllForActKey(actKey));
	}

	@ForMobile
	@GetMapping("/progress/{act-key}")
	public ResponseEntity<Long> getProgress(@PathVariable("act-key") String actKey) {
		return ResponseEntity.ok(questionService.getProgress(actKey));
	}

	@ForMobile
	@PutMapping("/progress/{act-key}")
	public ResponseEntity<Long> updateProgress(@PathVariable("act-key") String actKey) {
		return ResponseEntity.ok(questionService.updateProgress(actKey));
	}
}
