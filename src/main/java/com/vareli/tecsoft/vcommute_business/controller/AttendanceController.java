package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.service.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    

    @Autowired
    private AttendanceService attendanceService;


    @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findAllAttendance(@RequestParam(required = false) String stdate,
			@RequestParam(required = false) String enddate) {
		return attendanceService.findByAll(stdate, enddate);
	}

    @GetMapping(value = "/fetchAttendLevelAuthority")
	public ResponseEntity<?> fetchAttendLevelAuthority(@RequestParam(required = false) String stdate,
			@RequestParam(required = false) String enddate) {
		return attendanceService.fetchAttendLevelAuthority(stdate, enddate);
	}

}
