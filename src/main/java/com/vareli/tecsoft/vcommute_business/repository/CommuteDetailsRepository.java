package com.vareli.tecsoft.vcommute_business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.CommuteDetails;

public interface CommuteDetailsRepository extends JpaRepository<CommuteDetails, Long>{
    
}
