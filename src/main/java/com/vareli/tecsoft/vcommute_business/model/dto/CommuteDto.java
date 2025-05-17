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
public class CommuteDto {
    public Long headerId;
    private Long userId;
    private Long custId;
    private String startLocation;
    private String endLocation;
    private Double totalDistance;
    private Double totalTime;
    private String purpose;
    private String referencenumber;
    private String note;
    private Integer locationId;
    private Long deatilsId;
    private Double startLat;
    private Double startLong;
    private String startLocationArea;
    private Double endLat;
    private Double endLong;
    private String endLocationArea;
    private Integer ModeId;
    private String images;
    private Double actualPrice;
    private boolean isPriceRequired;
    private boolean isDocumentRequired;
    private String cityName;
    private boolean isMultiCommute;
    private boolean isIntervelStop;
    private Integer companyId;
    private Integer superCompanyId;
}
