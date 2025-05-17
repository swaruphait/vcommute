package com.vareli.tecsoft.vcommute_business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vareli.tecsoft.vcommute_business.model.SuperCompany;


public interface SuperCompanyRepository extends JpaRepository<SuperCompany, Integer>{
    boolean existsByNameAndEmail(String name, String email);
}
