package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.SubCompany;
import com.vareli.tecsoft.vcommute_business.service.SubcompanyService;

@RestController
@RequestMapping("/subcompany")
public class SubcompanyController {
    
   @Autowired
   private SubcompanyService subcompanyService;

     @PostMapping("/createCompany")
    public ResponseEntity<?> createCompany(@RequestBody SubCompany company) {
        return subcompanyService.createCompany(company);
    }

    @GetMapping("/findByAll")
    public ResponseEntity<?> findByAll() {
        return subcompanyService.findByAll();
    }

    @GetMapping("/findByAllActive")
    public ResponseEntity<?> findByAllActive() {
        return subcompanyService.findByAllActive();
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return subcompanyService.findById(id);
    }

    @GetMapping("/activeCompany")
    public ResponseEntity<?> activeCompany(@RequestParam Integer id) {
        return subcompanyService.activeCompany(id);
    }

    @GetMapping("/deactiveCompany")
    public ResponseEntity<?> deactiveCompany(@RequestParam Integer id) {
        return subcompanyService.deactiveCompany(id);
    }
}
