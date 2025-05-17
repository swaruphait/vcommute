package com.tecsoft.vcommute.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.Role;
import com.tecsoft.vcommute.repository.RoleRepository;
import com.tecsoft.vcommute.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> fetchRoleType() {
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.findAll());
    }

    @Override
    public ResponseEntity<?> addRoleType(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Alreday In the Database");
        } else {
            roleRepository.save(role);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Saved Role");
        }
    }

    @Override
    public ResponseEntity<?> deleteusertype(Integer id) {
        roleRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted Role");
    }

    @Override
    public ResponseEntity<?> editusertype(Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.findById(id));
    }

}
