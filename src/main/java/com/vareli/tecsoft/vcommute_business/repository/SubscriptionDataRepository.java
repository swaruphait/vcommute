package com.vareli.tecsoft.vcommute_business.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.SubscriptionData;

public interface SubscriptionDataRepository extends JpaRepository<SubscriptionData, Integer>{
    boolean existsByStatusAndSuperCompanyId(boolean status, Integer superCompanyId);

    Optional<SubscriptionData> findByStatusAndSuperCompanyId(boolean status, Integer superCompanyId);

    @Query(value = "SELECT COALESCE((SELECT add_no_of_company FROM mst_subscription WHERE super_company_id = ?1 AND status = TRUE LIMIT 1), 0) AS add_no_of_company" , nativeQuery = true)
    Integer fetchNoOfCompanyLimit(Integer superCompanyId);

    @Query(value = "SELECT COALESCE((SELECT add_no_of_user FROM mst_subscription WHERE super_company_id = ?1 AND status = TRUE LIMIT 1), 0) AS add_no_of_user", nativeQuery = true)
    Integer fetchNoOfUserLimit(Integer superCompanyId);

}