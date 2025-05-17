package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.SubcriptionPlan;
import com.vareli.tecsoft.vcommute_business.service.PlanService;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PostMapping("/addPlan")
    public ResponseEntity<?> addPlan(@RequestBody SubcriptionPlan plan) {
        return planService.addPlan(plan);
    }

    @GetMapping("/findByAllActiveList")
    public ResponseEntity<?> findByAllActiveList() {
        return planService.findByAllActiveList();
    }

    @GetMapping("/findByAll")
    public ResponseEntity<?> findByAll() {
        return planService.findByAll();
    }

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return planService.findById(id);
    }

    @GetMapping("/activePlan")
    public ResponseEntity<?> activePlan(@RequestParam Integer id) {
        return planService.activePlan(id);
    }

    @GetMapping("/deactivePlan")
    public ResponseEntity<?> deactivePlan(@RequestParam Integer id) {
        return planService.deactivePlan(id);
    }
}
