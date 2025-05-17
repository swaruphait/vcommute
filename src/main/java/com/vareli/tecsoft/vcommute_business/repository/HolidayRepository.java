package com.vareli.tecsoft.vcommute_business.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    boolean existsByPurposeAndHolidayDateAndSuperCompanyIdAndCompanyId(String purpose, LocalDate holidayDate,
            Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT * FROM mst_holiday WHERE YEAR(holiday_date) = ?1 and super_company_id= ?2 and company_id= ?3", nativeQuery = true)
    List<Holiday> fetchHolidayList(String year, Integer superCompanyId, Integer companyId);

    @Query(value = "SELECT YEAR(holiday_date) AS holiday_year FROM mst_holiday  where super_company_id= ?1 and company_id= ?2 GROUP BY holiday_year ORDER BY holiday_year DESC", nativeQuery = true)
    List<String> fetchYearList(Integer superCompanyId, Integer companyId);
}
