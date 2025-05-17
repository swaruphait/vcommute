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

import com.vareli.tecsoft.vcommute_business.model.Department;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.DepartmentRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Override
    public ResponseEntity<?> createDepartment(Department department) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (department.getId() == null) {
            if (department.getCompanyId() == null) {
                department.setCompanyId(user.getCompanyId());
            }
            department.setSuperCompanyId(user.getSuperCompanyId());
            boolean exists = departmentRepository
                    .existsByNameAndSuperCompanyIdAndCompanyId(department.getName(),
                    department.getSuperCompanyId(), department.getCompanyId());
            if (!exists) {
                department.setStatus(true);
                departmentRepository.save(department);
                return ResponseEntity.status(HttpStatus.OK).body("Department Successfully Saved....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Department Saved....");
            }
        } else {
            Optional<Department> byId = departmentRepository.findById(department.getId());
            if (byId.isPresent()) {
                Department department2 = byId.get();
                department2.setName(department.getName());
                departmentRepository.save(department2);
                return ResponseEntity.status(HttpStatus.OK).body("Department Successfully Updated....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong....");
                
            }

        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Department> collect = departmentRepository.findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(),
                companyId);
                 for (Department data : collect) {
                    if (data.getCompanyId()==0) {
                        data.setCompanyName(superCompanyRepository.findById(data.getSuperCompanyId()).get().getName());
                    } else {
                        data.setCompanyName(subCompanyRepository.findById(data.getCompanyId()).get().getName());
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
        List<Department> collect = departmentRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId).stream()
                .filter(c -> c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
       Optional<Department> byId = departmentRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong", HttpStatus.OK, null);
            
        }
    }

    @Override
    public ResponseEntity<?> activeDepartment(Integer id) {
        Optional<Department> byId = departmentRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            departmentRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("Activated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role Already Active....");
        }
    }

    @Override
    public ResponseEntity<?> deactiveDepartment(Integer id) {
        Optional<Department> byId = departmentRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            departmentRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("DeActivated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role DeAlready Active....");
        }
    }
    
}
