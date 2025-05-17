package com.vareli.tecsoft.vcommute_business.controller.android;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.service.android.MobileLocationService;

@RestController
@RequestMapping("/android")
public class MobileLocationController {
    
    @Autowired
    private MobileLocationService mobileLocationService;

     @GetMapping(value ="/fetchAllCity")
	public ResponseEntity<?> fetchAllCity() {
	     return mobileLocationService.fetchAllCity();
	}
}
