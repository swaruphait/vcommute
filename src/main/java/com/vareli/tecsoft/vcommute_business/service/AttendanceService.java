package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

public interface AttendanceService {

    ResponseEntity<?> findByAll(String stdate, String enddate);

    ResponseEntity<?> fetchAttendLevelAuthority(String stdate, String enddate);
    
}
