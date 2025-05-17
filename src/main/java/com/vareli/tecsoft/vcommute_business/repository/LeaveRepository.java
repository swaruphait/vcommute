package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Integer>{
    
    List<Leave> findBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT COALESCE(sum(no_of_leave),0) FROM mst_leave where super_company_id= ?1 and company_id= ?2 and status is true", nativeQuery = true)
    Integer totalNoOfLeave(Integer superCompanyId, Integer companyId);
}
