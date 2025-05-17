package com.tecsoft.vcommute.controller.mobile;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.AttendanceData;
import com.tecsoft.vcommute.service.mobile.AttendanceMobileService;


@RestController
@RequestMapping("/mobile")
public class MobileAttendanceController {

    @Autowired
    private AttendanceMobileService attendanceService;
    
    @PostMapping("/startAttandance")
	public ResponseEntity<Object> startAttandance(@RequestBody AttendanceData attendanceData) {
		return attendanceService.startAttandance(attendanceData);
	}

	@PostMapping("/stopAttandance")
	public ResponseEntity<Object> stopAttandance(@RequestBody AttendanceData attendanceData) throws IOException {
		return attendanceService.stopAttandance(attendanceData);
	}

    @GetMapping(value = "/fetchAttandanceData")
	public ResponseEntity<Object> fetchAttandanceData(@RequestParam Long id) {
		return attendanceService.fetchAttandanceData(id);
	}


}
