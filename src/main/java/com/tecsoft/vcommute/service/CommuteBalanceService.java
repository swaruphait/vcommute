package com.tecsoft.vcommute.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.model.AdvanceBalance;
import com.tecsoft.vcommute.model.CommuteBalance;

public interface CommuteBalanceService {

    ResponseEntity<?> addAdavnceCommute(CommuteBalance commuteBalance);

    ResponseEntity<?> fetchAdavnceById(Long id);

    ResponseEntity<?> fetchAdavnceCommute(String stdate, String enddate);

    ResponseEntity<?> deleteAdavnceCommute(Long id);

    ResponseEntity<?> fetchBalanceSheet();

    ResponseEntity<?> fetchPaiidSheet(String stdate, String enddate);

    ResponseEntity<?> markPaidSheet(Integer id);

    ResponseEntity<?> markListOfPaidSheet(List<Integer> ids);

    ResponseEntity<?> markPaymentImportedList(List<Integer> ids);

    ResponseEntity<?> editAdavnceCommute(AdvanceBalance advanceBalance);

}
