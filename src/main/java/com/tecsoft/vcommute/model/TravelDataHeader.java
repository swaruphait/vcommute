package com.tecsoft.vcommute.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecsoft.vcommute.audit.Auditable;

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
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" })
public class TravelDataHeader extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long custId;

    private LocalDate startDate;
    private LocalTime startTime;
    private String startLocation;
    private String startLocationArea;
    private Double startLat;
    private Double startLong;

    private LocalDate endDate;
    private LocalTime endTime;
    private Double endLat;
    private Double endLong;
    private String endLocation;
    private String endLocationArea;

    private Double totalDistance;
    private String totalTime;
    private Double totalEstimatePrice;
    private Double totalActualPrice;

    private boolean status;

    private String approvalLevel;

    private String purpose;
    private String referencenumber;
    private String note;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "travelDataHeader")
    @JsonIgnoreProperties("travelDataHeader")
    private Set<TravelDataDetails> travelDataDetails;

    @Transient
    private String username;

    @Transient
    private String employeeId;

    @Transient
    private String customerName;

    @Transient
    private String customerLocation;

	private boolean isMultiCommute;
	private boolean isIntervelStop;

	@Transient
	private String cityName;

}
