package com.tecsoft.vcommute.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.model.AdvanceBalance;
import com.tecsoft.vcommute.model.CommuteBalance;
import com.tecsoft.vcommute.service.CommuteBalanceService;

@RestController
public class CommuteBalanceController {

    @Autowired
    CommuteBalanceService commuteBalanceService;

    @PostMapping(value = "/addAdavnceCommute", consumes = "application/json")
    public ResponseEntity<?> addAdavnceCommute(@RequestBody CommuteBalance commuteBalance, HttpServletRequest request)
            throws Exception {
        return commuteBalanceService.addAdavnceCommute(commuteBalance);
    }

    @PostMapping(value = "/editAdavnceCommute", consumes = "application/json")
    public ResponseEntity<?> editAdavnceCommute(@RequestBody AdvanceBalance advanceBalance, HttpServletRequest request)
            throws Exception {
        return commuteBalanceService.editAdavnceCommute(advanceBalance);
    }

    @GetMapping(value = "/fetchAdavnceById")
    public ResponseEntity<?> fetchAdavnceById(@RequestParam Long id) {
        return commuteBalanceService.fetchAdavnceById(id);
    }

    @GetMapping("/fetchAdavnceCommute")
    public ResponseEntity<?> fetchAdavnceCommute(@RequestParam(required = false) String stdate, @RequestParam(required = false) String enddate) {
        return commuteBalanceService.fetchAdavnceCommute(stdate, enddate);
    }

    @PostMapping(value = "/deleteAdavnceCommute")
    public ResponseEntity<?> deleteAdavnceCommute(@RequestParam Long id) {
        return commuteBalanceService.deleteAdavnceCommute(id);
    }

    @GetMapping("/fetchBalanceSheet")
    public ResponseEntity<?> fetchBalanceSheet() {
        return commuteBalanceService.fetchBalanceSheet();
    }

    @GetMapping("/fetchPaiidSheet")
    public ResponseEntity<?> fetchPaiidSheet(@RequestParam(required = false) String stdate,
            @RequestParam(required = false) String enddate) {
        return commuteBalanceService.fetchPaiidSheet(stdate, enddate);
    }

    @PutMapping(value = "/markPaidSheet")
    public ResponseEntity<?> markPaidSheet(@RequestParam Integer id) {
        return commuteBalanceService.markPaidSheet(id);
    }

    @PutMapping(value = "/markListOfPaidSheet")
    public ResponseEntity<?> markListOfPaidSheet(@RequestParam List<Integer> ids) {
        return commuteBalanceService.markListOfPaidSheet(ids);
    }

    @PutMapping(value = "/markPaymentImportedList")
    public ResponseEntity<?> markPaymentImportedList(@RequestParam List<Integer> ids) {
        return commuteBalanceService.markPaymentImportedList(ids);
    }
}
