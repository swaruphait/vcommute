package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Leave;
import com.vareli.tecsoft.vcommute_business.service.LeaveService;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping(value = "/createLeave")
    public ResponseEntity<?> createLeave(@RequestBody Leave leave) {
        return leaveService.createLeave(leave);
    }

    @GetMapping(value = "/findByAll")
    public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
        return leaveService.findByAll(companyId);
    }

    @GetMapping(value = "/findByAllActive")
    public ResponseEntity<?> findByAllActive() {
        return leaveService.findByAllActive();
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return leaveService.findById(id);
    }

    @GetMapping(value = "/activeLeave")
    public ResponseEntity<?> activeLeave(@RequestParam Integer id) {
        return leaveService.activeLeave(id);
    }

    @GetMapping(value = "/deactiveLeave")
    public ResponseEntity<?> deactiveLeave(@RequestParam Integer id) {
        return leaveService.deactiveLeave(id);
    }
}
