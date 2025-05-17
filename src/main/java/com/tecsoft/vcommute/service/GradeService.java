package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.Grade;

public interface GradeService {

    ResponseEntity<?> addGrade(Grade grade);

    ResponseEntity<?> fetchGrade();

    ResponseEntity<?> editGrade(Long id);

    ResponseEntity<?> deleteGrade(Long id);

}
