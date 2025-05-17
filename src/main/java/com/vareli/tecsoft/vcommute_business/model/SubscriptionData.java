package com.vareli.tecsoft.vcommute_business.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vareli.tecsoft.vcommute_business.audit.Auditable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mst_subscription")
@JsonIgnoreProperties({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
public class SubscriptionData extends Auditable<String>{
     @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private String planName;
    private Integer planId;
    private Integer addNoOfCompany;
	private Integer addNoOfUser;
    private boolean commuteAccess;
	private boolean attendaceAccess;
    private boolean leaveAccess;
    private boolean biometricAttend;
	private boolean financeAccess;
    private boolean status;
    private Integer superCompanyId;
    private Integer validity;
    private double price;
    private LocalDate purchaseDate;
    private LocalDate startDate;
	private LocalDate endDate;
}
