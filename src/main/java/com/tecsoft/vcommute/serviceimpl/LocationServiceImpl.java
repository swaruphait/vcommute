package com.tecsoft.vcommute.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tecsoft.vcommute.model.City;
import com.tecsoft.vcommute.repository.CityRepository;
import com.tecsoft.vcommute.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> fetchAllCity() {
        return ResponseEntity.status(HttpStatus.OK).body(cityRepository.findAll());
    }

    @Override
    public ResponseEntity<?> fetchAllCityPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("city").ascending());
        Page<City> pageCity = cityRepository.fetchCityList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageCity);
    }

    @Override
    public ResponseEntity<?> editCity(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(cityRepository.findById(id));
    }

}
