package com.vareli.tecsoft.vcommute_business.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.City;
import com.vareli.tecsoft.vcommute_business.model.Country;
import com.vareli.tecsoft.vcommute_business.model.State;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.repository.CountryRepository;
import com.vareli.tecsoft.vcommute_business.repository.StateRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService{

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> addCountry(Country country) {
       if (country.getId()==null) {
        boolean existsByName = countryRepository.existsByName(country.getName());
        if (!existsByName) {
            country.setStatus(true);
            countryRepository.save(country);
        return ResponseHandler.generateResponse("Successfully saved...", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Country Already Exist...", HttpStatus.BAD_REQUEST, null); 
        }
       } else {
        countryRepository.save(country);
        return ResponseHandler.generateResponse("Successfully saved...", HttpStatus.OK, null);
       }
    }

    @Override
    public ResponseEntity<?> findByAllActiveCountry() {
        List<Country> collect = countryRepository.findAll().stream().filter(c -> c.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully fatched...", HttpStatus.OK, collect);

    }

    @Override
    public ResponseEntity<?> findByAllCountry() {
        List<Country> collect = countryRepository.findAll();
        return ResponseHandler.generateResponse("Successfully fatched...", HttpStatus.OK, collect);
    }

    @Override
    public ResponseEntity<?> findByCountryId(Integer id) {
        Optional<Country> byId = countryRepository.findById(id);
        if (byId.isPresent()) {
        return ResponseHandler.generateResponse("Successfully fatched...", HttpStatus.OK, byId);
            
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong.", HttpStatus.BAD_REQUEST, null); 
            
        }
    }

    @Override
    public ResponseEntity<?> activeCountry(Integer id) {
        Optional<Country> byId = countryRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            countryRepository.save(byId.get());
        return ResponseHandler.generateResponse("Successfully Activated...", HttpStatus.OK, byId);   
        } else {
            return ResponseHandler.generateResponse("Opps.. Already Active.", HttpStatus.BAD_REQUEST, null); 
            
        }
    }

    @Override
    public ResponseEntity<?> deactiveCountry(Integer id) {
        Optional<Country> byId = countryRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            countryRepository.save(byId.get());
        return ResponseHandler.generateResponse("Successfully Deactivated...", HttpStatus.OK, byId);   
        } else {
            return ResponseHandler.generateResponse("Opps.. Already Deactive.", HttpStatus.BAD_REQUEST, null); 
            
        }
    }

    @Override
    public ResponseEntity<?> addState(State state) {
        if (state.getId()==null) {
            boolean existsByName = stateRepository.existsByNameAndCountryId(state.getName(), state.getCountryId());
            if (!existsByName) {
                state.setStatus(true);
                stateRepository.save(state);
            return ResponseHandler.generateResponse("Successfully saved...", HttpStatus.OK, null);
            } else {
                return ResponseHandler.generateResponse("Country Already Exist...", HttpStatus.BAD_REQUEST, null); 
            }
           } else {
            stateRepository.save(state);
            return ResponseHandler.generateResponse("Successfully saved...", HttpStatus.OK, null);
           }
    }

    @Override
    public ResponseEntity<?> findByAllActiveState() {
        List<State> collect = stateRepository.findAll().stream().filter(s -> s.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);   
    }

    @Override
    public ResponseEntity<?> findByAllState() {
        List<State> collect = stateRepository.findAll();
        for (State state : collect) {
            state.setCountry(countryRepository.findById(state.getCountryId()).get().getName());
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);  
    }

    @Override
    public ResponseEntity<?> findAllStateCountryWise(Integer countryId) {
        List<State> collect = stateRepository.findAllByCountryId(countryId).stream().filter(s -> s.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);  
    }



    @Override
    public ResponseEntity<?> findByStateId(Integer id) {
        Optional<State> byId = stateRepository.findById(id);
        if (byId.isPresent()) {
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);   
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong.", HttpStatus.BAD_REQUEST, null);   
        }
    }

    @Override
    public ResponseEntity<?> activeState(Integer id) {
        Optional<State> byId = stateRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            stateRepository.save(byId.get());
        return ResponseHandler.generateResponse("Successfully Activated...", HttpStatus.OK, byId);   
        } else {
            return ResponseHandler.generateResponse("Opps.. Already Active.", HttpStatus.BAD_REQUEST, null); 
            
        }
    }

    @Override
    public ResponseEntity<?> deactiveState(Integer id) {
        Optional<State> byId = stateRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            stateRepository.save(byId.get());
        return ResponseHandler.generateResponse("Successfully Deactivated...", HttpStatus.OK, byId);   
        } else {
            return ResponseHandler.generateResponse("Opps.. Already Deactive.", HttpStatus.BAD_REQUEST, null); 
            
        }
    }

    @Override
    public ResponseEntity<?> addCity(City city) {
        if (city.getId()==null) {
            boolean existsByName = cityRepository.existsByCityAndStateId(city.getCity(), city.getStateId());
            if (!existsByName) {
                city.setStatus(true);
                cityRepository.save(city);
            return ResponseHandler.generateResponse("Successfully saved...", HttpStatus.OK, null);
            } else {
                return ResponseHandler.generateResponse("Country Already Exist...", HttpStatus.BAD_REQUEST, null); 
            }
           } else {
            cityRepository.save(city);
            return ResponseHandler.generateResponse("Successfully saved...", HttpStatus.OK, null);
           }
    }

    @Override
    public ResponseEntity<?> findByAllActiveCity() {
        List<City> collect = cityRepository.findAll().stream().filter(s -> s.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);  
    }

    @Override
    public ResponseEntity<?> findByAllCity() {
        List<City> collect = cityRepository.findAll();
        for (City city : collect) {
            city.setCountry(countryRepository.findById(city.getCountryId()).get().getName());
            city.setState(stateRepository.findById(city.getStateId()).get().getName());
        }
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);  
    }

    @Override
    public ResponseEntity<?> findByCityId(Integer id) {
        Optional<City> byId = cityRepository.findById(id);
        if (byId.isPresent()) {
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, byId);   
        } else {
            return ResponseHandler.generateResponse("Opps.. Something went wrong.", HttpStatus.BAD_REQUEST, null);   
        }
    }

    @Override
    public ResponseEntity<?> activeCity(Integer id) {
        Optional<City> byId = cityRepository.findById(id);
        if (!byId.get().isStatus()) {
            byId.get().setStatus(true);
            cityRepository.save(byId.get());
        return ResponseHandler.generateResponse("Successfully Activated...", HttpStatus.OK, byId);   
        } else {
            return ResponseHandler.generateResponse("Opps.. Already Active.", HttpStatus.BAD_REQUEST, null); 
            
        }
    }

    @Override
    public ResponseEntity<?> deactiveCity(Integer id) {
        Optional<City> byId = cityRepository.findById(id);
        if (byId.get().isStatus()) {
            byId.get().setStatus(false);
            cityRepository.save(byId.get());
        return ResponseHandler.generateResponse("Successfully Deactivated...", HttpStatus.OK, byId);   
        } else {
            return ResponseHandler.generateResponse("Opps.. Already Deactive.", HttpStatus.BAD_REQUEST, null); 
            
        }
    }

    @Override
    public ResponseEntity<?> findAllCityStateWise(Integer stateId) {
        List<City> collect = cityRepository.findAllByStateId(stateId).stream().filter(s -> s.isStatus()).collect(Collectors.toList());
        return ResponseHandler.generateResponse("Successfully Fetched...", HttpStatus.OK, collect);  
    }
    
    
}
