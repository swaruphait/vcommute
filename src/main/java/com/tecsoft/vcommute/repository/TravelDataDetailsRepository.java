package com.tecsoft.vcommute.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.model.TravelDataDetails;

public interface TravelDataDetailsRepository extends JpaRepository<TravelDataDetails, Long> {
    @Query(value = "SELECT * FROM vintegrate_tour.travel_data_details where timestampdiff(HOUR,CONCAT(start_date,' ',start_time), NOW()) >=12  and start_date=?1 AND status = false", nativeQuery = true)
    List<TravelDataDetails> fetchLastUnfinish(LocalDate date);

    @Query(value = "SELECT * FROM travel_data_details a join travel_data_header b on(a.travel_data_id=b.id) where a.api_status = false and a.status is true and b.approval_level!='NS' and b.status is true", nativeQuery = true)
    List<TravelDataDetails> fetchTravelDatagoogleapi();

    @Query(value = "SELECT * FROM travel_data_details a left join travel_data_header b on(a.travel_data_id= b.id) WHERE a.distance is not null and a.time is not null and a.status = true order by  a.start_date desc, a.start_time DESC", nativeQuery = true)
    List<TravelDataDetails> fetchTravelDataTdsUpdate();

    @Query(value = "SELECT a.* FROM travel_data_details a join travel_data_header b on(a.travel_data_id= b.id) where b.user_id=?1 and a.start_date=?2 and b.status is false order by a.start_date desc, a.start_time desc limit 1", nativeQuery = true)
    List<Map<String, Object>> fetchLastUnfinishCommute(Long id, LocalDate dt);

}
