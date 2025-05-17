package com.vareli.tecsoft.vcommute_business.controller.android;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.LeaveData;
import com.vareli.tecsoft.vcommute_business.service.android.MobileLeaveService;

@RestController
@RequestMapping("/android")
public class MobileLeaveController {
    
    @Autowired
    private MobileLeaveService mobileLeaveService;

    @PostMapping(value ="/leaveApply")
    	public ResponseEntity<?> leaveApply(@RequestBody LeaveData leaveData) {	
		return mobileLeaveService.leaveApply(leaveData);
	}

    @GetMapping(value ="/fetchLeaveData")
	public ResponseEntity<?> fetchLeaveData(@RequestParam Long userId) {
	     return mobileLeaveService.fetchLeaveData(userId);
	}
	
	@GetMapping(value ="/fetchLeaveType")
	public ResponseEntity<?> fetchLeaveType(@RequestParam Long userId) {
	     return mobileLeaveService.fetchLeaveType(userId);
	}

	@GetMapping(value ="/fetchPendingLeave")
	public ResponseEntity<?> fetchPendingLeave(@RequestParam Long userId) {
	     return mobileLeaveService.fetchPendingLeave(userId);
	}

	@GetMapping(value ="/fetchLeaveHistory")
	public ResponseEntity<?> fetchLeaveHistory(@RequestParam Long userId) {
	     return mobileLeaveService.fetchLeaveHistory(userId);
	}

	@GetMapping(value ="/fetchLeaveBalance")
	public ResponseEntity<?> fetchLeaveBalance(@RequestParam Long userId) {
	     return mobileLeaveService.fetchLeaveBalance(userId);
	}

}
