package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Holiday;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.HolidayRepository;
import com.vareli.tecsoft.vcommute_business.service.HolidayService;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    @Override
    public ResponseEntity<?> createHoliday(Holiday holiday) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (holiday.getId() == null) {
            if (holiday.getCompanyId() == null) {
                holiday.setCompanyId(user.getCompanyId());
            }
            boolean existsBy = holidayRepository.existsByPurposeAndHolidayDateAndSuperCompanyIdAndCompanyId(
                    holiday.getPurpose(), holiday.getHolidayDate(), user.getSuperCompanyId(), holiday.getCompanyId());
            if (!existsBy) {
                holiday.setSuperCompanyId(user.getSuperCompanyId());
                holiday.setStatus(true);
                holidayRepository.save(holiday);
                return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Created...");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Present...");
            }
        } else {
            holidayRepository.save(holiday);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Updated...");
        }
    }

    @Override
    public ResponseEntity<?> findByAll(Integer companyId, String year) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId==null) {
            companyId =  user.getCompanyId();
        }

        List<Holiday> fetchHolidayList = holidayRepository.fetchHolidayList(year, user.getSuperCompanyId(), companyId);
 
        return ResponseEntity.status(HttpStatus.CREATED).body(fetchHolidayList);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<Holiday> byId = holidayRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(byId); 
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong...");    
        }
    }

    @Override
    public ResponseEntity<?> delteHoliday(Long id) {
        holidayRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Deleted..."); 
    }

    @Override
    public ResponseEntity<?> fetchYearList(Integer companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails user = (MyUserDetails) auth.getPrincipal();
        if (companyId==null) {
            companyId =  user.getCompanyId();
        }
        List<String> fetchYearList = holidayRepository.fetchYearList(user.getSuperCompanyId(), companyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(fetchYearList); 
    }

}
