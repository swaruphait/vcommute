package com.vareli.tecsoft.vcommute_business.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "mst_user")
@JsonIgnoreProperties({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "password", "embedding" })
public class User extends Auditable<String> {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @NotNull
   private String username;
   private String password;
   private String name;
   private String email;
   private String role;
   private String mobile;
   private String employeeId;
   private boolean enabled;
   private boolean accountNotExpired;
   private String deviceId;
   private Integer deptId;
   private Integer designationId;
   private String qualification;
   private Integer approvalLevelId;

   private Integer superCompanyId;
   private Integer companyId;
	private String images;

   private Integer gradeId;
   private Integer locationId;
   private Integer stateId;

   private Long reportManager;
   private Long hod;
   private Long financeAuth;
   private String financeAccessArea;

   private String bankName;
   private String bankIFSCCode;
   private String bankAccountNo;
   private String bankType;

   @JsonIgnore
   @Column(columnDefinition = "JSON")
   private String embedding;

   @Transient
   private String companyName;
   @Transient
   private String location;
   @Transient
   private String grade;
   @Transient
   private String department;
   @Transient
   private String designation;
   @Transient
   private String nameReportManager;
   @Transient
   private String nameHod;
   @Transient
   private String nameFinanceAuth;
   @Transient
   private String rawPassword;
   @Transient
   private Long openAttendanceId;
   @Transient
   private Long openCommuteHeaderId;
   @Transient
   private Long openCommuteDetailsId;
   @Transient
	private boolean isDocumentRequired;
	@Transient
	private boolean isPriceRequired;
   @Transient
   private List<CommuteHeader> commuteData;
   @Transient
   private List<Attendance> attendanceData;
   @Transient
   private double[] embeddingData;
   @Transient
   private boolean checkEmbedding;
}
