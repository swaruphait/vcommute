package com.vareli.tecsoft.vcommute_business.serviceImpl.android;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vareli.tecsoft.vcommute_business.model.Transport;
import com.vareli.tecsoft.vcommute_business.repository.TransportRepository;
import com.vareli.tecsoft.vcommute_business.response.ResponseHandler;
import com.vareli.tecsoft.vcommute_business.service.android.MobileModeService;

@Service
public class MobileModeServiceImpl implements MobileModeService{

    @Autowired
    private TransportRepository transportRepository;

    @Override
    public ResponseEntity<Object> fetchTransportType(Integer companyId, Integer superCompanyId) {
        List<Transport> fetchData = transportRepository.findAllBySuperCompanyIdAndCompanyId(superCompanyId, companyId);
        return ResponseHandler.generateResponse("Fetching all Data...", HttpStatus.OK,
            fetchData); 
    }
    
}
