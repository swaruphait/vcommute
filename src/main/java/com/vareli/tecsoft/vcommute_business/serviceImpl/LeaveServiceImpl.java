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

import com.vareli.tecsoft.vcommute_business.model.Leave;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.LeaveRepository;
import com.vareli.tecsoft.vcommute_business.service.LeaveService;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Override
    public ResponseEntity<?> createLeave(Leave leave) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (leave.getId() == null) {
            if (leave.getCompanyId() == null) {
                leave.setCompanyId(user.getCompanyId());
            }
            leave.setSuperCompanyId(user.getSuperCompanyId());
            leave.setStatus(true);
            leaveRepository.save(leave);
            return ResponseEntity.status(HttpStatus.OK).body("Successfuly Created");
        } else {
            leaveRepository.save(leave);
            return ResponseEntity.status(HttpStatus.OK).body("Successfuly Updated");
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Leave> bySuperCompanyIdAndCompanyId = leaveRepository
                .findBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId);
        return ResponseEntity.status(HttpStatus.OK).body(bySuperCompanyIdAndCompanyId);

    }

    @Override
    public ResponseEntity<?> findByAllActive() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<Leave> collect = leaveRepository
                .findBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), user.getCompanyId()).stream().filter(t -> t.isStatus()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<Leave> byId = leaveRepository.findById(id);
        if (byId.isPresent()) {
          return ResponseEntity.status(HttpStatus.OK).body(byId);
        } else {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Override
    public ResponseEntity<?> activeLeave(Integer id) {
        Optional<Leave> byId = leaveRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            leaveRepository.save(byId.get());
          return ResponseEntity.status(HttpStatus.OK).body("Successfully Activate..");
        } else {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aleady Activate..");
        }
    }

    @Override
    public ResponseEntity<?> deactiveLeave(Integer id) {
        Optional<Leave> byId = leaveRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            leaveRepository.save(byId.get());
          return ResponseEntity.status(HttpStatus.OK).body("Successfully DeActivate..");
        } else {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aleady DeActivate..");
        }
    }

}
