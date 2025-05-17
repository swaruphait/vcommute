package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.Department;

public interface DepartmentService {

    ResponseEntity<?> fetchDepartment();

    ResponseEntity<?> addDepartment(Department department);

    ResponseEntity<?> editDepartment(Integer id);

    ResponseEntity<?> deleteDepartment(Integer id);

}
