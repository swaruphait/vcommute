package com.tecsoft.vcommute.dto;

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
public class MultiApproveDto {
    public Long id;
    public Integer clusetrId;
    public Double totalActualPrice;

}
