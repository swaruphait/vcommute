package com.vareli.tecsoft.vcommute_business.service.android;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.LeaveData;

public interface MobileLeaveService {

    ResponseEntity<?> leaveApply(LeaveData leaveData);

    ResponseEntity<?> fetchLeaveData(Long userId);

    ResponseEntity<?> fetchLeaveType(Long userId);

    ResponseEntity<?> fetchPendingLeave(Long userId);

    ResponseEntity<?> fetchLeaveHistory(Long userId);

    ResponseEntity<?> fetchLeaveBalance(Long userId);

    
}
