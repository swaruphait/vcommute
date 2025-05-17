package com.tecsoft.vcommute.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" })
public class AttendanceData extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Double startLat;
    private Double startLong;
    private String startLocation;
    private String startLocationArea;
    private Double endLat;
    private Double endLong;
    private String endLocation;
    private String endLocationArea;
    private boolean status;
    private Integer companyId;
    private String remarks;
    private String purpose;
    private String approvalLevel;
    private boolean isOpenStatus;
    private Long startCustId;
    private Long endCustId;
    private String givenBy;

    @Transient
    private String customerName;
    @Transient
    private String startCustName;
    @Transient
    private String endCustName;
    @Transient
    private String username;
    @Transient
    private String empcode;
    @Transient
    private String companyName;
	private String overDesc;
	private String overDutyIndi;
	@Transient
	private String customerLocation;
	@Transient
	private String userName;

}
