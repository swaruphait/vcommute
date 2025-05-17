package com.tecsoft.vcommute.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Entity
@Table
public class TravelDataDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double startLat;
    private Double startLong;
    private LocalDate startDate;
    private LocalTime startTime;
    private String startLocation;
    private String startLocationArea;

    private Double endLat;
    private Double endLong;
    private LocalDate endDate;
    private LocalTime endTime;
    private String endLocation;
    private String endLocationArea;

    private Integer modeId;
    private String modeName;

    private boolean status;
    private String distance;
    private String time;

    private Integer companyId;
    private Integer locationId;
    private String images;

    private Double estimatePrice;

    private Double actualPrice;

    private boolean isPriceRequired;

    private boolean isDocumentRequired;

    private boolean apiStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "travel_data_id", nullable = false)
    @JsonIgnoreProperties("travelDataDetails")
    private TravelDataHeader travelDataHeader;
}
