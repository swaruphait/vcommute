package com.tecsoft.vcommute.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public interface VAttendanceDto {

    Long getUser_id();
    
    String getEmp_code();

    LocalDate getSt_dt();

    LocalTime getSt_time();

    String getSt_location();

    LocalDate getEnd_dt();

    LocalTime getEnd_time();

    String getEnd_location();

    String getName();

    String getUsername();
    
}
