package com.tecsoft.vcommute.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.model.TravelDataHeader;

public interface TravelDataHeaderRepository extends JpaRepository<TravelDataHeader, Long> {
        
    List<TravelDataHeader> findByUserId(Long userId);
    
    @Query(value = "SELECT * FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) where (b.report_manager=?1 OR b.hod=?2) and a.approval_level='A1' and a.status is true order by start_date desc, start_time desc", nativeQuery = true)
    List<TravelDataHeader> fetchApprovalData(Long managerId, Long hodId);

    @Query(value = "SELECT * FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) where a.start_date BETWEEN ?1 AND ?2 and a.status is true and a.approval_level!='NS' order by start_date desc, start_time desc", nativeQuery = true)
    List<TravelDataHeader> fetchAllCommuteDataByDate(LocalDate stdt, LocalDate enddate);

    @Query(value = "SELECT * FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) where (b.report_manager=?1 OR b.hod=?2) and a.approval_level='A1' and a.start_date BETWEEN ?3 AND ?4 and a.status is true order by start_date desc, start_time desc", nativeQuery = true)
    List<TravelDataHeader> fetchApprovalDataByDate(Long managerId, Long hodId, LocalDate stdt, LocalDate enddate);

    @Query(value = "SELECT * FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) where b.finance_auth=?1 and a.approval_level='A2' and a.status is true and DAY(a.start_date) >= ?2 AND DAY(a.start_date) <= ?3 order by start_date desc, start_time desc", nativeQuery = true)
    List<TravelDataHeader> fetchFinanceApprovalData(Long userId, Integer stdt, Integer enddate);

    @Query(value = "SELECT * FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) where a.approval_level='A2' and a.status is true and DAY(a.start_date) >= ?1 AND DAY(a.start_date) <= ?2     order by start_date desc, start_time desc", nativeQuery = true)
    List<TravelDataHeader> fetchAllFinanceApprovalData(Integer stdt, Integer enddate);

    @Query(value = "SELECT a.* FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) left join trn_apprv_data c on(a.id=c.td_id) where (b.report_manager=?1 OR b.hod=?1) and a.approval_level='D' AND c.apprv_order='A1' order by a.start_date desc, a.start_time desc", nativeQuery = true)
    List<TravelDataHeader> fetchDisapprovedData(Long managerId, Long hodId);

    @Query(value = "SELECT a.* FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) left join trn_apprv_data c on(a.id=c.td_id) where (b.finance_auth=?1) AND  a.approval_level='D' AND c.apprv_order='A2' order by a.start_date desc, a.start_time desc", nativeQuery = true)
    List<TravelDataHeader> fetchFinanceDisapprovedData(Long authId);

    @Query(value = "SELECT a.* FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) left join trn_apprv_data c on(a.id=c.td_id) where a.approval_level='D' AND c.apprv_order='A2' order by a.start_date desc, a.start_time desc", nativeQuery = true)
    List<TravelDataHeader> fetchAllFinanceDisapprovedData();

    // #########################################################
    // *********************Unfinish Data******************************
    // #############################################################

    @Query(value = "SELECT * FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) where (b.report_manager=?1 OR b.hod=?2) and (approval_level = 'NS' OR a.status = false) AND (a.start_date BETWEEN ?3 AND ?4) order by start_date desc, start_time desc", nativeQuery = true)
    List<TravelDataHeader> unfinishTravelDataAuthority(Long managerId, Long hodId, LocalDate stdt, LocalDate eddt);

    @Query(value = "SELECT * FROM travel_data_header a left join adm_user_reg b on(a.user_id=b.user_id) where (approval_level = 'NS' OR a.status = false) AND (a.start_date BETWEEN ?1 AND ?2) order by start_date desc, start_time desc", nativeQuery = true)
    List<TravelDataHeader> unfinishTravelData(LocalDate stdt, LocalDate eddt);

    // #########################################################
    // *********************DashBoard Data******************************
    // #############################################################

    @Query(value = "select count(distinct(id)) from travel_data_header where start_date BETWEEN ?1 AND ?2", nativeQuery = true)
    public Integer monthlyvisit(LocalDate stdt, LocalDate eddt);

    @Query(value = "SELECT count(distinct(user_id)) FROM travel_data_header where start_date BETWEEN ?1 AND ?2", nativeQuery = true)
    public Integer commuteTrafic(LocalDate stdt, LocalDate eddt);

    @Query(value = "select count(distinct(a.id)) from travel_data_header a join adm_user_reg b on(a.user_id = b.user_id) where (b.report_manager=?1 OR b.hod=?2)and a.start_date BETWEEN ?3 AND ?4", nativeQuery = true)
    public Integer monthlyvisitByAuthority(Integer reportId, Integer hodId, LocalDate stdt, LocalDate eddt);

    // #########################################################
    // *********************Report******************************
    // #############################################################
    @Query(value = "SELECT * FROM travel_data_header a join adm_user_reg b on(a.user_id=b.user_id) where (b.report_manager=?1 OR b.hod=?2) AND approval_level!='NS' AND (a.start_date BETWEEN ?3 AND ?4) AND status is true order by a.start_date DESC, a.start_time DESC", nativeQuery = true)
    List<TravelDataHeader> reportGenAuthAllData(Long managerId, Long hodId, LocalDate stdt, LocalDate eddt);

    @Query(value = "SELECT * FROM travel_data_header a join adm_user_reg b on(a.user_id=b.user_id) where (b.report_manager=?1 OR b.hod=?2) AND approval_level!='NS' AND (a.start_date BETWEEN ?3 AND ?4) AND status is true AND approval_level=?5 order by a.start_date DESC, a.start_time DESC", nativeQuery = true)
    List<TravelDataHeader> reportGenAuthTypeWiseData(Long managerId, Long hodId, LocalDate stdt, LocalDate eddt,
            String active);

    @Query(value = "SELECT * FROM travel_data_header a join adm_user_reg b on(a.user_id=b.user_id) where (b.report_manager=?1 OR b.hod=?2) AND approval_level!='NS' AND (a.start_date BETWEEN ?3 AND ?4) AND status is true AND approval_level NOT IN('A','NS','A2','D')  order by a.start_date DESC, a.start_time DESC", nativeQuery = true)
    List<TravelDataHeader> reportGenAuthPendingData(Long managerId, Long hodId, LocalDate stdt, LocalDate eddt);

    @Query(value = "SELECT * FROM travel_data_header a join adm_user_reg b on(a.user_id=b.user_id) where approval_level!='NS' AND (a.start_date BETWEEN ?1 AND ?2) AND status is true order by a.start_date DESC, a.start_time DESC", nativeQuery = true)
    List<TravelDataHeader> reportGenAllData(LocalDate stdt, LocalDate eddt);

    @Query(value = "SELECT * FROM travel_data_header a join adm_user_reg b on(a.user_id=b.user_id) where approval_level!='NS' AND (a.start_date BETWEEN ?1 AND ?2) AND status is true AND approval_level=?3 order by a.start_date DESC, a.start_time DESC", nativeQuery = true)
    List<TravelDataHeader> reportGenTypeWiseData(LocalDate stdt, LocalDate eddt, String active);

    @Query(value = "SELECT * FROM travel_data_header a join adm_user_reg b on(a.user_id=b.user_id) where approval_level!='NS' AND (a.start_date BETWEEN ?1 AND ?2) AND status is true AND approval_level NOT IN('A','NS','A2','D')  order by a.start_date DESC, a.start_time DESC", nativeQuery = true)
    List<TravelDataHeader> reportGenPendingData(LocalDate stdt, LocalDate eddt);

    @Query(value = "SELECT DISTINCT a.* FROM travel_data_header a JOIN travel_data_details b ON a.id = b.travel_data_id WHERE a.user_id = ?1 AND a.status IS FALSE AND a.start_date= ?2 ORDER BY a.start_date DESC, a.start_time DESC limit 1", nativeQuery = true)
    List<TravelDataHeader> fetchLastUnfinishCommute(Long id, LocalDate dt);
}
