package com.vareli.tecsoft.vcommute_business.service.android;

import org.springframework.http.ResponseEntity;

public interface MobileModeService {

    ResponseEntity<Object> fetchTransportType(Integer companyId, Integer superCompanyId);
    
}
