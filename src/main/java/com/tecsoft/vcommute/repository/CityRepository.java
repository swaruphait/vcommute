package com.tecsoft.vcommute.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.model.City;

public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findAllByOrderByCityAsc();

    @Query(value = "SELECT CASE WHEN COUNT(city) >= 1 THEN  true ELSE false END As name FROM mst_city WHERE city=?1 and state_id=?2", nativeQuery = true)
    Integer existsByCity(String name, Integer StateId);

    @Query(value = "SELECT a.* FROM mst_city a LEFT JOIN state_master b ON (a.state_id = b.id) LEFT JOIN mst_countries c ON (b.country_id=c.id) WHERE LOWER(city) LIKE ?1", nativeQuery = true)
    List<City> searchCity(String city);

    @Query(value = "SELECT a.* FROM mst_city a LEFT JOIN state_master b ON (a.state_id = b.id) LEFT JOIN mst_countries c ON (b.country_id=c.id) order by a.city", countQuery = "SELECT count(a.id) FROM mst_city a LEFT JOIN state_master b ON (a.state_id = b.id) LEFT JOIN mst_countries c ON (b.country_id=c.id) order by a.city", nativeQuery = true)
    Page<City> fetchCityList(Pageable pageable);
    
    @Query(value = "SELECT * FROM mst_city WHERE LOWER(city) = ?1 order by id asc limit 1", nativeQuery = true)
    Optional<City> findByCityName(String name);

}
