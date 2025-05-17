package com.tecsoft.vcommute.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.model.CommuteBalance;

public interface CommuteBalanceRepository extends JpaRepository<CommuteBalance, Integer> {

    // List<CommuteBalance> findAllByPayStatus(String payStatus);

    boolean existsByUserIdAndBatchNo(Long userId, String batchNo);

    @Query(value = "SELECT CASE WHEN COUNT(user_id) >= 1 THEN  true ELSE false END As name FROM commute_balance WHERE user_id = ?1 AND batch_no = ?2 AND pay_status IN ('ADVANCE', 'PAYABLE')", nativeQuery = true)
    Integer isPresentSameWithBatch(Long userId, String batchNo);

    @Query(value = "SELECT * FROM commute_balance WHERE user_id = ?1 AND batch_no = ?2 AND pay_status IN ('ADVANCE', 'PAYABLE')", nativeQuery = true)
    Optional<CommuteBalance> fetchPresentBatch(Long userId, String batchNo);

    // boolean existsByUserIdAndBatchNoAndPayStatus(Long userId, String batchNo, String payStatus);

    // Optional<CommuteBalance> findByUserIdAndBatchNoAndPayStatus(Long userId, String batchNo, String payStatus);

    Optional<CommuteBalance> findByUserIdAndBatchNo(Long userId, String batchNo);

    @Query(value = "SELECT * FROM commute_balance where pay_status in ('ADVANCE','PAYABLE')", nativeQuery = true)
    List<CommuteBalance> findBalaceSheet();

    @Query(value = "SELECT * FROM commute_balance where pay_status='PAID'", nativeQuery = true)
    List<CommuteBalance> findByPaisReportDateWise();
}
