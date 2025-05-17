package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.SubcriptionPlan;

public interface PlanService {

    ResponseEntity<?> addPlan(SubcriptionPlan plan);

    ResponseEntity<?> findByAllActiveList();

    ResponseEntity<?> findByAll();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activePlan(Integer id);

    ResponseEntity<?> deactivePlan(Integer id);
    
}
