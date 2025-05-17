package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Holiday;

public interface HolidayService {

    ResponseEntity<?> createHoliday(Holiday holiday);

    ResponseEntity<?> findByAll(Integer companyId, String year);

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> delteHoliday(Long id);

    ResponseEntity<?> fetchYearList(Integer companyId);
    
}
