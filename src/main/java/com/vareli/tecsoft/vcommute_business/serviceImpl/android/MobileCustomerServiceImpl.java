package com.vareli.tecsoft.vcommute_business.serviceImpl.android;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Customer;
import com.vareli.tecsoft.vcommute_business.model.User;
import com.vareli.tecsoft.vcommute_business.repository.CustomerRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.android.MobileCustomerService;

@Service
public class MobileCustomerServiceImpl implements MobileCustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> fetchCustomer(Integer companyId, Integer superCompanyId) {
        List<Customer> findAll = customerRepository.findAllBySuperCompanyIdAndCompanyId(superCompanyId, companyId);
        return ResponseHandler.generateResponse("Fetch Customer successfully!", HttpStatus.OK, findAll);
    }

    @Override
    public ResponseEntity<?> fetchCustomerLocationWise(Integer companyId, Integer superCompanyId, Integer locationId) {
        List<Customer> findAll = customerRepository.findAllBySuperCompanyIdAndCompanyIdAndLocationId(superCompanyId,
                companyId, locationId);
        return ResponseHandler.generateResponse("Fetch Customer successfully!", HttpStatus.OK, findAll);

    }

    @Override
    public ResponseEntity<?> saveCustomer(Customer customer) {
        Optional<User> byId = userRepository.findById(customer.getUserId());
        if (byId.isPresent()) {
            User user = byId.get();
            boolean exists = customerRepository.existsByNameAndLocationIdAndBranchAreaAndSuperCompanyIdAndCompanyId(
                    customer.getName(),
                    customer.getLocationId(), customer.getBranchArea(), customer.getSuperCompanyId(),
                    customer.getCompanyId());
            if (!exists) {
                customer.setCreatedBy(user.getUsername());
                customer.setStatus(true);
                customer.setApproved(false);
                customerRepository.save(customer);
                return ResponseHandler.generateResponse("Customer saved successfully", HttpStatus.OK, customer);
            } else {
                return ResponseHandler.generateResponse("Customer already exists", HttpStatus.BAD_REQUEST, null);
            }
        }else{
            return ResponseHandler.generateResponse("Opps.. something went wrong.", HttpStatus.BAD_REQUEST, null);
        }
    }

}
