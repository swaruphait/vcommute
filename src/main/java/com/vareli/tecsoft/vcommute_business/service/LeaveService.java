package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Leave;

public interface LeaveService {

    ResponseEntity<?> createLeave(Leave leave);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeLeave(Integer id);

    ResponseEntity<?> deactiveLeave(Integer id);
    
}
