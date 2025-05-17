package com.vareli.tecsoft.vcommute_business.controller.android;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vareli.tecsoft.vcommute_business.service.android.MobileModeService;

@RestController
@RequestMapping("/android")
public class MobileModeController {

    @Autowired
    private MobileModeService mobileModeService;

    @GetMapping(value = "/fetchTransportType")
    public ResponseEntity<?> fetchTransportType(@RequestParam Integer companyId,
            @RequestParam Integer superCompanyId) {
        return mobileModeService.fetchTransportType(companyId, superCompanyId);
    }
}
