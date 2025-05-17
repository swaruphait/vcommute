package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Mode;

public interface ModeService {

    ResponseEntity<?> createMode(Mode mode);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive(Integer companyId);

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> activeMode(Long id);

    ResponseEntity<?> deactiveMode(Long id);
    
}
