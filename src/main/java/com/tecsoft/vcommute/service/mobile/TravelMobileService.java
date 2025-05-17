package com.tecsoft.vcommute.service.mobile;

import org.springframework.http.ResponseEntity;

import com.tecsoft.vcommute.dto.TravelDataDto;
import com.tecsoft.vcommute.model.TrackingData;

public interface TravelMobileService {

    ResponseEntity<Object> addStartTravelData(TravelDataDto travelDataHeader);

    ResponseEntity<Object> intervalTravelDataStart(Long id, TravelDataDto travelDataDto);

    ResponseEntity<Object> intervalTravelDataStop(TravelDataDto travelData);

    ResponseEntity<Object> addStopTravelData(TravelDataDto travelDataDto);

    ResponseEntity<Object> fetchTravelData(Long id);

    ResponseEntity<Object> addTracking(TrackingData trackingData);
}
