package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer>{
    
    List<Department> findAllBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

    boolean existsByNameAndSuperCompanyIdAndCompanyId(String name, Integer superCompanyId ,Integer companyId);
}
