package com.vareli.tecsoft.vcommute_business.service.android;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Customer;

public interface MobileCustomerService {

    ResponseEntity<?> fetchCustomer(Integer companyId, Integer superCompanyId);

    ResponseEntity<?> fetchCustomerLocationWise(Integer companyId, Integer superCompanyId, Integer locationId);

    ResponseEntity<?> saveCustomer(Customer customer);
    
}
