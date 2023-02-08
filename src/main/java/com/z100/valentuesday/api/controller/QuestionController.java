package com.z100.valentuesday.api.controller;

import com.z100.valentuesday.api.dto.QuestionDTO;
import com.z100.valentuesday.api.service.QuestionService;
import com.z100.valentuesday.util.annotation.ForMobile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;

	@Secured("ROLE_USER")
	@PostMapping("/question")
	public ResponseEntity<QuestionDTO> create(@RequestBody QuestionDTO preferences) {
		return ResponseEntity.ok(questionService.create(preferences));
	}

	@ForMobile
	@Secured("ROLE_USER")
	@GetMapping("/question/{id}")
	public ResponseEntity<QuestionDTO> get(@PathVariable("id") String id) {
		return ResponseEntity.ok(questionService.get(id));
	}

	@ForMobile
	@Secured("ROLE_USER")
	@PutMapping("/question")
	public ResponseEntity<QuestionDTO> update(@RequestBody QuestionDTO dto) {
		return ResponseEntity.ok(questionService.update(dto));
	}

	@Secured("ROLE_USER")
	@DeleteMapping("/question/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") String id) {
		return ResponseEntity.ok(questionService.delete(id));
	}

	@ForMobile
	@Secured("ROLE_USER")
	@GetMapping("/question/next-for")
	public ResponseEntity<QuestionDTO> getNextFor(@RequestHeader("activation-key") String actKey) {
		return ResponseEntity.ok(questionService.getNextFor(actKey));
	}

	@ForMobile
	@Secured("ROLE_USER")
	@GetMapping("/question/all-for-act-key")
	public ResponseEntity<List<QuestionDTO>> getAllForActKey(@RequestHeader("activation-key") String actKey) {
		return ResponseEntity.ok(questionService.getAllForActKey(actKey));
	}

	@ForMobile
	@Secured("ROLE_USER")
	@GetMapping("/question/progress")
	public ResponseEntity<Long> getProgress(@RequestHeader("activation-key") String actKey) {
		return ResponseEntity.ok(questionService.getProgress(actKey));
	}

	@ForMobile
	@Secured("ROLE_USER")
	@PutMapping("/question/progress")
	public ResponseEntity<Long> updateProgress(@RequestHeader("activation-key") String actKey) {
		return ResponseEntity.ok(questionService.updateProgress(actKey));
	}

	@ForMobile
	@Secured("ROLE_USER")
	@PatchMapping("/question/progress")
	public ResponseEntity<Long> resetProgress(@RequestHeader("activation-key") String actKey) {
		return ResponseEntity.ok(questionService.resetProgress(actKey));
	}
}
