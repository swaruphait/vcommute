package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.State;


public interface StateRepository extends JpaRepository<State, Integer>{

 boolean existsByNameAndCountryId(String name, Integer countryId);
 
 List<State> findAllByCountryId(Integer countryId);
    
}
