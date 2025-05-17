package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.Cluster;
import com.vareli.tecsoft.vcommute_business.service.ClusterService;

@RestController
@RequestMapping("/cluster")
public class ClusterController {
   
    @Autowired
    private ClusterService clusterService;

    @PostMapping(value = "/createCluster")
	public ResponseEntity<?> createCluster(@RequestBody Cluster cluster) {
		return clusterService.createCluster(cluster);
	}

     @GetMapping(value = "/findByAll")
	public ResponseEntity<?> findByAll(@RequestParam(required = false) Integer companyId) {
		return clusterService.findByAll(companyId);
	}

	@GetMapping(value = "/findByAllActive")
	public ResponseEntity<?> findByAllActive() {
		return clusterService.findByAllActive();
	}

	@GetMapping(value = "/findById")
	public ResponseEntity<?> findById(@RequestParam Integer id) {
		return clusterService.findById(id);
	}

    @GetMapping(value = "/activeCluster")
	public ResponseEntity<?> activeCluster(@RequestParam Integer id) {
		return clusterService.activeCluster(id);
	}

    @GetMapping(value = "/deactiveCluster")
	public ResponseEntity<?> deactiveCluster(@RequestParam Integer id) {
		return clusterService.deactiveCluster(id);
	}
}
