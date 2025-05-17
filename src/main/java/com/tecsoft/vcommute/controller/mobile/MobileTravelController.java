package com.tecsoft.vcommute.controller.mobile;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.dto.TravelDataDto;
import com.tecsoft.vcommute.model.TrackingData;
import com.tecsoft.vcommute.service.mobile.TravelMobileService;

@RestController
@RequestMapping("/mobile") 
public class MobileTravelController {
    
    @Autowired
    private TravelMobileService travelService;

    @PostMapping("/addStartTravelData")
	public ResponseEntity<Object> addStartTravelData(@RequestBody TravelDataDto travelDataHeader) {
		return travelService.addStartTravelData(travelDataHeader);
	}

	@PostMapping("/intervalTravelDataStart")
	public ResponseEntity<Object> intervalTravelDataStart(@RequestParam Long id,
			@RequestBody TravelDataDto travelDataDto) {
		return travelService.intervalTravelDataStart(id, travelDataDto);
	}

	@PostMapping("/intervalTravelDataStop")
	public ResponseEntity<Object> intervalTravelDataStop(@RequestBody TravelDataDto travelData) {
		return travelService.intervalTravelDataStop(travelData);
	}

	@PostMapping("/addStopTravelData")
	public ResponseEntity<Object> addStopTravelData(@RequestBody TravelDataDto travelDataDto) throws IOException {
		return travelService.addStopTravelData(travelDataDto);
	}

    @GetMapping(value = "/fetchTravelData")
	public ResponseEntity<Object> fetchTravelData(@RequestParam Long id) {
		return travelService.fetchTravelData(id);
	}

	@PostMapping("/addTracking")
	public ResponseEntity<Object> addTracking(@RequestBody TrackingData trackingData) {
		return travelService.addTracking(trackingData);
	}

}
