package com.vareli.tecsoft.vcommute_business.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "mst_designation")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "department"})
public class Designation extends Auditable<String>{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private String name;
    private boolean status;
    private Integer companyId;
    private Integer superCompanyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Transient
    private String companyName;  

    @Transient
    private Integer departmentId;  

    @Transient
    private String deptName;  
}
