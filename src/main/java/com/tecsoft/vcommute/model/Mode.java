package com.tecsoft.vcommute.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecsoft.vcommute.audit.Auditable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mst_mode")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" })
public class Mode extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long levelId;
    @Column(name = "base_price")
    private Double basePrice;
    private Double basekm;
    private Double priceperkm;
    private Boolean enabled;
    @Column(name = "company_id")
    private Integer companyId;
    private Long locationId;
    @Transient
    private String comanyName;

    @Transient
    private String gradeName;

    @Transient
    private String cityName;

    @Transient
    private String areaName;

    @Transient
    private String levelName;
	
	
	@Transient
	private String name;

}
