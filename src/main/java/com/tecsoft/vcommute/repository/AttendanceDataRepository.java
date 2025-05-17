package com.tecsoft.vcommute.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.dto.VAttendanceDto;
import com.tecsoft.vcommute.model.AttendanceData;

public interface AttendanceDataRepository extends JpaRepository<AttendanceData, Long> {

    List<AttendanceData> findByUserIdOrderByStartDateDescStartTimeDesc(Long userId);

    @Query(value = "SELECT * FROM attendance_data where timestampdiff(HOUR,CONCAT(start_date,' ',start_time), NOW()) >=12  and start_date=?1 AND status = false", nativeQuery = true)
    List<AttendanceData> fetchLastUnfinishAttendance(LocalDate date);

    @Query(value = "SELECT * FROM attendance_data where start_date between ?1 and ?2 and status is true", nativeQuery = true)
    List<AttendanceData> attandanceDataDataByDate(LocalDate stdt, LocalDate enddt);

    @Query(value = "select count(distinct(id)) from attendance_data where start_date BETWEEN ?1 AND ?2", nativeQuery = true)
    public Integer attendanceMonthCount(LocalDate stdt, LocalDate eddt);

    List<AttendanceData> findByIsOpenStatusAndStatus(boolean isOpenStatus, boolean status);

    @Query(value = "SELECT a.* FROM attendance_data a left join adm_user_reg b on(a.user_id=b.user_id) left join mst_customer stc on(a.start_cust_id=stc.id) left join mst_customer enc on(a.end_cust_id=enc.id) " +
    "where (b.report_manager=?1 OR b.hod=?2) order by start_date desc, start_time desc;", nativeQuery = true)
    List<AttendanceData> fetchAuthorityWiseAttendanceDatas(Long managerId, Long hodId);

    @Query(value = "SELECT * FROM attendance_data where user_id=?1 and start_date=?2 and status is false order by start_date desc, start_time desc limit 1", nativeQuery = true)
    List<AttendanceData> fetchLastUnfinishAttendance(Long id, LocalDate dt);

    @Query(value = "SELECT a.* FROM vintegrate_tour.attendance_data a JOIN adm_user_reg b ON a.user_id = b.user_id WHERE"
            + " (b.shift_start_time = ?1 OR (b.shift_start_time IS NULL AND TIME_TO_SEC(a.start_time) >= 50400))"
            + " AND a.start_date BETWEEN ?2 AND ?3 AND a.approval_level IS NULL AND a.status is false", nativeQuery = true)
    List<AttendanceData> fetchShiftWiseAttendance(LocalTime sftTime, LocalDate stDate, LocalDate endDate);

    

    @Query(value = "SELECT DISTINCT user_id, emp_code,st_dt,TIME(FIRST_VALUE(st_time) OVER w) AS st_time,st_location,end_dt, " + 
                "TIME(LAST_VALUE(end_time) OVER w) AS end_time,end_location,name,username FROM (SELECT att.user_id, usr.emp_code, " +
                "att.start_date AS st_dt, att.start_time AS st_time, att.start_location AS st_location, att.end_date AS end_dt, " + 
                "att.end_time AS end_time, att.end_location, usr.name, usr.username FROM attendance_data att LEFT JOIN adm_user_reg usr ON att.user_id = usr.user_id " + 
                "WHERE att.start_date = ?1 UNION ALL SELECT hdr.user_id,usr.emp_code,hdr.start_date AS st_dt, " + 
                "hdr.start_time AS st_time,hdr.start_location AS st_location, hdr.end_date AS end_dt,hdr.end_time AS end_time, " +
                "hdr.end_location,usr.name,usr.username FROM travel_data_header hdr LEFT JOIN adm_user_reg usr ON hdr.user_id = usr.user_id " + 
                "WHERE hdr.start_date = ?1) t WINDOW w AS (PARTITION BY emp_code, st_dt ORDER BY st_time " + 
                "ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) ORDER BY name, st_dt", nativeQuery = true)
    List<VAttendanceDto> fetchAttendanceApi(String startDate);
}
