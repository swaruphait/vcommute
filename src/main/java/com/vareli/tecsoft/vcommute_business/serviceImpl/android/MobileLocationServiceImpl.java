package com.vareli.tecsoft.vcommute_business.serviceImpl.android;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.City;
import com.vareli.tecsoft.vcommute_business.repository.CityRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.android.MobileLocationService;

@Service
public class MobileLocationServiceImpl implements MobileLocationService{
    
    @Autowired
    private CityRepository cityRepository;

    @Override
    public ResponseEntity<?> fetchAllCity() {
       List<City> fetchAllCity = cityRepository.findAll();
        if (fetchAllCity.isEmpty()) {
            return ResponseHandler.generateResponse("City is Not Found!", HttpStatus.NOT_FOUND, null);
        } else {
            return ResponseHandler.generateResponse("Fetching all City!", HttpStatus.OK, fetchAllCity);
        }
    }
    
}
