package com.tecsoft.vcommute.service.mobile;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.Customer;

public interface CustomerMobileService {

    ResponseEntity<?> fetchCustomerWithLoc(Integer coId);

    ResponseEntity<?> fetchCustomer(Integer coId);

    ResponseEntity<?> saveCustomer(Customer customer);
}
