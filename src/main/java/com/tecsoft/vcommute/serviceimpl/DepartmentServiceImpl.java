package com.tecsoft.vcommute.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.Department;
import com.tecsoft.vcommute.repository.DepartmentRepository;
import com.tecsoft.vcommute.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public ResponseEntity<?> fetchDepartment() {
        return ResponseEntity.status(HttpStatus.OK).body(departmentRepository.findAllByStatus(true));
    }

    @Override
    public ResponseEntity<?> addDepartment(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Same Department Already Present");
        } else {
            department.setStatus(true);
            departmentRepository.save(department);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Department Saved");
        }

    }

    @Override
    public ResponseEntity<?> editDepartment(Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(departmentRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> deleteDepartment(Integer id) {
        Optional<Department> byId = departmentRepository.findById(id);
        byId.get().setStatus(false);
        departmentRepository.save(byId.get());
        return ResponseEntity.status(HttpStatus.OK).body("Succesffuly Deleted");
    }

}
