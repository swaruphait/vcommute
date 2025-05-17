package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.SubCompany;


public interface SubCompanyRepository extends JpaRepository<SubCompany, Integer>{

@Query(value = "SELECT * FROM mst_subcompany where super_company_id=?1", nativeQuery = true)
List<SubCompany> fetchAll(Integer superCompanyId);
    
@Query(value = "SELECT count(id) FROM mst_subcompany where super_company_id=?1", nativeQuery = true)
Integer noOfCompanyPresent(Integer SuperCompany);
}
