package com.tecsoft.vcommute.service.mobile;

import org.springframework.http.ResponseEntity;

public interface TransportMobileService {

    ResponseEntity<Object> fetchAllCity();

    ResponseEntity<Object> fetchTransportPlain(Integer grade_id, Integer location_id);

    ResponseEntity<Object> fetchAllAreaTypeTransport(Integer grade_id, Integer location_id);

    ResponseEntity<Object> fetchAllAreaTypeTransportWithoutLocation();  
}
