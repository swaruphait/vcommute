package com.vareli.tecsoft.vcommute_business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.Cluster;
import java.util.List;


public interface ClusterRepository extends JpaRepository<Cluster, Integer>{

    List<Cluster> findAllBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

    boolean existsByClusterNameAndSuperCompanyIdAndCompanyId(String clusterName, Integer superCompanyId, Integer companyId);


}
