package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.Cluster;

public interface ClusterService {

    ResponseEntity<?> createCluster(Cluster cluster);

    ResponseEntity<?> findByAll(Integer companyId);

    ResponseEntity<?> findByAllActive();

    ResponseEntity<?> findById(Integer id);

    ResponseEntity<?> activeCluster(Integer id);

    ResponseEntity<?> deactiveCluster(Integer id);
    
}
