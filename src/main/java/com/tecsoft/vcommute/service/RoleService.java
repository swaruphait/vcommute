package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.Role;

public interface RoleService {

    ResponseEntity<?> fetchRoleType();

    ResponseEntity<?> addRoleType(Role role);

    ResponseEntity<?> deleteusertype(Integer id);

    ResponseEntity<?> editusertype(Integer id);

}
