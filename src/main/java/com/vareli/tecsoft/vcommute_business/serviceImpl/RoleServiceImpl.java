package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Role;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.RoleRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

        @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Override
    public ResponseEntity<?> createRole(Role role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (role.getId() == null) {
            if (role.getCompanyId() == null) {
                role.setCompanyId(user.getCompanyId());
            }
            role.setSuperCompanyId(user.getSuperCompanyId());
            boolean exists = roleRepository
                    .existsByNameAndSuperCompanyIdAndCompanyId(role.getName(),
                    role.getSuperCompanyId(), role.getCompanyId());
            if (!exists) {
                role.setStatus(true);
                roleRepository.save(role);
                return ResponseEntity.status(HttpStatus.OK).body("Role Successfully Saved....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Role Saved....");
            }
        } else {
            roleRepository.save(role);
            return ResponseEntity.status(HttpStatus.OK).body("Role Successfully Updated....");
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
               Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Role> collect = roleRepository.findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(),
                companyId);
                 for (Role role : collect) {
                    if (role.getCompanyId()==0) {
                        role.setCompanyName(superCompanyRepository.findById(role.getSuperCompanyId()).get().getName());
                    } else {
                        role.setCompanyName(subCompanyRepository.findById(role.getCompanyId()).get().getName());
                    }
                }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findByAllActive(Integer companyId) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Role> collect = roleRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId).stream()
                .filter(c -> c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<Role> byId = roleRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong", HttpStatus.OK, null);    
        }
    }

    @Override
    public ResponseEntity<?> activeRole(Integer id) {
        Optional<Role> byId = roleRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            roleRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("Activated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role Already Active....");
        }
    }

    @Override
    public ResponseEntity<?> deactiveRole(Integer id) {
        Optional<Role> byId = roleRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            roleRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("DeActivated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role DeAlready Active....");
        }
    }
    
}
