package com.vareli.tecsoft.vcommute_business.service.android;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Attendance;

public interface MobileAttendanceService {

    ResponseEntity<?> startAttandance(Attendance attendance);

    ResponseEntity<?> stopAttandance(Attendance attendance);

    ResponseEntity<?> fetchAttandanceData(Long userId);

    ResponseEntity<?> fetchUnstoppedAttandanceData(Long userId);
    
}
