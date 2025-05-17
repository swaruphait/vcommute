package com.tecsoft.vcommute.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.service.CustomerService;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/fetchCustomer")
    public ResponseEntity<?> fetchCustomer() {
        return customerService.fetchCustomer();
    }

    @GetMapping("/fetchCustomerByName")
    public ResponseEntity<?> fetchCustomerByName(String name) {
        return customerService.fetchCustomerByName(name);
    }

    @PostMapping(value = "/addCustomer", consumes = "application/json")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer, HttpServletRequest request)
            throws Exception {
        return customerService.addCustomer(customer);
    }

    @GetMapping(value = "/editcustomer")
    public ResponseEntity<?> editcustomer(@RequestParam Long id) {
        return customerService.editcustomer(id);
    }

    @GetMapping(value = "/deletecustomer")
    public ResponseEntity<?> deletecustomer(@RequestParam Long id) {
        return customerService.deletecustomer(id);
    }

    @GetMapping(value = "/getLatLongCutomer")
    public ResponseEntity<?> getLatLongCutomer(@RequestParam String address) {
        return customerService.getLatLongCutomer(address);
    }

}
