package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.ApprovalHeader;

public interface ApprovalHeaderRepository extends JpaRepository<ApprovalHeader, Integer> {
    
    boolean existsByTitleAndSuperCompanyIdAndCompanyId(String title, Integer superCompanyId, Integer companyId);

    List<ApprovalHeader> findAllBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);
}
