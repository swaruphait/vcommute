package com.vareli.tecsoft.vcommute_business.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.CommuteHeader;
import com.vareli.tecsoft.vcommute_business.model.dto.FetchCommuteDto;

public interface CommuteHeaderRepository extends JpaRepository<CommuteHeader, Long> {

    @Query(value = "SELECT DISTINCT a.* FROM mst_commute_header a JOIN trn_commute_details b ON a.id = b.header_id WHERE a.user_id = ?1 "+
            "AND a.super_company_id= ?2 AND a.company_id= ?3 AND a.status IS FALSE AND a.start_date= ?4 "+
            "ORDER BY a.start_date DESC, a.start_time DESC limit 1", nativeQuery = true)
    List<CommuteHeader> fetchLastUnfinishCommute(Long id, Integer superCompanyId, Integer companyId, LocalDate date);

    @Query(value = "SELECT h.id,u.name,u.employee_id,h.start_date,h.start_time,h.start_location,h.start_location_area,h.start_lat,h.start_long,h.end_date,h.end_time,h.end_location, " + 
            "h.end_location_area,h.end_lat,h.end_long, h.total_actual_price, h.total_distance, h.total_estimate_price,h.total_time,h.status,h.approval_level, " + 
            "h.purpose,h.referencenumber,c.name as customer FROM mst_commute_header h left join mst_user u on(h.user_id=u.id) left join mst_customer c on(h.cust_id=c.id) " + 
            "where h.user_id= ?1 order by h.start_date desc, h.start_time desc", nativeQuery = true)
    List<FetchCommuteDto> fetchCommuteDataMobile(Long userId);

    @Query(value = "SELECT c.* FROM mst_commute_header c LEFT JOIN mst_user u ON (c.user_id = u.id) LEFT JOIN mst_apvl_dtls d ON "+
            "(u.approval_level_id = d.header_id AND d.apvl_level = c.approval_level) WHERE d.user_id = ?1 AND d.status IS TRUE " +
            "AND c.status IS TRUE AND c.super_company_id= ?2 AND c.company_id= ?3 order by start_date desc, start_time desc", nativeQuery = true)
    List<CommuteHeader> fetchCommuteApprovalData(Long userId, Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT DISTINCT c.* FROM mst_commute_header c LEFT JOIN mst_user u ON (c.user_id = u.id) LEFT JOIN trn_appvl_data a on(a.commute_id=c.id) "+
            "LEFT JOIN mst_apvl_dtls d ON (u.approval_level_id = d.header_id AND d.apvl_level = a.prev_appvl_order) WHERE d.user_id = ?1  AND d.status IS TRUE "+
            "AND c.status IS TRUE AND c.super_company_id= ?2 AND c.company_id= ?3 AND c.approval_level='D' order by start_date desc, start_time desc", nativeQuery = true)
    List<CommuteHeader> fetchCommuteDisapproveData(Long userId, Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT * FROM mst_commute_header where start_date between ?1 and ?2 AND super_company_id= ?3 AND company_id= ?4 order by start_date desc, start_time desc", nativeQuery = true)
    List<CommuteHeader> fetchCustomerList(String startDate, String endDate, Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT c.* FROM mst_commute_header c LEFT JOIN mst_user u ON (c.user_id = u.id) LEFT JOIN mst_apvl_dtls d ON (u.approval_level_id = d.header_id) " + 
        "WHERE c.start_date between ?1 and ?2 AND  d.user_id =?3  AND d.status IS TRUE AND c.super_company_id= ?4 AND c.company_id= ?5 order by start_date desc", nativeQuery = true)
   List<CommuteHeader> fetchCommuteDataAuthorityUnderUser(String startDate, String endDate, Long userId, Integer superCompanyId, Integer companyId);

   @Query(value = "SELECT * FROM mst_commute_header where DAY(start_date) BETWEEN ?1 AND ?2 AND approval_level='F' and super_company_id= ?3 and company_id= ?4", nativeQuery = true)
   List<CommuteHeader> fetchFinanceApprovalDataList(Integer startDay, Integer endDay,Integer superCompanyId, Integer companyId);

}
