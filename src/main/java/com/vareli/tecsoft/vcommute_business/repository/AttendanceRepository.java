package com.vareli.tecsoft.vcommute_business.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query(value = "SELECT * FROM mst_attendance where user_id= ?1 AND super_company_id= ?2 AND company_id= ?3 AND start_date= ?4 "
            +
            "AND status is false order by start_date desc, start_time desc limit 1", nativeQuery = true)
    List<Attendance> fetchLastUnfinishAttendance(Long id, Integer superCompanyId, Integer companyId, LocalDate date);

    List<Attendance> findAllByUserIdOrderByStartDateDescStartTimeDesc(Long userId);

    @Query(value = "SELECT * FROM mst_attendance where start_date between ?1 and ?2 and super_company_id= ?3 and company_id= ?4 order by start_date desc, start_time desc", nativeQuery = true)
    List<Attendance> fetchAttendance(String startDate, String endDate, Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT c.* FROM mst_attendance c LEFT JOIN mst_user u ON (c.user_id = u.id) LEFT JOIN mst_apvl_dtls d ON (u.approval_level_id = d.header_id) "
            +
            "WHERE c.start_date between ?1 and ?2 AND d.user_id = ?3 AND d.status IS TRUE AND c.super_company_id= ?4 AND c.company_id= ?5 order by start_date desc", nativeQuery = true)
    List<Attendance> fetchAttendanceAthorityUnder(String startDate, String endDate, Long userId, Integer superCompanyId,
            Integer companyId);

}
