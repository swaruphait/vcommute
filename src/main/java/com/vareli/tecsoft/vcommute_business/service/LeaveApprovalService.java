package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

public interface LeaveApprovalService {

    ResponseEntity<?> leaveApproval(Long id);

    ResponseEntity<?> fetchAprrovalList();

    ResponseEntity<?> rejectLeave(Long id);

    ResponseEntity<?> fetchLeaveAllData(String startDate, String endDate);

    ResponseEntity<?> fetchLeaveAuthorityData(String startDate, String endDate);
    
}
