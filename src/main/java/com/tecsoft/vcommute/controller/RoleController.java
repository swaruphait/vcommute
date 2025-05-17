package com.tecsoft.vcommute.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.Role;
import com.tecsoft.vcommute.service.RoleService;

@RestController
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/fetchRoleType")
    public ResponseEntity<?> fetchRoleType() {
        return roleService.fetchRoleType();
    }

    @PostMapping(value = "/addRoleType", consumes = "application/json")
    public ResponseEntity<?> addRoleType(@RequestBody Role role, HttpServletRequest request) throws Exception {
        return roleService.addRoleType(role);
    }

    @DeleteMapping(value = "/deleteusertype")
    public ResponseEntity<?> deleteusertype(@RequestParam Integer id) {
        return roleService.deleteusertype(id);
    }

    @PutMapping(value = "/editusertype")
    public ResponseEntity<?> editusertype(@RequestParam Integer id) {
        return roleService.editusertype(id);
    }
}
