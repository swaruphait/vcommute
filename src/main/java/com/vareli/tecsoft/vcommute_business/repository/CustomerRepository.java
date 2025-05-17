package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

    List<Customer> findAllBySuperCompanyIdAndCompanyIdAndLocationId(Integer superCompanyId, Integer companyId,
            Integer locationId);

    boolean existsBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

    boolean existsByNameAndLocationIdAndBranchAreaAndSuperCompanyIdAndCompanyId(String name, Integer locationId,
            String branchArea, Integer superCompanyId, Integer companyId);

    boolean existsById(Long id);

    boolean existsByNameAndBranchAreaAndLatitudeAndLongitudeAndSuperCompanyIdAndCompanyId(String name,
            String branchArea, double latitude, double longitude, Integer superCompanyId, Integer companyId);

}
