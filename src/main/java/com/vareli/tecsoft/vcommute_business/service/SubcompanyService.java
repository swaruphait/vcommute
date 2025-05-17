package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.SubCompany;

public interface SubcompanyService {

    ResponseEntity<?> createCompany(SubCompany company);

    ResponseEntity<?> findByAll();

    ResponseEntity<?> findByAllActive();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeCompany(Integer id);

    ResponseEntity<?> deactiveCompany(Integer id);
    
}
