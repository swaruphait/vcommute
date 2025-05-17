package com.tecsoft.vcommute.service.mobile;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.AttendanceData;

public interface AttendanceMobileService {

    ResponseEntity<Object> startAttandance(AttendanceData attendanceData);

    ResponseEntity<Object> stopAttandance(AttendanceData attendanceData);

    ResponseEntity<Object> fetchAttandanceData(Long id);
}
