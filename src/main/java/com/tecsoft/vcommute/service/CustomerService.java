package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.Customer;

public interface CustomerService {

    ResponseEntity<?> fetchCustomer();

    ResponseEntity<?> addCustomer(Customer customer);

    ResponseEntity<?> editcustomer(Long id);

    ResponseEntity<?> deletecustomer(Long id);

    ResponseEntity<?> getLatLongCutomer(String address);

    ResponseEntity<?> fetchCustomerByName(String name);

}
