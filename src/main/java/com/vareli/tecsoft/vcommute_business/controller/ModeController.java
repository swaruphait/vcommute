package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Mode;
import com.vareli.tecsoft.vcommute_business.service.ModeService;

@RestController
@RequestMapping("/mode")
public class ModeController {
    
     @Autowired
    private ModeService modeService;

    @PostMapping(value = "/createMode")
	public ResponseEntity<?> createMode(@RequestBody Mode mode) {
		return modeService.createMode(mode);
	}

     @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
		return modeService.findByAll(companyId);
	}

	@GetMapping(value = "/findByAllActive")
	public ResponseEntity<?> findByAllActive(@RequestParam(required = false) Integer companyId) {
		return modeService.findByAllActive(companyId);
	}

	@GetMapping(value = "/findById")
	public ResponseEntity<?> findById(@RequestParam Long id) {
		return modeService.findById(id);
	}

    @GetMapping(value = "/activeMode")
	public ResponseEntity<?> activeMode(@RequestParam Long id) {
		return modeService.activeMode(id);
	}

    @GetMapping(value = "/deactiveMode")
	public ResponseEntity<?> deactiveMode(@RequestParam Long id) {
		return modeService.deactiveMode(id);
	}

}
