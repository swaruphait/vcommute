package com.tecsoft.vcommute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.service.LocationService;

@RestController
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping(value = "/fetchAllCity")
    public ResponseEntity<?> fetchAllCity() {
        return locationService.fetchAllCity();
    }

    @GetMapping(value = "/fetchAllCityPage")
    public ResponseEntity<?> fetchAllCityPage(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "100", required = false) Integer pageSize) {
        return locationService.fetchAllCityPage(pageNumber, pageSize);
    }

    @GetMapping(value = "/editCity")
    public ResponseEntity<?> editCity(@RequestParam Long id) {
        return locationService.editCity(id);
    }

}
