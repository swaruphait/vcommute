package com.vareli.tecsoft.vcommute_business.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.persistence.CascadeType;
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
@Table(name = "mst_commute_header")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate","commuteHeader"})
public class CommuteHeader extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long custId;

    private LocalDate startDate;
    private LocalTime startTime;
    private String startLocation;
    private Double startLat;
    private Double startLong;
    private String startLocationArea;

    private LocalDate endDate;
    private LocalTime endTime;
    private String endLocation;
    private Double endLat;
    private Double endLong;
    private String endLocationArea;

    private Double totalDistance;
    private long totalTime;
    private Double totalEstimatePrice;
    private Double totalActualPrice;

    private boolean status;
    private String approvalLevel;

    private String purpose;
    private String referencenumber;
    private String note;
    private boolean isMultiCommute;
    private boolean isIntervelStop;
    private Integer companyId;
    private Integer superCompanyId;

    @OneToMany(mappedBy = "commuteHeader", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CommuteDetails> commuteDetails;

    @Transient
    private String name;

    @Transient
    private String employeeId;

    @Transient
    private String customerName;

    @Transient
    private String customerLocation;

    @Transient
    private String clusterName;

}
