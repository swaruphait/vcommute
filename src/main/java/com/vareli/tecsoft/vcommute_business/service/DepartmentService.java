package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Department;

public interface DepartmentService {

    ResponseEntity<?> createDepartment(Department department);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive(Integer companyId);

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeDepartment(Integer id);

    ResponseEntity<?> deactiveDepartment(Integer id);
    
}
