package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Role;

public interface RoleService {

    ResponseEntity<?> createRole(Role role);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive(Integer companyId);

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeRole(Integer id);

    ResponseEntity<?> deactiveRole(Integer id);
    
}
