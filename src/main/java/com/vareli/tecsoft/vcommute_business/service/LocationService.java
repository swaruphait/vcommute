package com.vareli.tecsoft.vcommute_business.service;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.City;
import com.vareli.tecsoft.vcommute_business.model.Country;
import com.vareli.tecsoft.vcommute_business.model.State;

public interface LocationService {

    ResponseEntity<?> addCountry(Country country);

    ResponseEntity<?> findByAllActiveCountry();

    ResponseEntity<?> findByAllCountry();

    ResponseEntity<?> findByCountryId(Integer id);

    ResponseEntity<?> activeCountry(Integer id);

    ResponseEntity<?> deactiveCountry(Integer id);

    ResponseEntity<?> addState(State state);

    ResponseEntity<?> findByAllActiveState();

    ResponseEntity<?> findByAllState();

    ResponseEntity<?> findAllStateCountryWise(Integer countryId);

    ResponseEntity<?> findByStateId(Integer id);

    ResponseEntity<?> activeState(Integer id);

    ResponseEntity<?> deactiveState(Integer id);

    ResponseEntity<?> addCity(City city);

    ResponseEntity<?> findByAllActiveCity();

    ResponseEntity<?> findAllCityStateWise(Integer stateId);

    ResponseEntity<?> findByAllCity();

    ResponseEntity<?> findByCityId(Integer id);

    ResponseEntity<?> activeCity(Integer id);

    ResponseEntity<?> deactiveCity(Integer id);
    
}
