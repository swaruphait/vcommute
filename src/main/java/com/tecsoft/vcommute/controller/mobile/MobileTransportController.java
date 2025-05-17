package com.tecsoft.vcommute.controller.mobile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.service.mobile.TransportMobileService;

@RestController
@RequestMapping("/mobile")
public class MobileTransportController {

    @Autowired
    private TransportMobileService transportService;
    
    
    @GetMapping(value = "/fetchAllCity")
	public ResponseEntity<Object> fetchAllCity() {
		return transportService.fetchAllCity();
	}

	@GetMapping(value = "/fetchTransportPlain")
	public ResponseEntity<Object> fetchTransportPlain(@RequestParam Integer grade_id,
			@RequestParam Integer location_id) {

		return transportService.fetchTransportPlain(grade_id, location_id);
	}

	@GetMapping(value = "/fetchTransportHill")
	public ResponseEntity<Object> fetchTransportHill(@RequestParam Integer grade_id,
			@RequestParam Integer location_id) {

		return transportService.fetchTransportPlain(grade_id, location_id);
	}

	@GetMapping(value = "/fetchAllAreaTypeTransport")
	public ResponseEntity<Object> fetchAllAreaTypeTransport(@RequestParam Integer grade_id,
			@RequestParam Integer location_id) {

		return transportService.fetchAllAreaTypeTransport(grade_id, location_id);
	}

	@GetMapping(value = "/fetchAllAreaTypeTransportWithoutLocation")
	public ResponseEntity<Object> fetchAllAreaTypeTransportWithoutLocation() {

		return transportService.fetchAllAreaTypeTransportWithoutLocation();
	}

    
}
