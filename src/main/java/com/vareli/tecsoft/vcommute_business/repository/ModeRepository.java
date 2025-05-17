package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.Mode;


public interface ModeRepository extends JpaRepository<Mode, Long>{
    
    List<Mode> findAllBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

    boolean existsByLevelIdAndLocationIdAndSuperCompanyIdAndCompanyId(Integer levelId, Integer locationId, Integer superCompanyId, Integer companyId);

    Optional<Mode> findByLevelIdAndLocationIdAndSuperCompanyIdAndCompanyId(Integer levelId, Integer locationId, Integer superCompanyId, Integer companyId);


}
