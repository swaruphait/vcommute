package com.tecsoft.vcommute.service;

import org.springframework.http.ResponseEntity;

public interface DashboardService {

    ResponseEntity<?> adminBarchartdata();

    ResponseEntity<?> adminWeekchartdata();

    ResponseEntity<?> adminMntAttndchartdata();

    ResponseEntity<?> adminMntUsesApplication();

    ResponseEntity<?> adminUserDistributiondata();

    ResponseEntity<?> hodBarchartdata();

    ResponseEntity<?> hodWeekchartdata();

    ResponseEntity<?> hodMntAttndchartdata();

    ResponseEntity<?> hodMntUsesApplication();

    ResponseEntity<?> hodMonthlyvisit();

    ResponseEntity<?> monthlyvisit();

}
