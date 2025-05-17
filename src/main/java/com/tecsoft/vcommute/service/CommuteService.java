package com.tecsoft.vcommute.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.dto.MultiApproveDto;
import com.tecsoft.vcommute.dto.MultipartImage;

public interface CommuteService {

    ResponseEntity<?> fetchApprovalData();

    ResponseEntity<?> disapprovalTravelData(Long id, Double price, String reason);

    ResponseEntity<?> approvalTravelData(Long id, Integer clusetrId, Double price);

    ResponseEntity<?> approvalTravelDataList(List<MultiApproveDto> data);

    ResponseEntity<?> disapprovalTravelDataList(List<MultiApproveDto> data, String reason);

    ResponseEntity<?> updateDataBygoogleapi();

    ResponseEntity<?> fetchFirstLevelDisapprovData();

    ResponseEntity<?> fetchFinanceLevelDisapprovData();

    ResponseEntity<?> firstLevelReApprove(Long id, Double price);

    ResponseEntity<?> financeLevelReApprove(Long id, Double price);

    ResponseEntity<?> fetchCommuteDatabyDate(String stdate, String enddate);

    ResponseEntity<?> unfinishCommuteAuthority(String stdate, String enddate);

    ResponseEntity<?> reportGenAutority(String stdate, String enddate, String active);

    ResponseEntity<?> fetchAllCommuteData(String stdate, String enddate);

    ResponseEntity<?> unfinishAllCommuteData(String stdate, String enddate);

    ResponseEntity<?> reportGen(String stdate, String enddate, String active);

    ResponseEntity<?> addDocument(MultipartImage file) throws IOException;

}
