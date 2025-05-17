package com.tecsoft.vcommute.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "mst_city")
public class City {
    @Id
    private Long id;
    private String city;
    private Integer state_id;
    private String active;
    @Transient
    private String stateName;
    @Transient
    private String country;
}
