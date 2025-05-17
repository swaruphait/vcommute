package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Designation;
import com.vareli.tecsoft.vcommute_business.service.DesignationService;

@RestController
@RequestMapping("/designation")
public class DesignationController {
    
    @Autowired
    private DesignationService designationService;

         @PostMapping(value = "/createDesgination")
	public ResponseEntity<?> createDesgination(@RequestBody Designation designation) {
		return designationService.createDesgination(designation);
	}

    @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
		return designationService.findByAll(companyId);
	}

	@GetMapping(value = "/findByAllActive")
	public ResponseEntity<?> findByAllActive(@RequestParam(required = false) Integer companyId) {
		return designationService.findByAllActive(companyId);
	}

	@GetMapping(value = "/findByDepartmentAll")
	public ResponseEntity<?> findByDepartmentAll(@RequestParam Integer deptId) {
		return designationService.findByDepartmentAll(deptId);
	}

	@GetMapping(value = "/findById")
	public ResponseEntity<?> findById(@RequestParam Integer id) {
		return designationService.findById(id);
	}

    @GetMapping(value = "/activeDesgination")
	public ResponseEntity<?> activeDesgination(@RequestParam Integer id) {
		return designationService.activeDesgination(id);
	}

    @GetMapping(value = "/deactiveDesgination")
	public ResponseEntity<?> deactiveDesgination(@RequestParam Integer id) {
		return designationService.deactiveDesgination(id);
	}
}
