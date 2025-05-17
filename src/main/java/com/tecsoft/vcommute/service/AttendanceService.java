package com.tecsoft.vcommute.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.AttendanceData;

public interface AttendanceService {

    ResponseEntity<?> attandanceDataByDate(String stdate, String enddate);

    ResponseEntity<?> attendApprovalData();

    ResponseEntity<?> approvAttendData(Long id);

    ResponseEntity<?> disapprovAttendData(Long id);

    ResponseEntity<?> approvAttendDataList(List<Long> ids);

    ResponseEntity<?> disapprovAttendDataList(List<Long> ids);

    ResponseEntity<?> addAttendanceByAuthority(AttendanceData attendanceDto);

    ResponseEntity<?> fetchAttendApprovalDataAuthorityWise();

    ResponseEntity<?> findAttendById(Long id);

    ResponseEntity<?> editAttendance(AttendanceData attendanceData);

    ResponseEntity<?> takeAttendanceData(String startDate);

}
