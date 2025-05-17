package com.tecsoft.vcommute.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.LevelDescriptions;
import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.repository.LevelDescriptionsRepository;
import com.tecsoft.vcommute.service.TransportService;

@Service
public class TransportServiceImpl implements TransportService {

    @Autowired
    private LevelDescriptionsRepository levelDescriptionsRepository;

    @Override
    public ResponseEntity<?> addLevelDes(LevelDescriptions levelDescriptions) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer companyId = ((MyUserDetails) principal).CompanyId();
        if (levelDescriptions.getId()==null) {
            if (levelDescriptionsRepository.existsByDes(levelDescriptions.getDes())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Details already present!!");
            } else {
                levelDescriptions.setCompanyId(companyId);
                levelDescriptions.setStatus(true);
                levelDescriptionsRepository.save(levelDescriptions);
                return ResponseEntity.status(HttpStatus.OK).body("Successfully Save");
            }  
        } else {
            levelDescriptionsRepository.save(levelDescriptions);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Updated");
        }
        
    }

    @Override
    public ResponseEntity<?> fetchLevelDescriptions() {
        return ResponseEntity.status(HttpStatus.OK).body(levelDescriptionsRepository.findAllByStatus(true));
    }

    @Override
    public ResponseEntity<?> editLevelDescriptions(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(levelDescriptionsRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> deleteLevelDescriptions(Long id) {
        Optional<LevelDescriptions> byLvlDesId = levelDescriptionsRepository.findById(id);
        byLvlDesId.get().setStatus(false);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
    }

}
