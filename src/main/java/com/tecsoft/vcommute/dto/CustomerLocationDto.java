package com.tecsoft.vcommute.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLocationDto {
    private String Address;
    private String sortArea;
    private double latitude;
    private double longitude;
    private String pincode;
}
