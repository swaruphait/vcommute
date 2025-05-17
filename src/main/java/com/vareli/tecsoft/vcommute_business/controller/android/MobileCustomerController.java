package com.vareli.tecsoft.vcommute_business.controller.android;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Customer;
import com.vareli.tecsoft.vcommute_business.service.android.MobileCustomerService;

@RestController
@RequestMapping("/android")
public class MobileCustomerController {

    @Autowired
    private MobileCustomerService mobileCustomerService;

    @GetMapping(value = "/fetchCustomer")
    public ResponseEntity<?> fetchCustomer(@RequestParam Integer companyId, @RequestParam Integer superCompanyId) {
        return mobileCustomerService.fetchCustomer(companyId, superCompanyId);
    }

    @GetMapping(value = "/fetchCustomerLocationWise")
    public ResponseEntity<?> fetchCustomerLocationWise(@RequestParam Integer companyId,
            @RequestParam Integer superCompanyId, @RequestParam Integer locationId) {
        return mobileCustomerService.fetchCustomerLocationWise(companyId, superCompanyId, locationId);
    }

    @PostMapping("/saveCustomer")
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) throws Exception {
        return mobileCustomerService.saveCustomer(customer);
    }
}
