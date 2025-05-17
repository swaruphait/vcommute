package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Department;
import com.vareli.tecsoft.vcommute_business.service.DepartmentService;

@RestController
@RequestMapping("/department")
public class DepartmenController {
    
    @Autowired
    private DepartmentService departmentService;

      @PostMapping(value = "/createDepartment")
	public ResponseEntity<?> createDepartment(@RequestBody Department department) {
		return departmentService.createDepartment(department);
	}

    @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
		return departmentService.findByAll(companyId);
	}

	@GetMapping(value = "/findByAllActive")
	public ResponseEntity<?> findByAllActive(@RequestParam(required = false) Integer companyId) {
		return departmentService.findByAllActive(companyId);
	}

	@GetMapping(value = "/findById")
	public ResponseEntity<?> findById(@RequestParam Integer id) {
		return departmentService.findById(id);
	}

    @GetMapping(value = "/activeDepartment")
	public ResponseEntity<?> activeDepartment(@RequestParam Integer id) {
		return departmentService.activeDepartment(id);
	}

    @GetMapping(value = "/deactiveDepartment")
	public ResponseEntity<?> deactiveDepartment(@RequestParam Integer id) {
		return departmentService.deactiveDepartment(id);
	}


}
