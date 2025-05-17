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

import com.vareli.tecsoft.vcommute_business.model.SuperCompany;
import com.vareli.tecsoft.vcommute_business.service.CompanyService;

@RestController
@RequestMapping("/company")
public class CompanyController {

     @Autowired
    private CompanyService companyService;

    @PostMapping("/createSuperCompany")
    public ResponseEntity<?> createSuperCompany(@RequestBody SuperCompany company) {
        return companyService.createSuperCompany(company);
    }

    @PutMapping("/editSuperCompany")
    public ResponseEntity<?> editSuperCompany(@RequestBody SuperCompany company) {
        return companyService.editSuperCompany(company);
    }

    @GetMapping("/findByAllActiveList")
    public ResponseEntity<?> findByAllActiveList() {
        return companyService.findByAllActiveList();
    }

    @GetMapping("/findByAll")
    public ResponseEntity<?> findByAll() {
        return companyService.findByAll();
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return companyService.findById(id);
    }

    @GetMapping("/activeCompany")
    public ResponseEntity<?> activeCompany(@RequestParam Integer id) {
        return companyService.activeCompany(id);
    }

    @GetMapping("/deactiveCompany")
    public ResponseEntity<?> deactiveCompany(@RequestParam Integer id) {
        return companyService.deactiveCompany(id);
    }

}
