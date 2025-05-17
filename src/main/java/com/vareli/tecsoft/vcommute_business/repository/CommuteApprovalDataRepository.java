package com.vareli.tecsoft.vcommute_business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.CommuteApprovalData;

public interface CommuteApprovalDataRepository extends JpaRepository<CommuteApprovalData, Long>{

    @Query(value = "SELECT * FROM trn_appvl_data where commute_id= ?1 order by id desc limit 1", nativeQuery = true)
  Optional<CommuteApprovalData>  fetchLatestApprovalDataByCommuteId(Long commuteId);

  @Query(value = "SELECT * FROM trn_appvl_data where commute_id= ?1 AND current_appvl_order='D' order by id desc limit 1", nativeQuery = true)
  Optional<CommuteApprovalData>  fetchLatestByCommuteId(Long commuteId);
    
}
