package com.tecsoft.vcommute.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.AttendanceData;
import com.tecsoft.vcommute.service.AttendanceService;


@RestController
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping(value = "/attandanceDataByDate")
    public ResponseEntity<?> attandanceDataByDate(@RequestParam String stdate, @RequestParam String enddate) {
        return attendanceService.attandanceDataByDate(stdate, enddate);
    }

    @GetMapping(value = "/fetchAttendApprovalData")
    public ResponseEntity<?> attendApprovalData() {
        return attendanceService.attendApprovalData();
    }

    @GetMapping(value = "/fetchAttendApprovalDataAuthorityWise")
    public ResponseEntity<?> fetchAttendApprovalDataAuthorityWise() {
        return attendanceService.fetchAttendApprovalDataAuthorityWise();
    }

    @GetMapping(value = "/findAttendById")
    public ResponseEntity<?> findAttendById(Long id) {
        return attendanceService.findAttendById(id);
    }



    @GetMapping(value = "/approvAttendData")
    public ResponseEntity<?> approvAttendData(@RequestParam Long id) {
        return attendanceService.approvAttendData(id);
    }

    @GetMapping(value = "/disapprovAttendData")
    public ResponseEntity<?> disapprovAttendData(@RequestParam Long id) {
        return attendanceService.disapprovAttendData(id);
    }

    @GetMapping(value = "/approvAttendDataList")
    public ResponseEntity<?> approvAttendDataList(@RequestParam List<Long> ids) {
        return attendanceService.approvAttendDataList(ids);
    }

    @GetMapping(value = "/disapprovAttendDataList")
    public ResponseEntity<?> disapprovAttendDataList(@RequestParam List<Long> ids) {
        return attendanceService.disapprovAttendDataList(ids);
    }

    
    @PostMapping(value = "/addAttendanceByAuthority", consumes = "application/json")
    public ResponseEntity<?> addAttendanceByAuthority(@RequestBody AttendanceData attendanceDto) {
        return attendanceService.addAttendanceByAuthority(attendanceDto);
    }

    @PutMapping(value = "/editAttendance")
    public ResponseEntity<?> editAttendance(@RequestBody AttendanceData attendanceData) {
        return attendanceService.editAttendance(attendanceData);
    }

    @GetMapping(value = "/takeAttendanceData")
    public ResponseEntity<?> takeAttendanceData(@RequestParam String startDate) {
        return attendanceService.takeAttendanceData(startDate);
    }


}
