package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.CommuteCliamAmount;
import com.vareli.tecsoft.vcommute_business.service.ClaimSettlementService;

@RestController
@RequestMapping("/claim")
public class ClaimSettelmentController {
    
        @Autowired
    private ClaimSettlementService claimSettlementService;

        @PostMapping(value = "/addAdvance")
    public ResponseEntity<?> addAdvance(@RequestBody CommuteCliamAmount commuteCliamAmount){
        return claimSettlementService.addAdvance(commuteCliamAmount);
    }

    @GetMapping("/fetchAdavnce")
    public ResponseEntity<?> fetchAdavnce() {
        return claimSettlementService.fetchAdavnce();
    }

    @GetMapping(value = "/fetchAdavnceById")
    public ResponseEntity<?> fetchAdavnceById(@RequestParam Long id) {
        return claimSettlementService.fetchAdavnceById(id);
    }
}
