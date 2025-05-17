package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.CommuteCliamAmount;


public interface AdvanceAmountRepository extends JpaRepository<CommuteCliamAmount, Long>{
    
    @Query(value = "SELECT * FROM trn_advance_amt where super_company_id= ?1 AND company_id= ?2 AND status IN ('C','S')", nativeQuery = true)
    List<CommuteCliamAmount> fetchAdvAmount(Integer superCompanyId, Integer companyId);
}
