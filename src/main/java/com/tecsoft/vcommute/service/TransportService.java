package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.LevelDescriptions;

public interface TransportService {

    ResponseEntity<?> addLevelDes(LevelDescriptions levelDescriptions);

    ResponseEntity<?> fetchLevelDescriptions();

    ResponseEntity<?> editLevelDescriptions(Long id);

    ResponseEntity<?> deleteLevelDescriptions(Long id);

}
