package com.vareli.tecsoft.vcommute_business.model;

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
@Table(name = "mst_cluster")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
public class Cluster extends Auditable<String>{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String clusterName;
	private Integer startDate;
	private Integer endDate;
    private Integer superCompanyId;
    private Integer companyId;
	private boolean status;

	@Transient
	private String companyName;
}
