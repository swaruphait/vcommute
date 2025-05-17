package com.tecsoft.vcommute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecsoft.vcommute.service.DashboardService;

@RestController
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping(value = "/adminBarchartdata")
    public ResponseEntity<?> adminBarchartdata() {
        return dashboardService.adminBarchartdata();
    }

    @GetMapping(value = "/adminWeekchartdata")
    public ResponseEntity<?> adminWeekchartdata() {
        return dashboardService.adminWeekchartdata();
    }

    @GetMapping(value = "/adminMntAttndchartdata")
    public ResponseEntity<?> adminMntAttndchartdata() {
        return dashboardService.adminMntAttndchartdata();
    }

    @GetMapping(value = "/adminMntUsesApplication")
    public ResponseEntity<?> adminMntUsesApplication() {
        return dashboardService.adminMntUsesApplication();
    }

    @GetMapping(value = "/adminUserDistributiondata")
    public ResponseEntity<?> adminUserDistributiondata() {
        return dashboardService.adminUserDistributiondata();
    }

    @GetMapping(value = "/monthlyvisit")
    public ResponseEntity<?> monthlyvisit() {
        return dashboardService.monthlyvisit();
    }
    // ***********************HOD Dashboard****************************

    @GetMapping(value = "/hodBarchartdata")
    public ResponseEntity<?> hodBarchartdata() {
        return dashboardService.hodBarchartdata();
    }

    @GetMapping(value = "/hodWeekchartdata")
    public ResponseEntity<?> hodWeekchartdata() {
        return dashboardService.hodWeekchartdata();
    }

    @GetMapping(value = "/hodMntAttndchartdata")
    public ResponseEntity<?> hodMntAttndchartdata() {
        return dashboardService.hodMntAttndchartdata();
    }

    @GetMapping(value = "/hodMntUsesApplication")
    public ResponseEntity<?> hodMntUsesApplication() {
        return dashboardService.hodMntUsesApplication();
    }

    @GetMapping(value = "/hodMonthlyvisit")
    public ResponseEntity<?> hodMonthlyvisit() {
        return dashboardService.hodMonthlyvisit();
    }

}
