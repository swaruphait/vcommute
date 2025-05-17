package com.vareli.tecsoft.vcommute_business.model.dto;

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
public class AppvlDtlsDto {
    private Long userId;
	private  Integer apvlLevel; 
}
