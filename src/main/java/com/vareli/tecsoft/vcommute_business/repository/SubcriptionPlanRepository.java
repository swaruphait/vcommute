package com.vareli.tecsoft.vcommute_business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.SubcriptionPlan;

public interface SubcriptionPlanRepository extends JpaRepository<SubcriptionPlan, Integer>{
    boolean existsByPlanName(String planName);
    
 }
 