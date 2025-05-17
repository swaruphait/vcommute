package com.vareli.tecsoft.vcommute_business.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.dto.MultiApproveDto;
import com.vareli.tecsoft.vcommute_business.model.dto.MultipartImage;
import com.vareli.tecsoft.vcommute_business.service.CommuteService;

@RestController
@RequestMapping("/commute")
public class CommuteController {

    @Autowired
    private CommuteService commuteService;

    @GetMapping(value = "/fetchApprovalList")
    public ResponseEntity<?> fetchApprovalList() {
        return commuteService.fetchApprovalList();
    }

    @GetMapping(value = "/fetchDisApprovalList")
    public ResponseEntity<?> fetchDisApprovalList() {
        return commuteService.fetchDisApprovalList();
    }

    @PutMapping(value = "/approvedCommuteData")
    public ResponseEntity<?> approvedCommuteData(@RequestParam Long id,
            @RequestParam(required = false) Integer clusetrId, @RequestParam Double price) {
        return commuteService.approvedCommuteData(id, clusetrId, price);
    }

    @PutMapping(value = "/reApprovedData")
    public ResponseEntity<?> reApprovedData(@RequestParam Long id, @RequestParam Double price) {
        return commuteService.reApprovedData(id, price);
    }
 
    @GetMapping(value = "/fetchFinaceApprovalList")
    public ResponseEntity<?> fetchFinaceApprovalList(@RequestParam Integer clusterId) {
        return commuteService.fetchFinaceApprovalList(clusterId);
    }

    @PutMapping(value = "/cliamSettledApproval")
    public ResponseEntity<?> cliamSettledApproval(@RequestParam Long id,
            @RequestParam(required = false) Integer clusetrId, @RequestParam Double price) {
        return commuteService.cliamSettledApproval(id, clusetrId, price);
    }

    @PutMapping(value = "/approvedDataList")
    public ResponseEntity<?> approvedDataList(@RequestBody List<MultiApproveDto> data) {
        return commuteService.approvedDataList(data);
    }

    @PutMapping("/addDocument")
    public ResponseEntity<?> addDocument(@ModelAttribute MultipartImage file) throws IOException {
        return commuteService.addDocument(file);
    }

    @PutMapping(value = "/disapprovedCommuteData")
    public ResponseEntity<?> disapprovedCommuteData(@RequestParam Long id, @RequestParam Double price,
            @RequestParam String reason) {
        return commuteService.disapprovedCommuteData(id, price, reason);
    }

    @PutMapping(value = "/disapprovedCommuteDataList")
    public ResponseEntity<?> disapprovedCommuteDataList(@RequestBody List<MultiApproveDto> data,
            @RequestParam String reason) {
        return commuteService.disapprovedCommuteDataList(data, reason);
    }

    @GetMapping(value = "/fetchCommuteData")
    public ResponseEntity<?> fetchCommuteData(@RequestParam (required = false) String startDate, 
    @RequestParam (required = false) String endDate) {
        return commuteService.fetchCommuteData(startDate, endDate);
    }

    @GetMapping(value = "/fetchAuthorityData")
    public ResponseEntity<?> fetchAuthorityData(@RequestParam (required = false) String startDate, 
    @RequestParam (required = false) String endDate) {
        return commuteService.fetchAuthorityData(startDate, endDate);
    }

    
}
