package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.SuperCompany;

public interface CompanyService {

    ResponseEntity<?> createSuperCompany(SuperCompany company);

    ResponseEntity<?> editSuperCompany(SuperCompany company);

    ResponseEntity<?> findByAllActiveList();

    ResponseEntity<?> findByAll();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeCompany(Integer id);

    ResponseEntity<?> deactiveCompany(Integer id);


}
