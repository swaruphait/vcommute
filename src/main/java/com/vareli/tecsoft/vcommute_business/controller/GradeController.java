package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Grade;
import com.vareli.tecsoft.vcommute_business.service.GradeService;

@RestController
@RequestMapping("/grade")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;

    @PostMapping(value = "/createGrade")
	public ResponseEntity<?> createGrade(@RequestBody Grade grade) {
		return gradeService.createGrade(grade);
	}

     @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
		return gradeService.findByAll(companyId);
	}

	@GetMapping(value = "/findByAllActive")
	public ResponseEntity<?> findByAllActive(@RequestParam(required = false) Integer companyId) {
		return gradeService.findByAllActive(companyId);
	}

	@GetMapping(value = "/findById")
	public ResponseEntity<?> findById(@RequestParam Integer id) {
		return gradeService.findById(id);
	}

    @GetMapping(value = "/activeGrade")
	public ResponseEntity<?> activeGrade(@RequestParam Integer id) {
		return gradeService.activeGrade(id);
	}

    @GetMapping(value = "/deactiveGrade")
	public ResponseEntity<?> deactiveGrade(@RequestParam Integer id) {
		return gradeService.deactiveGrade(id);
	}
}
