package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Role;
import com.vareli.tecsoft.vcommute_business.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @PostMapping(value = "/createRole")
	public ResponseEntity<?> createRole(@RequestBody Role role) {
		return roleService.createRole(role);
	}

     @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
		return roleService.findByAll(companyId);
	}

	@GetMapping(value = "/findByAllActive")
	public ResponseEntity<?> findByAllActive(@RequestParam(required = false) Integer companyId) {
		return roleService.findByAllActive(companyId);
	}

	@GetMapping(value = "/findById")
	public ResponseEntity<?> findById(@RequestParam Integer id) {
		return roleService.findById(id);
	}

    @GetMapping(value = "/activeRole")
	public ResponseEntity<?> activeRole(@RequestParam Integer id) {
		return roleService.activeRole(id);
	}

    @GetMapping(value = "/deactiveRole")
	public ResponseEntity<?> deactiveRole(@RequestParam Integer id) {
		return roleService.deactiveRole(id);
	}

}
