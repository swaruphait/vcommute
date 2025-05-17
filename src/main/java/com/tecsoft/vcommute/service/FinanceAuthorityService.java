package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.FinanceAuthority;

public interface FinanceAuthorityService {

    ResponseEntity<?> fetchFinanceAuthority();

    ResponseEntity<?> addFinanceAuthority(FinanceAuthority financeAuthority);

    ResponseEntity<?> editFinanceAuthority(Integer id);

    ResponseEntity<?> deleteFinanceAuthority(Integer id);

    ResponseEntity<?> fetchAllFinaceRole();

}
