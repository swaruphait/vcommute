package com.vareli.tecsoft.vcommute_business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.LeaveData;

import java.time.LocalDate;
import java.util.List;


public interface LeaveDataRepository extends JpaRepository<LeaveData, Long>{
    
    List<LeaveData> findAllByUserIdOrderByLeaveStartDateDesc(Long userId);

    @Query(value = "SELECT c.* FROM mst_leave_data c LEFT JOIN mst_user u ON (c.user_id = u.id) LEFT JOIN mst_apvl_dtls d ON " +
                "(u.approval_level_id = d.header_id AND d.apvl_level = 1) WHERE d.user_id = ?1 AND d.status IS TRUE " +
                "AND c.status='C' AND c.super_company_id= ?2 AND c.company_id= ?3 order by leave_start_date desc", nativeQuery = true)
    List<LeaveData>  fetchApprovalLeaveAuthority(Long userId, Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT * FROM mst_leave_data where leave_start_date between ?1 AND ?2 and super_company_id= ?3 AND company_id= ?4", nativeQuery = true)
    List<LeaveData> fetchLeaveData(String startDate, String endDate, Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT c.* FROM mst_leave_data c LEFT JOIN mst_user u ON (c.user_id = u.id) LEFT JOIN mst_apvl_dtls d ON " + 
                "(u.approval_level_id = d.header_id) WHERE c.leave_start_date between ?1 AND ?2 AND d.user_id = ?3 " + 
                "AND d.status IS TRUE AND c.super_company_id= ?4 AND c.company_id= ?5 order by leave_start_date desc", nativeQuery = true)
    List<LeaveData> fetchAuthorityLevaeData(String startDate, String endDate, Long userId, Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT COALESCE(SUM(no_of_days_leave), 0) AS total_leave_days FROM mst_leave_data WHERE user_id = ?1  " + 
                "AND leave_type_id = ?2 AND leave_start_date BETWEEN ?3 AND ?4", nativeQuery = true)
    Integer countLeaveBalance(Long userId, Integer leaveTypeId, LocalDate starDate, LocalDate endDate);

    @Query(value = "SELECT  COALESCE(sum(no_of_days_leave),0) FROM mst_leave_data where user_id= ?1 and leave_start_date BETWEEN ?2 AND ?3 and status in ('A','O')", nativeQuery = true)
    Integer noOfTotalLeaveTaken(Long userId, LocalDate starDate, LocalDate endDate);
    
}
