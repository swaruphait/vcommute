package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Customer;
import com.vareli.tecsoft.vcommute_business.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/createCustomer")
	public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
		return customerService.createCustomer(customer);
	}

    @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
		return customerService.findByAll(companyId);
	}

	@GetMapping(value = "/findByAllActive")
	public ResponseEntity<?> findByAllActive() {
		return customerService.findByAllActive();
	}

	@GetMapping(value = "/findById")
	public ResponseEntity<?> findById(@RequestParam Long id) {
		return customerService.findById(id);
	}

    @GetMapping(value = "/activeCustomer")
	public ResponseEntity<?> activeCustomer(@RequestParam Long id) {
		return customerService.activeCustomer(id);
	}

    @GetMapping(value = "/deactiveCustomer")
	public ResponseEntity<?> deactiveCustomer(@RequestParam Long id) {
		return customerService.deactiveCustomer(id);
	}

}
