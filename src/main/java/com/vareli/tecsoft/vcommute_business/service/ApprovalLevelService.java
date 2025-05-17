package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.ApprovalHeader;

public interface ApprovalLevelService {

    ResponseEntity<?> createAppvlLevel(ApprovalHeader approvalHeader);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive(Integer companyId);

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeAppvlLevel(Integer id);

    ResponseEntity<?> deActiveAppvlLevel(Integer id);

    ResponseEntity<?> editAppvlLevel(ApprovalHeader approvalHeader);

    ResponseEntity<?> activeAppvlDetails(Integer id);

    ResponseEntity<?> deActiveAppvlDetails(Integer id);
    
}
