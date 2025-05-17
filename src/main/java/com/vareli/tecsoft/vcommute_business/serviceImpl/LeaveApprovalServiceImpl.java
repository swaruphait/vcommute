package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.LeaveData;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.LeaveDataRepository;
import com.vareli.tecsoft.vcommute_business.repository.LeaveRepository;
import com.vareli.tecsoft.vcommute_business.repository.UserRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.LeaveApprovalService;

@Service
public class LeaveApprovalServiceImpl implements LeaveApprovalService {

    @Autowired
    private LeaveDataRepository leaveDataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    @Override
    public ResponseEntity<?> fetchAprrovalList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();

        List<LeaveData> fetchApprovalLeaveAuthority = leaveDataRepository
                .fetchApprovalLeaveAuthority(user.getUser().getId(), user.getSuperCompanyId(), user.getCompanyId());
        for (LeaveData leaveData : fetchApprovalLeaveAuthority) {
            leaveData.setName(userRepository.findById(leaveData.getUserId()).get().getName());
            leaveData.setTypeOfLeave(leaveRepository.findById(leaveData.getLeaveTypeId()).get().getTypeName());
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchApprovalLeaveAuthority);

    }

    @Override
    public ResponseEntity<?> leaveApproval(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();

        Optional<LeaveData> byId = leaveDataRepository.findById(id);
        if (byId.isPresent()) {
            LeaveData leaveData = byId.get();
            leaveData.setStatus("A");
            leaveData.setApprovedBy(user.getUser().getId());
            leaveDataRepository.save(leaveData);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Leave Approved...");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Opps.. something went wrong.");
        }
    }

    @Override
    public ResponseEntity<?> rejectLeave(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();

        Optional<LeaveData> byId = leaveDataRepository.findById(id);
        if (byId.isPresent()) {
            LeaveData leaveData = byId.get();
            leaveData.setStatus("R");
            leaveData.setApprovedBy(user.getUser().getId());
            leaveDataRepository.save(leaveData);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Leave Approved...");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Opps.. something went wrong.");
        }
    }

    @Override
    public ResponseEntity<?> fetchLeaveAllData(String startDate, String endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<LeaveData> fetchLeaveData = leaveDataRepository.fetchLeaveData(startDate, endDate,
                user.getSuperCompanyId(), user.getCompanyId());
        for (LeaveData leaveData : fetchLeaveData) {
            leaveData.setName(userRepository.findById(leaveData.getUserId()).get().getName());
            leaveData.setTypeOfLeave(leaveRepository.findById(leaveData.getLeaveTypeId()).get().getTypeName());
            if (leaveData.getApprovedBy()!=null) {
                leaveData.setActivityBy(userRepository.findById(leaveData.getApprovedBy()).get().getName());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchLeaveData);

    }

    @Override
    public ResponseEntity<?> fetchLeaveAuthorityData(String startDate, String endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        List<LeaveData> fetchLeaveData = leaveDataRepository.fetchAuthorityLevaeData(startDate, endDate,
                user.getUser().getId(), user.getSuperCompanyId(), user.getCompanyId());
        for (LeaveData leaveData : fetchLeaveData) {
            leaveData.setName(userRepository.findById(leaveData.getUserId()).get().getName());
            leaveData.setTypeOfLeave(leaveRepository.findById(leaveData.getLeaveTypeId()).get().getTypeName());
            if (leaveData.getApprovedBy()!=null) {
                leaveData.setActivityBy(userRepository.findById(leaveData.getApprovedBy()).get().getName());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, fetchLeaveData);
    }

}
