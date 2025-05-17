package com.vareli.tecsoft.vcommute_business.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface FetchCommuteDto {
    Long getId();
    String getName();
    String getEmployee_id();
    LocalDate getStart_date();
    LocalTime getStart_time();
    String getStart_location();
    Double getStart_lat();
    Double getStart_long();
    String getStart_location_area();

    LocalDate getEnd_date();
    LocalTime getEnd_time();
    String getEnd_location();
    Double getEnd_lat();
    Double getEnd_long();
    String getEnd_location_area();

    Double getEotal_distance();
    long getTotal_time();
    Double getTotal_estimate_price();
    Double getTotal_actual_price();

    boolean getStatus();
    String getApproval_level();

   String getPurpose();
   String getReferencenumber();
   String getCustomer();
}
