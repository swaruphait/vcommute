package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Grade;

public interface GradeService {

    ResponseEntity<?> createGrade(Grade grade);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive(Integer companyId);

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeGrade(Integer id);

    ResponseEntity<?> deactiveGrade(Integer id);
    
}
