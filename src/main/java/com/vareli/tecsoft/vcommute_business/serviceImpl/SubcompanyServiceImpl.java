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

import com.vareli.tecsoft.vcommute_business.model.SubCompany;
import com.vareli.tecsoft.vcommute_business.model.SuperCompany;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.CountryRepository;
import com.vareli.tecsoft.vcommute_business.repository.StateRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubscriptionDataRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.SubcompanyService;

@Service
public class SubcompanyServiceImpl implements SubcompanyService{

    @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Autowired
    private SubscriptionDataRepository subscriptionDataRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> createCompany(SubCompany company) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (company.getId() == null) {
            System.out.println();
            Optional<SuperCompany> byId = superCompanyRepository.findById(user.getSuperCompanyId());
            if (byId.isPresent()) {
                Integer fetchNoOfCompanyLimit = subscriptionDataRepository
                        .fetchNoOfCompanyLimit(user.getSuperCompanyId());
                Integer noOfCompanyPresent = subCompanyRepository.noOfCompanyPresent(user.getSuperCompanyId());
                if (fetchNoOfCompanyLimit > noOfCompanyPresent) {
                    company.setSuperCompany(byId.get());
                    company.setStatus(true);
                    subCompanyRepository.save(company);
                    return ResponseEntity.status(HttpStatus.OK).body("Successfully Saved...");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Max Limit Exist.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No Valid Supercompany present.");
            }
        } else {
            Optional<SubCompany> byId = subCompanyRepository.findById(company.getId());
            company.setSuperCompany(byId.get().getSuperCompany());
            subCompanyRepository.save(company);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Updated...");
        }
    }

    @Override
    public ResponseEntity<?> findByAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<SubCompany> fetchAll = subCompanyRepository.fetchAll(user.getSuperCompanyId());
        for (SubCompany subCompany : fetchAll) {
            subCompany.setCountry(countryRepository.findById(subCompany.getCountryId()).get().getName());
            subCompany.setState(stateRepository.findById(subCompany.getStateId()).get().getName());
            subCompany.setCity(cityRepository.findById(subCompany.getCityId()).get().getCity());
            subCompany.setSuperCompanyName(subCompany.getSuperCompany().getName());
        }
        return ResponseHandler.generateResponse("Successfully Updated...", HttpStatus.OK, fetchAll);
    }

    @Override
    public ResponseEntity<?> findByAllActive() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<SubCompany> fetchAll = subCompanyRepository.fetchAll(user.getSuperCompanyId()).stream().filter(t -> t.isStatus()).collect(Collectors.toList());
        for (SubCompany subCompany : fetchAll) {
            subCompany.setSuperCompanyName(subCompany.getSuperCompany().getName());
        }
        return ResponseHandler.generateResponse("Successfully Updated...", HttpStatus.OK, fetchAll);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<SubCompany> byId = subCompanyRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Updated...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Opps.. something went wrong.", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> activeCompany(Integer id) {
        Optional<SubCompany> byId = subCompanyRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            subCompanyRepository.save(byId.get());
            return ResponseHandler.generateResponse("Successfully Activated...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Already Active...", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> deactiveCompany(Integer id) {
        Optional<SubCompany> byId = subCompanyRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            subCompanyRepository.save(byId.get());
            return ResponseHandler.generateResponse("Successfully DeActivated...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Already DeActive...", HttpStatus.BAD_REQUEST, null);
        }
    }
    
}
