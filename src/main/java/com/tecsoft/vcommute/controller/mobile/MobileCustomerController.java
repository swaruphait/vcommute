package com.tecsoft.vcommute.controller.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.service.mobile.CustomerMobileService;


@RestController
@RequestMapping("/mobile")
public class MobileCustomerController {

    @Autowired
    private CustomerMobileService customerService;

    @GetMapping(value = "/fetchCustomerWithLoc")
	public ResponseEntity<?> fetchCustomerWithLoc(@RequestParam Integer coId) {
		return customerService.fetchCustomerWithLoc(coId);
	}

	@GetMapping(value = "/fetchCustomer")
	public ResponseEntity<?> fetchCustomer(@RequestParam Integer coId) {
		return customerService.fetchCustomer(coId);
	}

	@PostMapping("/saveCustomer")
	public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) throws Exception {
		return customerService.saveCustomer(customer);
	}
}
