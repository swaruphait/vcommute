package com.tecsoft.vcommute.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.FinanceAuthority;
import com.tecsoft.vcommute.service.FinanceAuthorityService;

@RestController
public class FinanceAuthorityController {

    @Autowired
    private FinanceAuthorityService financeAuthorityService;

    @GetMapping("/fetchFinanceAuthority")
    public ResponseEntity<?> fetchFinanceAuthority() {
        return financeAuthorityService.fetchFinanceAuthority();
    }

    @PostMapping(value = "/addFinanceAuthority", consumes = "application/json")
    public ResponseEntity<?> addFinanceAuthority(@RequestBody FinanceAuthority financeAuthority,
            HttpServletRequest request) throws Exception {
        return financeAuthorityService.addFinanceAuthority(financeAuthority);
    }

    @GetMapping(value = "/editFinanceAuthority")
    public ResponseEntity<?> editFinanceAuthority(@RequestParam Integer id) {
        return financeAuthorityService.editFinanceAuthority(id);
    }

    @GetMapping(value = "/deleteFinanceAuthority")
    public ResponseEntity<?> deleteFinanceAuthority(@RequestParam Integer id) {
        return financeAuthorityService.deleteFinanceAuthority(id);
    }

    @GetMapping("/fetchAllFinaceRole")
    public ResponseEntity<?> fetchAllFinaceRole() {
        return financeAuthorityService.fetchAllFinaceRole();
    }

}
