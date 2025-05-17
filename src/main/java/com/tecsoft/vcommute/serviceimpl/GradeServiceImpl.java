package com.tecsoft.vcommute.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.Grade;
import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.repository.GradeRepository;
import com.tecsoft.vcommute.service.GradeService;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public ResponseEntity<?> addGrade(Grade grade) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer companyId = ((MyUserDetails) principal).CompanyId();
        if (gradeRepository.existsByNameAndLevelAndStatus(grade.getName(), grade.getLevel(), true)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data Already Present");
        } else {
            grade.setCompanyId(companyId);
            grade.setStatus(true);
            gradeRepository.save(grade);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully save");
        }

    }

    @Override
    public ResponseEntity<?> fetchGrade() {
        return ResponseEntity.status(HttpStatus.OK).body(gradeRepository.findAllByStatus(true));
    }

    @Override
    public ResponseEntity<?> editGrade(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(gradeRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> deleteGrade(Long id) {
        Optional<Grade> byId = gradeRepository.findById(id);
        byId.get().setStatus(false);
        gradeRepository.save(byId.get());
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted");
    }

}
