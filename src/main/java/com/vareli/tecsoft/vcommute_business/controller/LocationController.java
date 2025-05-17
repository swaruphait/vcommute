package com.vareli.tecsoft.vcommute_business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.model.City;
import com.vareli.tecsoft.vcommute_business.model.Country;
import com.vareli.tecsoft.vcommute_business.model.State;
import com.vareli.tecsoft.vcommute_business.service.LocationService;

@RestController
@RequestMapping("/location")
public class LocationController {
    
    @Autowired
    private LocationService locationService;


      @PostMapping("/addCountry")
    public ResponseEntity<?> addCountry(@RequestBody Country country) {
        return locationService.addCountry(country);
    }

     @GetMapping("/findByAllActiveCountry")
    public ResponseEntity<?> findByAllActiveCountry() {
        return locationService.findByAllActiveCountry();
    }

    @GetMapping("/findByAllCountry")
    public ResponseEntity<?> findByAllCountry() {
        return locationService.findByAllCountry();
    }

    @GetMapping("/findByCountryId")
    public ResponseEntity<?> findByCountryId(@RequestParam Integer id) {
        return locationService.findByCountryId(id);
    }

    @GetMapping("/activeCountry")
    public ResponseEntity<?> activeCountry(@RequestParam Integer id) {
        return locationService.activeCountry(id);
    }

    @GetMapping("/deactiveCountry")
    public ResponseEntity<?> deactiveCountry(@RequestParam Integer id) {
        return locationService.deactiveCountry(id);
    }

    @PostMapping("/addState")
    public ResponseEntity<?> addState(@RequestBody State state) {
        return locationService.addState(state);
    }

     @GetMapping("/findByAllActiveState")
    public ResponseEntity<?> findByAllActiveState() {
        return locationService.findByAllActiveState();
    }

    @GetMapping("/findByAllState")
    public ResponseEntity<?> findByAllState() {
        return locationService.findByAllState();
    }

    @GetMapping("/findAllStateCountryWise")
    public ResponseEntity<?> findAllStateCountryWise(@RequestParam Integer countryId) {
        return locationService.findAllStateCountryWise(countryId);
    }

    @GetMapping("/findByStateId")
    public ResponseEntity<?> findByStateId(@RequestParam Integer id) {
        return locationService.findByStateId(id);
    }

    @GetMapping("/activeState")
    public ResponseEntity<?> activeState(@RequestParam Integer id) {
        return locationService.activeState(id);
    }

    @GetMapping("/deactiveState")
    public ResponseEntity<?> deactiveState(@RequestParam Integer id) {
        return locationService.deactiveState(id);
    }

    @PostMapping("/addCity")
    public ResponseEntity<?> addCity(@RequestBody City city) {
        return locationService.addCity(city);
    }

     @GetMapping("/findByAllActiveCity")
    public ResponseEntity<?> findByAllActiveCity() {
        return locationService.findByAllActiveCity();
    }

    @GetMapping("/findAllCityStateWise")
    public ResponseEntity<?> findAllCityStateWise(@RequestParam Integer stateId) {
        return locationService.findAllCityStateWise(stateId);
    }

    @GetMapping("/findByAllCity")
    public ResponseEntity<?> findByAllCity() {
        return locationService.findByAllCity();
    }

    @GetMapping("/findByCityId")
    public ResponseEntity<?> findByCityId(@RequestParam Integer id) {
        return locationService.findByCityId(id);
    }

    @GetMapping("/activeCity")
    public ResponseEntity<?> activeCity(@RequestParam Integer id) {
        return locationService.activeCity(id);
    }

    @GetMapping("/deactiveCity")
    public ResponseEntity<?> deactiveCity(@RequestParam Integer id) {
        return locationService.deactiveCity(id);
    }

}
