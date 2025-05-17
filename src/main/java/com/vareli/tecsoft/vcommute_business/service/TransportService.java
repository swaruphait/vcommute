package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Transport;

public interface TransportService {

    ResponseEntity<?> createTransport(Transport transport);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive(Integer companyId);

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeTransport(Integer id);

    ResponseEntity<?> deactiveTransport(Integer id);
    
}
