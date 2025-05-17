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

import com.vareli.tecsoft.vcommute_business.model.Mode;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.ModeRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.SuperCompanyRepository;
import com.vareli.tecsoft.vcommute_business.repository.TransportRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.ModeService;

@Service
public class ModeServiceImpl implements ModeService{

    @Autowired
    private ModeRepository modeRepository;
 
    @Autowired
    private SubCompanyRepository subCompanyRepository;

    @Autowired
    private SuperCompanyRepository superCompanyRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CityRepository cityRepository;


    @Override
    public ResponseEntity<?> createMode(Mode mode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (mode.getId() == null) {
            if (mode.getCompanyId() == null) {
                mode.setCompanyId(user.getCompanyId());
            }
            mode.setSuperCompanyId(user.getSuperCompanyId());
            boolean exists = modeRepository
                    .existsByLevelIdAndLocationIdAndSuperCompanyIdAndCompanyId(mode.getLevelId(), mode.getLocationId(),
                    mode.getSuperCompanyId(), mode.getCompanyId());
            if (!exists) {
                mode.setStatus(true);
                modeRepository.save(mode);
                return ResponseEntity.status(HttpStatus.OK).body("Mode Successfully Saved....");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Mode Saved....");
            }
        } else {
            modeRepository.save(mode);
            return ResponseEntity.status(HttpStatus.OK).body("Mode Successfully Updated....");
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId) {
             Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId == null) {
            companyId = user.getCompanyId();
        }
        List<Mode> collect = modeRepository.findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(),
                companyId);
                for (Mode mode : collect) {
                    if (mode.getCompanyId()==0) {
                        mode.setCompanyName(superCompanyRepository.findById(mode.getSuperCompanyId()).get().getName());
                    } else {
                        mode.setCompanyName(subCompanyRepository.findById(mode.getCompanyId()).get().getName());
                    }
                    mode.setLevelName(transportRepository.findById(mode.getLevelId()).get().getDes());
                    mode.setCityName(cityRepository.findById(mode.getLocationId()).get().getCity());
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
        List<Mode> collect = modeRepository
                .findAllBySuperCompanyIdAndCompanyId(user.getSuperCompanyId(), companyId).stream()
                .filter(c -> c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<Mode> byId = modeRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong", HttpStatus.OK, null);
            
        }
    }

    @Override
    public ResponseEntity<?> activeMode(Long id) {
        Optional<Mode> byId = modeRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            modeRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("Activated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mode Already Active....");
        }
    }

    @Override
    public ResponseEntity<?> deactiveMode(Long id) {
        Optional<Mode> byId = modeRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            modeRepository.save( byId.get());
            return ResponseEntity.status(HttpStatus.OK).body("DeActivated Successfully....");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mode Already DeActive....");
        }
    }
    
}
