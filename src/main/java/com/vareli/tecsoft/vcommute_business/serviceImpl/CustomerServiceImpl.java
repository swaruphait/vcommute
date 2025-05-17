package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Customer;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.CustomerRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Override
    public ResponseEntity<?> createCustomer(Customer customer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (customer.getId() == null) {
            if (customer.getCompanyId() == null) {
                customer.setCompanyId(user.getCompanyId());
            }
            customer.setSuperCompanyId(user.getSuperCompanyId());
            boolean exists = customerRepository
                    .existsByNameAndBranchAreaAndLatitudeAndLongitudeAndSuperCompanyIdAndCompanyId(customer.getName(),
                            customer.getBranchArea(), customer.getLatitude(), customer.getLongitude(),
                            customer.getSuperCompanyId(), customer.getCompanyId());
            if (!exists) {
                customer.setStatus(true);
                customerRepository.save(customer);
                return ResponseEntity.status(HttpStatus.OK).body("Customer Successfully Saved....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Customer Saved....");
            }
        } else {
            customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.OK).body("Customer Successfully Updated....");
        }

    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Customer> collect = customerRepository.findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(),
                companyId);
                for (Customer customer : collect) {
                    customer.setLocation(cityRepository.findById(customer.getLocationId()).get().getCity());
                    if (customer.getCompanyId()==0) {
                        customer.setCompanyName(superCompanyRepository.findById(customer.getSuperCompanyId()).get().getName());
                    } else {
                        customer.setCompanyName(subCompanyRepository.findById(customer.getCompanyId()).get().getName());
                    }
                }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findByAllActive() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();

        List<Customer> collect = customerRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), user.getCompanyId()).stream()
                .filter(c -> c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong", HttpStatus.OK, null);
            
        }
    }

    @Override
    public ResponseEntity<?> activeCustomer(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            customerRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("Activated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Already Active....");
        }
    }

    @Override
    public ResponseEntity<?> deactiveCustomer(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            customerRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("DeActivated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Already DeActive....");
        }
    }

}
