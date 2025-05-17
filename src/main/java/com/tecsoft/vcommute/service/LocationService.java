package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

public interface LocationService {

    ResponseEntity<?> fetchAllCity();

    ResponseEntity<?> fetchAllCityPage(Integer pageNumber, Integer pageSize);

    ResponseEntity<?> editCity(Long id);

}
