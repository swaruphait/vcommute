package com.tecsoft.vcommute.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.dto.MultiApproveDto;
import com.tecsoft.vcommute.dto.MultipartImage;
import com.tecsoft.vcommute.service.CommuteService;

@RestController
public class CommuteController {

    @Autowired
    private CommuteService commuteService;

    @GetMapping(value = "/fetchApprovalData")
    public ResponseEntity<?> fetchApprovalData() {
        return commuteService.fetchApprovalData();
    }

    @GetMapping(value = "/fetchAllCommuteData")
    public ResponseEntity<?> fetchAllCommuteData(@RequestParam(required = false) String stdate,
            @RequestParam(required = false) String enddate) {
        return commuteService.fetchAllCommuteData(stdate, enddate);
    }

    @GetMapping(value = "/disapprovalTravelData")
    public ResponseEntity<?> disapprovalTravelData(@RequestParam Long id, @RequestParam Double price,
            @RequestParam String reason) {
        return commuteService.disapprovalTravelData(id, price, reason);
    }

    @GetMapping(value = "/approvalTravelData")
    public ResponseEntity<?> approvalTravelData(@RequestParam Long id,
            @RequestParam(required = false) Integer clusetrId, @RequestParam Double price) {
        return commuteService.approvalTravelData(id, clusetrId, price);
    }

    @PostMapping(value = "/approvalTravelDataList", consumes = "application/json")
    public ResponseEntity<?> approvalTravelDataList(@RequestBody List<MultiApproveDto> data) {
        return commuteService.approvalTravelDataList(data);
    }

    @PostMapping(value = "/disapprovalTravelDataList")
    public ResponseEntity<?> disapprovalTravelDataList(@RequestBody List<MultiApproveDto> data,
            @RequestParam String reason) {
        return commuteService.disapprovalTravelDataList(data, reason);
    }

    @GetMapping(value = "/autoupdateDataBygoogleapi")
    public ResponseEntity<?> updateDataBygoogleapi() {
        return commuteService.updateDataBygoogleapi();
    }

    @GetMapping(value = "/fetchFirstLevelDisapprovData")
    public ResponseEntity<?> fetchFirstLevelDisapprovData() {
        return commuteService.fetchFirstLevelDisapprovData();
    }

    @GetMapping(value = "/fetchFinanceLevelDisapprovData")
    public ResponseEntity<?> fetchFinanceLevelDisapprovData() {
        return commuteService.fetchFinanceLevelDisapprovData();
    }

    @GetMapping(value = "/firstLevelReApprove")
    public ResponseEntity<?> firstLevelReApprove(@RequestParam Long id, @RequestParam Double price) {
        return commuteService.firstLevelReApprove(id, price);
    }

    @GetMapping(value = "/financeLevelReApprove")
    public ResponseEntity<?> financeLevelReApprove(@RequestParam Long id, @RequestParam Double price) {
        return commuteService.financeLevelReApprove(id, price);
    }

    @GetMapping(value = "/fetchCommuteDatabyDate")
    public ResponseEntity<?> fetchCommuteDatabyDate(@RequestParam String stdate, @RequestParam String enddate) {
        return commuteService.fetchCommuteDatabyDate(stdate, enddate);
    }

    @GetMapping(value = "/unfinishCommuteAuthority")
    public ResponseEntity<?> unfinishCommuteAuthority(@RequestParam(required = false) String stdate,
            @RequestParam(required = false) String enddate) {
        return commuteService.unfinishCommuteAuthority(stdate, enddate);
    }

    @GetMapping(value = "/unfinishAllCommuteData")
    public ResponseEntity<?> unfinishAllCommuteData(@RequestParam(required = false) String stdate,
            @RequestParam(required = false) String enddate) {
        return commuteService.unfinishAllCommuteData(stdate, enddate);
    }

    @GetMapping(value = "/reportGenAutority")
    public ResponseEntity<?> reportGenAutority(@RequestParam String stdate, @RequestParam String enddate,
            @RequestParam String active) {
        return commuteService.reportGenAutority(stdate, enddate, active);
    }

    @GetMapping(value = "/reportGen")
    public ResponseEntity<?> reportGen(@RequestParam String stdate, @RequestParam String enddate,
            @RequestParam String active) {
        return commuteService.reportGen(stdate, enddate, active);
    }

    @PostMapping("/addDocument")
    public ResponseEntity<?> addDocument(@ModelAttribute MultipartImage file) throws IOException {
        return commuteService.addDocument(file);
    }

}
