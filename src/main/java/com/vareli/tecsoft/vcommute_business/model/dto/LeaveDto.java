package com.vareli.tecsoft.vcommute_business.model.dto;

import java.util.List;

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
public class LeaveDto {
    private Integer totalLevaeEarn;
    private Integer totalLevaeUsed;
    private Integer totalLevaeBalance;
    List<LeaveBalanceDto> LeaveBalanceDto;
}
