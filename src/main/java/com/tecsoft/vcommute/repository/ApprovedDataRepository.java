package com.tecsoft.vcommute.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.model.ApprovedData;

public interface ApprovedDataRepository extends JpaRepository<ApprovedData, Long> {

    @Query(value = "SELECT * FROM trn_apprv_data WHERE date BETWEEN ?1 AND ?2  order by date ASC ", nativeQuery = true)
    List<ApprovedData> fetchactivityreport(LocalDate stdt, LocalDate eddt);

    @Query(value = "SELECT * FROM trn_apprv_data where td_id= ?1 and apprv_by= ?2 and apprv_order !='D'", nativeQuery = true)
    Optional<ApprovedData> findByTdIdAndAppr(Long td_id, String apprv_by);

    @Query(value = "SELECT * FROM trn_apprv_data where td_id= ?1 and apprv_by= ?2 and apprv_order ='D' ", nativeQuery = true)
    Optional<ApprovedData> findByTdIdAndApv(Long td_id, String apprv_by);

    Optional<ApprovedData> findByTdId(Long tdId);
}
