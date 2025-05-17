package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.SubcriptionPlan;
import com.vareli.tecsoft.vcommute_business.model.SubscriptionData;
import com.vareli.tecsoft.vcommute_business.model.SuperCompany;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.CountryRepository;
import com.vareli.tecsoft.vcommute_business.repository.StateRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubcriptionPlanRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubscriptionDataRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Autowired
    private SubcriptionPlanRepository subcriptionPlanRepository;

    @Autowired
    private SubscriptionDataRepository subscriptionDataRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> createSuperCompany(SuperCompany company) {
        boolean existsByNameAndEmail = superCompanyRepository.existsByNameAndEmail(company.getName(),
                company.getEmail());
        if (!existsByNameAndEmail) {
            company.setStatus(true);
            SuperCompany save = superCompanyRepository.save(company);
            Optional<SubcriptionPlan> byId = subcriptionPlanRepository.findById(company.getPlanId());
            if (byId.isPresent()) {
                SubcriptionPlan subcriptionPlan = byId.get();
                SubscriptionData data = new SubscriptionData();
                data.setAddNoOfCompany(subcriptionPlan.getAddNoOfCompany());
                data.setAddNoOfUser(subcriptionPlan.getAddNoOfUser());
                data.setCommuteAccess(subcriptionPlan.isCommuteAccess());
                data.setBiometricAttend(subcriptionPlan.isBiometricAttend());
                data.setAttendaceAccess(subcriptionPlan.isAttendaceAccess());
                data.setLeaveAccess(subcriptionPlan.isLeaveAccess());
                data.setFinanceAccess(subcriptionPlan.isFinanceAccess());
                data.setStartDate(LocalDate.now());
                data.setEndDate(LocalDate.now().plusDays(subcriptionPlan.getValidity()));
                data.setPlanName(subcriptionPlan.getPlanName());
                data.setPlanId(company.getPlanId());
                data.setPurchaseDate(LocalDate.now());
                data.setValidity(subcriptionPlan.getValidity());
                data.setSuperCompanyId(save.getId());
                data.setStatus(true);
                subscriptionDataRepository.save(data);
                return ResponseHandler.generateResponse("Successfully Saved...", HttpStatus.OK, null);

            } else {
                return ResponseHandler.generateResponse("Opps.. Something went wrong..", HttpStatus.BAD_REQUEST, null);
            }
        } else {
            return ResponseHandler.generateResponse("Opps.. Company Already Present..", HttpStatus.BAD_REQUEST, null);
        }

    }

    @Override
    public ResponseEntity<?> editSuperCompany(SuperCompany company) {
        superCompanyRepository.save(company);
        Optional<SubscriptionData> byStatusAndSuperCompanyId = subscriptionDataRepository
                .findByStatusAndSuperCompanyId(true, company.getId());
        boolean existsByStatusAndSuperCompanyId = subscriptionDataRepository.existsByStatusAndSuperCompanyId(true,
                company.getId());
        if (existsByStatusAndSuperCompanyId) {
            if (company.getPlanId().equals(byStatusAndSuperCompanyId.get().getPlanId())) {
                return ResponseHandler.generateResponse("Successfully Updated...", HttpStatus.OK, null);
            } else {
                Optional<SubscriptionData> byStatusAndSuperCompanyId2 = subscriptionDataRepository
                        .findByStatusAndSuperCompanyId(true, company.getId());
                byStatusAndSuperCompanyId2.get().setStatus(false);
                subscriptionDataRepository.save(byStatusAndSuperCompanyId2.get());
                Optional<SubcriptionPlan> byId = subcriptionPlanRepository.findById(company.getPlanId());
                SubcriptionPlan subcriptionPlan = byId.get();
                SubscriptionData data = new SubscriptionData();
                data.setAddNoOfCompany(subcriptionPlan.getAddNoOfCompany());
                data.setAddNoOfUser(subcriptionPlan.getAddNoOfUser());
                data.setCommuteAccess(subcriptionPlan.isCommuteAccess());
                data.setBiometricAttend(subcriptionPlan.isBiometricAttend());
                data.setAttendaceAccess(subcriptionPlan.isAttendaceAccess());
                data.setLeaveAccess(subcriptionPlan.isLeaveAccess());
                data.setFinanceAccess(subcriptionPlan.isFinanceAccess());
                data.setStartDate(LocalDate.now());
                data.setEndDate(LocalDate.now().plusDays(subcriptionPlan.getValidity()));
                data.setPlanName(subcriptionPlan.getPlanName());
                data.setPlanId(company.getPlanId());
                data.setPurchaseDate(LocalDate.now());
                data.setValidity(subcriptionPlan.getValidity());
                data.setSuperCompanyId(company.getId());
                data.setStatus(true);
                subscriptionDataRepository.save(data);
                return ResponseHandler.generateResponse("Successfully Updated With Plan...", HttpStatus.OK, null);
            }
        } else {
            Optional<SubcriptionPlan> byId = subcriptionPlanRepository.findById(company.getPlanId());
            SubcriptionPlan subcriptionPlan = byId.get();
            SubscriptionData data = new SubscriptionData();
            data.setAddNoOfCompany(subcriptionPlan.getAddNoOfCompany());
            data.setAddNoOfUser(subcriptionPlan.getAddNoOfUser());
            data.setCommuteAccess(subcriptionPlan.isCommuteAccess());
            data.setBiometricAttend(subcriptionPlan.isBiometricAttend());
            data.setAttendaceAccess(subcriptionPlan.isAttendaceAccess());
            data.setLeaveAccess(subcriptionPlan.isLeaveAccess());
            data.setFinanceAccess(subcriptionPlan.isFinanceAccess());
            data.setStartDate(LocalDate.now());
            data.setEndDate(LocalDate.now().plusDays(subcriptionPlan.getValidity()));
            data.setPlanName(subcriptionPlan.getPlanName());
            data.setPlanId(company.getPlanId());
            data.setPurchaseDate(LocalDate.now());
            data.setValidity(subcriptionPlan.getValidity());
            data.setSuperCompanyId(company.getId());
            data.setStatus(true);
            subscriptionDataRepository.save(data);
            return ResponseHandler.generateResponse("Successfully Updated With Plan...", HttpStatus.OK, null);
        }
    }

    @Override
    public ResponseEntity<?> findByAllActiveList() {
        List<SuperCompany> collect = superCompanyRepository.findAll().stream().filter(c -> c.isStatus())
                .collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched Company List...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findByAll() {
        List<SuperCompany> collect = superCompanyRepository.findAll();
        for (SuperCompany superCompany : collect) {
            Optional<SubscriptionData> byStatusAndSuperCompanyId = subscriptionDataRepository
                    .findByStatusAndSuperCompanyId(true, superCompany.getId());
            if (byStatusAndSuperCompanyId.isPresent()) {
                superCompany.setSubscriptionData(byStatusAndSuperCompanyId.get());
            }

            superCompany.setCountry(countryRepository.findById(superCompany.getCountryId()).get().getName());
            superCompany.setState(stateRepository.findById(superCompany.getStateId()).get().getName());
            superCompany.setCity(cityRepository.findById(superCompany.getCityId()).get().getCity());
        }
        return ResponseHandler.generateResponse("Successfully Fetched Company List...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<SuperCompany> byId = superCompanyRepository.findById(id);
        if (byId.isPresent()) {
            Optional<SubscriptionData> byStatusAndSuperCompanyId = subscriptionDataRepository
                    .findByStatusAndSuperCompanyId(true, byId.get().getId());
            if (byStatusAndSuperCompanyId.isPresent()) {
                SubscriptionData subscriptionData = byStatusAndSuperCompanyId.get();
                byId.get().setPlanId(subscriptionData.getPlanId());
            }

            return ResponseHandler.generateResponse("Successfully Fetched Company details.", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("opps... something went wrong.", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> activeCompany(Integer id) {
        Optional<SuperCompany> byId = superCompanyRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            superCompanyRepository.save(byId.get());
            return ResponseHandler.generateResponse("Successfully Active.", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Company alraedy Active.", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Override
    public ResponseEntity<?> deactiveCompany(Integer id) {
        Optional<SuperCompany> byId = superCompanyRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            superCompanyRepository.save(byId.get());
            return ResponseHandler.generateResponse("Successfully Deactive.", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Company alraedy DeActive.", HttpStatus.BAD_REQUEST, null);
        }
    }


}
