package com.tecsoft.vcommute.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.dto.MultiApproveDto;
import com.tecsoft.vcommute.model.Cluster;
import com.tecsoft.vcommute.service.ClusterService;

@RestController
public class ClusterController {

    @Autowired
    ClusterService clusterService;

    @PostMapping(value = "/addCluster", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> addCluster(@RequestBody Cluster cluster, HttpServletRequest request) throws Exception {
        return clusterService.addCluster(cluster);
    }

    @PutMapping(value = "/editCluster")
    public ResponseEntity<?> editCluster(@RequestParam Integer id) {
        return clusterService.editCluster(id);
    }

    @GetMapping("/fetchCluster")
    public ResponseEntity<?> fetchCluster() {
        return clusterService.fetchCluster();
    }

    @DeleteMapping(value = "/deleteCluster")
    public ResponseEntity<?> deleteCluster(@RequestParam Integer id) {
        return clusterService.deleteCluster(id);
    }

    @GetMapping(value = "/travelDataCluster")
    public ResponseEntity<?> travelDataCluster(@RequestParam Integer id) {
        return clusterService.travelDataCluster(id);
    }

    @GetMapping(value = "/approveClusterData")
    public ResponseEntity<?> approveClusterData(@RequestParam Long id,
            @RequestParam(required = false) Integer clusetrId, @RequestParam Double price) {
        return clusterService.approveClusterData(id, clusetrId, price);
    }

    @PostMapping(value = "/approveClusterDataList", consumes = "application/json")
    public ResponseEntity<?> approveClusterDataList(@RequestBody List<MultiApproveDto> data) {
        return clusterService.approveClusterDataList(data);
    }

}
