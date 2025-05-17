package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Holiday;
import com.vareli.tecsoft.vcommute_business.service.HolidayService;

@RestController
@RequestMapping("/holiday")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @PostMapping(value = "/createHoliday")
    public ResponseEntity<?> createHoliday(@RequestBody Holiday holiday) {
        return holidayService.createHoliday(holiday);
    }

    @GetMapping(value = "/findByAll")
    public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId,@RequestParam(required = false) String year) {
        return holidayService.findByAll(companyId, year);
    }

    @GetMapping(value = "/findById")
    public ResponseEntity<?> findById(@RequestParam Long id) {
        return holidayService.findById(id);
    }

    @GetMapping(value = "/fetchYearList")
    public ResponseEntity<?> fetchYearList(@RequestParam (required = false) Integer companyId) {
        return holidayService.fetchYearList(companyId);
    }

    @DeleteMapping(value = "/deleteHoliday")
    public ResponseEntity<?> delteHoliday(@RequestParam Long id) {
        return holidayService.delteHoliday(id);
    }

}
