package com.vareli.tecsoft.vcommute_business.service.android;

import org.springframework.http.ResponseEntity;

import com.vareli.tecsoft.vcommute_business.model.dto.CommuteDto;

public interface MobileCommuteService {

    ResponseEntity<?> startCommuteData(CommuteDto commuteData);

    ResponseEntity<?> stopCommuteData(CommuteDto commuteData);

    ResponseEntity<Object> intervalTravelDataStart(Long id, CommuteDto commuteData);

    ResponseEntity<Object> intervalTravelDataStop(CommuteDto commuteData);

    ResponseEntity<Object> fetchCommuteData(Long userId);
    
}
