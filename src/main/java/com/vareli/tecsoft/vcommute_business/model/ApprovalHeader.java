package com.vareli.tecsoft.vcommute_business.model;

import java.util.List;
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
import com.vareli.tecsoft.vcommute_business.model.dto.AppvlDtlsDto;

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
@Table(name = "mst_apvl_header")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
public class ApprovalHeader extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private String title;
    private Integer superCompanyId;
    private Integer companyId;
    private boolean status;
    @OneToMany(mappedBy = "approvalHeader", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ApprovalDetails> approvalDetails;

    @Transient
	private List<AppvlDtlsDto> details; 

    @Transient
	private String company; 
}
