package com.tecsoft.vcommute.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.model.AdvanceBalance;

public interface AdvanceBalanceRepository extends JpaRepository<AdvanceBalance, Integer>{

    @Query(value = "SELECT * FROM advance_balance where advance_date between ?1 AND ?2", nativeQuery = true)
    List<AdvanceBalance> fetchAdvanceData(LocalDate stDate, LocalDate enDate);

    Optional<AdvanceBalance> findByBatchNo(String batchNo);
    
}
