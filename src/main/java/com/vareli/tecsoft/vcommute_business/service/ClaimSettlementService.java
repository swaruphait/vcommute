package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.CommuteCliamAmount;

public interface ClaimSettlementService {

    ResponseEntity<?> addAdvance(CommuteCliamAmount commuteCliamAmount);

    ResponseEntity<?> fetchAdavnce();

    ResponseEntity<?> fetchAdavnceById(Long id);
    
}
