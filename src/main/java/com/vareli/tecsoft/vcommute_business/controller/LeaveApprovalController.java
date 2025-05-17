package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.service.LeaveApprovalService;

@RestController
@RequestMapping("/leave_apvl")
public class LeaveApprovalController {
    
    @Autowired
    private LeaveApprovalService leaveApprovalService;

     @PutMapping(value = "/leaveApproval")
	public ResponseEntity<?> leaveApproval(@RequestParam Long id) {
		return leaveApprovalService.leaveApproval(id);
	}

    @PutMapping(value = "/rejectLeave")
	public ResponseEntity<?> rejectLeave(@RequestParam Long id) {
		return leaveApprovalService.rejectLeave(id);
	}

     @GetMapping(value = "/fetchAprrovalList")
	public ResponseEntity<?> fetchAprrovalList() {
		return leaveApprovalService.fetchAprrovalList();
	}

	@GetMapping(value = "/fetchLeaveAllData")
	public ResponseEntity<?> fetchLeaveAllData(@RequestParam (required = false) String startDate, 
    @RequestParam (required = false) String endDate) {
		return leaveApprovalService.fetchLeaveAllData(startDate, endDate);
	}

	@GetMapping(value = "/fetchLeaveAuthorityData")
	public ResponseEntity<?> fetchLeaveAuthorityData(@RequestParam (required = false) String startDate, 
    @RequestParam (required = false) String endDate) {
		return leaveApprovalService.fetchLeaveAuthorityData(startDate, endDate);
	}
}
