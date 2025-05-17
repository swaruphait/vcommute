package com.tecsoft.vcommute.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsoft.vcommute.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
