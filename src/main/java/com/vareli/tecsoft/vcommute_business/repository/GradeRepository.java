package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Integer>{
    

List<Grade> findAllBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

boolean existsByNameAndSuperCompanyIdAndCompanyId(String name, Integer superCompanyId, Integer companyId);


}
