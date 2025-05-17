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

import com.vareli.tecsoft.vcommute_business.model.Grade;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.GradeRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.GradeService;

@Service
public class GradeServiceImpl implements GradeService{

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Override
    public ResponseEntity<?> createGrade(Grade grade) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (grade.getId() == null) {
            if (grade.getCompanyId() == null) {
                grade.setCompanyId(user.getCompanyId());
            }
            grade.setSuperCompanyId(user.getSuperCompanyId());
            boolean exists = gradeRepository
                    .existsByNameAndSuperCompanyIdAndCompanyId(grade.getName(),
                    grade.getSuperCompanyId(), grade.getCompanyId());
            if (!exists) {
                grade.setStatus(true);
                gradeRepository.save(grade);
                return ResponseEntity.status(HttpStatus.OK).body("Grade Successfully Saved....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Grade Saved....");
            }
        } else {
            gradeRepository.save(grade);
            return ResponseEntity.status(HttpStatus.OK).body("Grade Successfully Updated....");
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Grade> collect = gradeRepository.findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(),
                companyId);
                for (Grade grade : collect) {
                    if (grade.getCompanyId()==0) {
                        grade.setCompanyName(superCompanyRepository.findById(grade.getSuperCompanyId()).get().getName());
                    } else {
                        grade.setCompanyName(subCompanyRepository.findById(grade.getCompanyId()).get().getName());
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
        List<Grade> collect = gradeRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId).stream()
                .filter(c -> c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Integer id) {
         Optional<Grade> byId = gradeRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong", HttpStatus.OK, null);
        }
    }

    @Override
    public ResponseEntity<?> activeGrade(Integer id) {
        Optional<Grade> byId = gradeRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            gradeRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("Activated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Grade Already Active....");
        }
    }

    @Override
    public ResponseEntity<?> deactiveGrade(Integer id) {
        Optional<Grade> byId = gradeRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            gradeRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("DeActivated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Grade Already DeActive....");
        }
    }
    
}
