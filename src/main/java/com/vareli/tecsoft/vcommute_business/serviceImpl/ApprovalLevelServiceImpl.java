package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.ApprovalDetails;
import com.vareli.tecsoft.vcommute_business.model.ApprovalHeader;
import com.vareli.tecsoft.vcommute_business.model.dto.AppvlDtlsDto;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.ApprovalDetailsRepository;
import com.vareli.tecsoft.vcommute_business.repository.ApprovalHeaderRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.service.ApprovalLevelService;

@Service
public class ApprovalLevelServiceImpl implements ApprovalLevelService {

    @Autowired
    private ApprovalHeaderRepository approvalHeaderRepository;

    @Autowired
    private ApprovalDetailsRepository approvalDetailsRepository;

    @Autowired
    private SubCompanyRepository subCompanyRepository;
           
    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> createAppvlLevel(ApprovalHeader approvalHeader) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (approvalHeader.getCompanyId()==null) {
            approvalHeader.setCompanyId(user.getCompanyId());
        }
        if (approvalHeader.getCompanyId() == null) {
            boolean existsBy = approvalHeaderRepository.existsByTitleAndSuperCompanyIdAndCompanyId(
                    approvalHeader.getTitle(), user.getSuperCompanyId(), approvalHeader.getCompanyId());
            if (!existsBy) {
                approvalHeader.setSuperCompanyId(user.getSuperCompanyId());
                approvalHeader.setStatus(true);
                ApprovalHeader save = approvalHeaderRepository.save(approvalHeader);
                for (AppvlDtlsDto details : approvalHeader.getDetails()) {
                    ApprovalDetails approvalDetails = new ApprovalDetails();
                    approvalDetails.setUserId(details.getUserId());
                    approvalDetails.setApvlLevel(details.getApvlLevel());
                    approvalDetails.setStatus(true);
                    approvalDetails.setApprovalHeader(save);
                    approvalDetailsRepository.save(approvalDetails);
                }
                return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Created...");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Present...");
            }
        } else {
            boolean existsBy = approvalHeaderRepository.existsByTitleAndSuperCompanyIdAndCompanyId(
                    approvalHeader.getTitle(), user.getSuperCompanyId(), approvalHeader.getCompanyId());
            if (!existsBy) {
                approvalHeader.setSuperCompanyId(user.getSuperCompanyId());
                approvalHeader.setCompanyId(approvalHeader.getCompanyId());
                approvalHeader.setStatus(true);
                ApprovalHeader save = approvalHeaderRepository.save(approvalHeader);
                for (AppvlDtlsDto details : approvalHeader.getDetails()) {
                    ApprovalDetails approvalDetails = new ApprovalDetails();
                    approvalDetails.setUserId(details.getUserId());
                    approvalDetails.setApvlLevel(details.getApvlLevel());
                    approvalDetails.setStatus(true);
                    approvalDetails.setApprovalHeader(save);
                    approvalDetailsRepository.save(approvalDetails);
                }
                return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Created...");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Present...");
            }
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<ApprovalHeader> data = approvalHeaderRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId);
                for (ApprovalHeader approvalHeader : data) {
                    if (approvalHeader.getCompanyId()==0) {
                        approvalHeader.setCompany(superCompanyRepository.findById(approvalHeader.getSuperCompanyId()).get().getName());
                    } else {
                        approvalHeader.setCompany(subCompanyRepository.findById(approvalHeader.getCompanyId()).get().getName());
                    }
                    for (ApprovalDetails details : approvalHeader.getApprovalDetails()) {
                        details.setName(userRepository.findById(details.getUserId()).get().getName());
                    }
                }
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @Override
    public ResponseEntity<?> findByAllActive(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<ApprovalHeader> collect = approvalHeaderRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId).stream()
                .filter(d -> d.isStatus()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<ApprovalHeader> byId = approvalHeaderRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(byId);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong...");

        }
    }

    @Override
    public ResponseEntity<?> activeAppvlLevel(Integer id) {
        Optional<ApprovalHeader> byId = approvalHeaderRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            approvalHeaderRepository.save(byId.get());
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Activated...");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Activated");
        }
    }

    @Override
    public ResponseEntity<?> deActiveAppvlLevel(Integer id) {
        Optional<ApprovalHeader> byId = approvalHeaderRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            approvalHeaderRepository.save(byId.get());
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Deactivated...");

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Deactivated");
        }
    }

    @Override
    public ResponseEntity<?> editAppvlLevel(ApprovalHeader approvalHeader) {
        Set<ApprovalDetails> approvalDetails = approvalHeader.getApprovalDetails();
        for (ApprovalDetails details : approvalDetails) {
          Optional<ApprovalHeader> byId = approvalHeaderRepository.findById(approvalHeader.getId());
          details.setApprovalHeader(byId.get());
          details.setStatus(true);
          approvalDetailsRepository.save(details);
        }
        approvalHeaderRepository.save(approvalHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Updated...");
    }

    @Override
    public ResponseEntity<?> activeAppvlDetails(Integer id) {
       Optional<ApprovalDetails> byId = approvalDetailsRepository.findById(id);
       if (!byId.get().isStatus()) {
        byId.get().setStatus(true);
        approvalDetailsRepository.save(byId.get());
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Activated...");
       }else{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Activated...");
       }
    }

    @Override
    public ResponseEntity<?> deActiveAppvlDetails(Integer id) {
        Optional<ApprovalDetails> byId = approvalDetailsRepository.findById(id);
        if (byId.get().isStatus()) {
         byId.get().setStatus(false);
         approvalDetailsRepository.save(byId.get());
         return ResponseEntity.status(HttpStatus.CREATED).body("Successfully DeActivated...");
        }else{
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already DeActivated...");
        }
    }

}
