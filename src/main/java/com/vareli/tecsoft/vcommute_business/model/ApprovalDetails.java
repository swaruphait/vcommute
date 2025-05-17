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
@Table(name = "mst_apvl_dtls")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate","approvalHeader"})
public class ApprovalDetails extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private Long userId;
    private Integer apvlLevel;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "header_id", nullable = false)
    private ApprovalHeader approvalHeader;
    private boolean status;

    @Transient
    private String name;
}
