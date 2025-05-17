package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.Transport;


public interface TransportRepository extends JpaRepository<Transport, Integer>{


    List<Transport> findAllBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

    boolean existsByDesAndSuperCompanyIdAndCompanyId(String des, Integer superCompanyId, Integer companyId);


}
