package com.tecsoft.vcommute.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.dto.MultiApproveDto;
import com.tecsoft.vcommute.model.Cluster;

public interface ClusterService {

    ResponseEntity<?> addCluster(Cluster cluster);

    ResponseEntity<?> editCluster(Integer id);

    ResponseEntity<?> fetchCluster();

    ResponseEntity<?> deleteCluster(Integer id);

    ResponseEntity<?> travelDataCluster(Integer id);

    ResponseEntity<?> approveClusterData(Long id, Integer clusetrId, Double price);

    ResponseEntity<?> approveClusterDataList(List<MultiApproveDto> data);

}
