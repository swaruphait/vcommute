package com.tecsoft.vcommute.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.Department;
import com.tecsoft.vcommute.service.DepartmentService;

@RestController
public class DepartmenController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/fetchDepartment")
    public ResponseEntity<?> fetchDepartment() {
        return departmentService.fetchDepartment();
    }

    @PostMapping(value = "/addDepartment", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> addDepartment(@RequestBody Department department, HttpServletRequest request)
            throws Exception {
        return departmentService.addDepartment(department);
    }

    @GetMapping(value = "/editDepartment")
    public ResponseEntity<?> editDepartment(@RequestParam Integer id) {
        return departmentService.editDepartment(id);
    }

    @GetMapping(value = "/deleteDepartment")
    public ResponseEntity<?> deleteDepartment(@RequestParam Integer id) {
        return departmentService.deleteDepartment(id);
    }

}
