package com.tecsoft.vcommute.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class CommuteBalance extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long userId;
    private Double advanceBalance;
    private LocalDate advanceDate;
    private String purpose;
    private String batchNo;
    private String payStatus;
    private Integer clusetrId;
    private Double payableAmount;
    private LocalDate approvedDt;
    private LocalDateTime importDate;

    @Transient
    private String name;
    @Transient
    private String clusterName;
    @Transient
    private String employeeId;
    @Transient
    private String bankName;
    @Transient
    private String bankAccountNo;
    @Transient
    private String ifscCode;
    @Transient
    private String bankType;
    @Transient
    private Double balance;
}
