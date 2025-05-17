package com.vareli.tecsoft.vcommute_business.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "mst_leave_data")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
public class LeaveData extends Auditable<String>{
     @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private Long userId;
    private LocalDate leaveStartDate;
    private LocalDate leaveEndDate;
    private Integer noOfDaysLeave;
    private Integer leaveTypeId;
    private Integer superCompanyId;
    private Integer companyId;
    private String status;
    private String reason;
    private Long approvedBy;

    @Transient
    private String name;

    @Transient
    private String typeOfLeave;

    @Transient
    private String activityBy;
}
