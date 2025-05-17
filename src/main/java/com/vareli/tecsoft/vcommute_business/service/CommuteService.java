package com.vareli.tecsoft.vcommute_business.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.dto.MultiApproveDto;
import com.vareli.tecsoft.vcommute_business.model.dto.MultipartImage;

public interface CommuteService {

    ResponseEntity<?> fetchApprovalList();

    ResponseEntity<?> approvedCommuteData(Long id, Integer clusetrId, Double price);

    ResponseEntity<?> approvedDataList(List<MultiApproveDto> data);

    ResponseEntity<?> addDocument(MultipartImage file);

    ResponseEntity<?> disapprovedCommuteData(Long id, Double price, String reason);

    ResponseEntity<?> disapprovedCommuteDataList(List<MultiApproveDto> data, String reason);

    ResponseEntity<?> fetchDisApprovalList();

    ResponseEntity<?> reApprovedData(Long id, Double price);

    ResponseEntity<?> fetchCommuteData(String startDate, String endDate);

    ResponseEntity<?> fetchAuthorityData(String startDate, String endDate);

    ResponseEntity<?> fetchFinaceApprovalList(Integer clusterId);

    ResponseEntity<?> cliamSettledApproval(Long id, Integer clusetrId, Double price);
    
}
