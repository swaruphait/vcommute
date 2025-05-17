package com.vareli.tecsoft.vcommute_business.controller.android;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Attendance;
import com.vareli.tecsoft.vcommute_business.service.android.MobileAttendanceService;


@RestController
@RequestMapping("/android")
public class MobileAttendanceController {
    
    @Autowired
    private MobileAttendanceService mobileAttendanceService;

    @PostMapping("/startAttandance")
	public ResponseEntity<?> startAttandance(@RequestBody Attendance attendance) {	
		return mobileAttendanceService.startAttandance(attendance);
	}

	@PostMapping("/stopAttandance")
	public ResponseEntity<?> stopAttandance(@RequestBody Attendance attendance) throws IOException {
		return mobileAttendanceService.stopAttandance(attendance);
	}
		
	@GetMapping(value = "/fetchAttandanceData")
	public ResponseEntity<?> fetchAttandanceData(@RequestParam Long userId) {
		return mobileAttendanceService.fetchAttandanceData(userId);
	}

		@GetMapping(value = "/fetchUnstoppedAttandanceData")
	public ResponseEntity<?> fetchUnstoppedAttandanceData(@RequestParam Long userId) {
		return mobileAttendanceService.fetchUnstoppedAttandanceData(userId);
	}
}
