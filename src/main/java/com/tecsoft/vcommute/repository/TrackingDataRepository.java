package com.tecsoft.vcommute.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsoft.vcommute.model.TrackingData;

public interface TrackingDataRepository extends JpaRepository<TrackingData, Long>{
    
}
