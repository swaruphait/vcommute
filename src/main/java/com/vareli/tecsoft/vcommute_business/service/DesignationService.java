package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Designation;

public interface DesignationService {

    ResponseEntity<?> createDesgination(Designation designation);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive(Integer companyId);

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeDesgination(Integer id);

    ResponseEntity<?> deactiveDesgination(Integer id);

    ResponseEntity<?> findByDepartmentAll(Integer deptId);
    
}
