package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.Mode;

public interface ModeService {

    ResponseEntity<?> fetchMode();

    ResponseEntity<?> addMode(Mode mode);

    ResponseEntity<?> editMode(Integer id);

    ResponseEntity<?> deleteMode(Integer id);

}
