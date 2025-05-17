package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.ApprovalHeader;
import com.vareli.tecsoft.vcommute_business.service.ApprovalLevelService;

@RestController
@RequestMapping("/approval_level")
public class ApprovalLevelController {

     @Autowired
    private ApprovalLevelService approvalOrderService;

    @PostMapping(value = "/createAppvlLevel")
    public ResponseEntity<?> createAppvlLevel(@RequestBody ApprovalHeader approvalHeader) {
        return approvalOrderService.createAppvlLevel(approvalHeader);
    }

    @PutMapping(value = "/editAppvlLevel")
    public ResponseEntity<?> editAppvlLevel(@RequestBody ApprovalHeader approvalHeader) {
        return approvalOrderService.editAppvlLevel(approvalHeader);
    }


    @GetMapping(value = "/findByAll")
    public ResponseEntity<?> findByAll(@RequestParam (required = false) Integer companyId) {
        return approvalOrderService.findByAll(companyId);
    }
  
    @GetMapping(value = "/findByAllActive")
    public ResponseEntity<?> findByAllActive(@RequestParam (required = false) Integer companyId) {
        return approvalOrderService.findByAllActive(companyId);
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return approvalOrderService.findById(id);
    }

    @GetMapping(value = "/activeAppvlLevel")
    public ResponseEntity<?> activeAppvlLevel(@RequestParam Integer id) {
        return approvalOrderService.activeAppvlLevel(id);
    }

    @GetMapping(value = "/deActiveAppvlLevel")
    public ResponseEntity<?> deActiveAppvlLevel(@RequestParam Integer id) {
        return approvalOrderService.deActiveAppvlLevel(id);
    }

    @GetMapping(value = "/activeAppvlDetails")
    public ResponseEntity<?> activeAppvlDetails(@RequestParam Integer id) {
        return approvalOrderService.activeAppvlDetails(id);
    }

    @GetMapping(value = "/deActiveAppvlDetails")
    public ResponseEntity<?> deActiveAppvlDetails(@RequestParam Integer id) {
        return approvalOrderService.deActiveAppvlDetails(id);
    }
}
