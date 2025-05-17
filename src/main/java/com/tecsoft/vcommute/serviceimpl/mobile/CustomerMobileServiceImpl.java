package com.tecsoft.vcommute.serviceimpl.mobile;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.Customer;
import com.tecsoft.vcommute.repository.CustomerRepository;
import com.tecsoft.vcommute.response.ResponseHandler;
import com.tecsoft.vcommute.service.mobile.CustomerMobileService;

@Service
public class CustomerMobileServiceImpl implements CustomerMobileService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<?> fetchCustomerWithLoc(Integer coId) {
        List<Customer> findCustomerWithLoc = customerRepository.findCustomerWithLoc().stream()
                .filter(t -> t.getEnabled().equals(true))
                .collect(Collectors.toList());
        return ResponseHandler.generateResponse("fatch Customer with location successfully!", HttpStatus.OK,
                findCustomerWithLoc);

    }

    @Override
    public ResponseEntity<?> fetchCustomer(Integer coId) {
        List<Customer> findAll = customerRepository.findAll().stream()
                .filter(t -> t.getEnabled().equals(true))
                .collect(Collectors.toList());
        return ResponseHandler.generateResponse("fatch Customer successfully!", HttpStatus.OK, findAll);

    }

    @Override
    public ResponseEntity<?> saveCustomer(Customer customer) {
        boolean existsByNameAndLocationIdAndBranchArea = customerRepository.existsByNameAndLocationIdAndBranchArea(
                customer.getName(), customer.getLocationId(), customer.getBranchArea());

        if (!existsByNameAndLocationIdAndBranchArea) {
            customer.setName(customer.getName().toUpperCase());
            customer.setEnabled(true);
            customer.setApproved(false);
        Customer save = customerRepository.save(customer);
        return ResponseHandler.generateResponse("Customer saved!!! !",
        HttpStatus.CREATED, save);
        } else {
            return ResponseHandler.generateResponse("Customer Already Present!!! !",
        HttpStatus.BAD_REQUEST, null);
        }
 

    }
}
