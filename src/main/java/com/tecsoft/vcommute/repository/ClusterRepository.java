package com.tecsoft.vcommute.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsoft.vcommute.model.Cluster;

public interface ClusterRepository extends JpaRepository<Cluster, Integer> {

}
