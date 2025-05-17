package com.vareli.tecsoft.vcommute_business.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vareli.tecsoft.vcommute_business.audit.Auditable;

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
@Table(name = "trn_commute_details")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "commuteHeader"})
public class CommuteDetails extends Auditable<String>{
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

    private Integer companyId;
    private String images;

    private Double estimatePrice;
    private Double actualPrice;

    private boolean status;
    private double distance;
    private long time;

    private boolean isPriceRequired;
    private boolean isDocumentRequired;

    private boolean apiStatus;
    private Integer locationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "header_id", nullable = false)
    private CommuteHeader commuteHeader;
}
