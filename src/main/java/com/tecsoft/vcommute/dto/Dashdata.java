package com.tecsoft.vcommute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Dashdata {
    private Integer preMonthTravel;
    private Integer pastMonthTravel;
    private Integer preWeekTravel;
    private Integer pastWeekTravel;
    private Integer preMonthAttendance;
    private Integer pastMonthAttendance;
    private Integer preWeekAttendance;
    private Integer pastWeekAttendance;
    private Integer preMonthUses;
    private Integer pastMonthUses;
    private Integer preWeekUses;
    private Integer pastWeekUses;
}
