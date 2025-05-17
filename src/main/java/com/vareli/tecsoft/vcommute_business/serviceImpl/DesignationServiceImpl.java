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
import com.vareli.tecsoft.vcommute_business.model.Designation;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.DepartmentRepository;
import com.vareli.tecsoft.vcommute_business.repository.DesignationRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.DesignationService;

@Service
public class DesignationServiceImpl implements DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Override
    public ResponseEntity<?> createDesgination(Designation designation) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (designation.getId() == null) {
            if (designation.getCompanyId() == null) {
                designation.setCompanyId(user.getCompanyId());
            }
            designation.setSuperCompanyId(user.getSuperCompanyId());
            Optional<Department> byId = departmentRepository.findById(designation.getDepartmentId());
            boolean exists = designationRepository.existsByNameAndDepartmentAndSuperCompanyIdAndCompanyId(
                    designation.getName(), byId.get(), user.getSuperCompanyId(), designation.getCompanyId());
            if (!exists) {
                designation.setDepartment(byId.get());
                designation.setStatus(true);
                designationRepository.save(designation);
                return ResponseEntity.status(HttpStatus.OK).body("Designation Successfully Saved....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Designation Saved....");
            }
        } else {
            Optional<Department> byId = departmentRepository.findById(designation.getDepartmentId());
            designation.setDepartment(byId.get());
            designationRepository.save(designation);
            return ResponseEntity.status(HttpStatus.OK).body("Designation Successfully Updated....");
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Designation> exists = designationRepository.findBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(),
                companyId);
        for (Designation designation : exists) {
            if (designation.getCompanyId() == 0) {
                designation.setCompanyName(
                        superCompanyRepository.findById(designation.getSuperCompanyId()).get().getName());
            } else {
                designation.setCompanyName(subCompanyRepository.findById(designation.getCompanyId()).get().getName());
            }
            designation.setDeptName(designation.getDepartment().getName());
        }

                return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, exists);
        // return ResponseEntity.status(HttpStatus.CREATED).body(exists);
    }

    @Override
    public ResponseEntity<?> findByAllActive(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Designation> exists = designationRepository
                .findBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId).stream()
                .filter(u -> u.isStatus()).collect(Collectors.toList());
        for (Designation designation : exists) {
            designation.setDeptName(designation.getDepartment().getName());
            if (designation.getCompanyId() == 0) {
                designation.setCompanyName(
                        superCompanyRepository.findById(designation.getSuperCompanyId()).get().getName());
            } else {
                designation.setCompanyName(subCompanyRepository.findById(designation.getCompanyId()).get().getName());
            }
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, exists);

        // return ResponseEntity.status(HttpStatus.CREATED).body(exists);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
        Optional<Designation> byId = designationRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().setDepartmentId(byId.get().getDepartment().getId());
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
            // return ResponseEntity.status(HttpStatus.CREATED).body(byId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong..");

        }
    }

    @Override
    public ResponseEntity<?> activeDesgination(Integer id) {
        Optional<Designation> byId = designationRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            designationRepository.save(byId.get());
            
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfullly Activated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already active..");
        }
    }

    @Override
    public ResponseEntity<?> deactiveDesgination(Integer id) {
        Optional<Designation> byId = designationRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            designationRepository.save(byId.get());
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Deactivated");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Deactivate..");
        }
    }

    @Override
    public ResponseEntity<?> findByDepartmentAll(Integer deptId) {
        Optional<Department> byId = departmentRepository.findById(deptId);
        if (byId.isPresent()) {
            Department department = byId.get();
            List<Designation> allByDepartmentAndStatus = designationRepository.findAllByDepartmentAndStatus(department,
                    true);
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, allByDepartmentAndStatus);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Opps Something went wrong..");

        }
    }

}
