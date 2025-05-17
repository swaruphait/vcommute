package com.tecsoft.vcommute.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.Mode;
import com.tecsoft.vcommute.model.MyUserDetails;
import com.tecsoft.vcommute.repository.CityRepository;
import com.tecsoft.vcommute.repository.CompanyRepository;
import com.tecsoft.vcommute.repository.LevelDescriptionsRepository;
import com.tecsoft.vcommute.repository.ModeRepository;
import com.tecsoft.vcommute.service.ModeService;

@Service
public class ModeServiceImpl implements ModeService {

    @Autowired
    private ModeRepository modeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private LevelDescriptionsRepository descriptionsRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> fetchMode() {
        List<Mode> all = modeRepository.findAllByEnabled(true);
        for (Mode mode : all) {
            if (mode.getCompanyId() != null) {
                mode.setComanyName(companyRepository.findById(mode.getCompanyId()).get().getName());
            }
            mode.setCityName(cityRepository.findById(mode.getLocationId()).get().getCity());
            mode.setLevelName(descriptionsRepository.findById(mode.getLevelId()).get().getDes());

        }
        return ResponseEntity.status(HttpStatus.OK).body(all);
    }

    @Override
    public ResponseEntity<?> addMode(Mode mode) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer companyId = ((MyUserDetails) principal).CompanyId();
        if (mode.getId()==null) {
            if (modeRepository.existsByLocationIdAndLevelId(mode.getLocationId(), mode.getLevelId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Present in DB");
            } else {
                mode.setCompanyId(companyId);
                mode.setEnabled(true);
                modeRepository.save(mode);
                return ResponseEntity.status(HttpStatus.OK).body("Successfully Saved");
            }
        } else {
            modeRepository.save(mode);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Updated");
        }
    }

    @Override
    public ResponseEntity<?> editMode(Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(modeRepository.findById(id));
    }

    @Override
    public ResponseEntity<?> deleteMode(Integer id) {
        Optional<Mode> byId = modeRepository.findById(id);
        byId.get().setEnabled(false);
        modeRepository.save(byId.get());
        return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted");
    }

}
