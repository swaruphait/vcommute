package com.tecsoft.vcommute.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttendanceDto {
    private Long id;
    private Long userId;
    private String startDate;
    private LocalTime startTime;
    private String endDate;
    private LocalTime endTime;
    private Double startLat;
    private Double startLong;
    private String startLocation;
    private String startLocationArea;
    private Double endLat;
    private Double endLong;
    private String endLocation;
    private String endLocationArea;
    private boolean status;
    private Integer companyId;
    private String remarks;
    private String purpose;
    private String approvalLevel;
    private boolean isOpenStatus;
    private Long startCustId;
    private Long endCustId;

}
