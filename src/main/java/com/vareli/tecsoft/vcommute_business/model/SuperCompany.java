package com.vareli.tecsoft.vcommute_business.model;

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



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
@Table(name = "mst_supercompany")
public class SuperCompany extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;
    private Integer cityId;
    private Integer stateId;
    private Integer countryId;
    private Integer pinCode;
    private String mobile;
    private String email;
    private String website;
    private boolean status;

    // @OneToMany(mappedBy = "superCompany", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<PriviledgeType> priviledgeType;

    @OneToMany(mappedBy = "superCompany", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SubCompany> subCompanies;

    @Transient
    private Integer planId;

    @Transient
    private String country;

    @Transient
    private String state;

    @Transient
    private String city;

    @Transient
    private SubscriptionData subscriptionData;
}
