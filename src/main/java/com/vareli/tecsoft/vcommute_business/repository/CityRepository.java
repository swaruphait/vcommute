package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.City;


public interface CityRepository extends JpaRepository<City, Integer>{

    boolean existsByCityAndStateId(String city, Integer stateId);

    List<City> findAllByStateId(Integer stateId);

    Optional<City> findByCity(String city);
}
