package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Customer;

public interface CustomerService {

    ResponseEntity<?> createCustomer(Customer customer);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> activeCustomer(Long id);

    ResponseEntity<?> deactiveCustomer(Long id);
    
}
