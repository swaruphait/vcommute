package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.Department;
import com.vareli.tecsoft.vcommute_business.model.Designation;

public interface DesignationRepository extends JpaRepository<Designation, Integer>{
    
    List<Designation> findBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

    boolean existsByNameAndDepartmentAndSuperCompanyIdAndCompanyId(String name, Department department, Integer superCompanyId, Integer companyId);

    List<Designation> findAllByDepartmentAndStatus(Department department, boolean status);
}
