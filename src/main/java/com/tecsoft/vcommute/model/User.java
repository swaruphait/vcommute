package com.tecsoft.vcommute.model;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "adm_user_reg")
@EntityListeners(AuditingEntityListener.class)
public class User extends Auditable<String> {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String name;
    @Column(name = "emp_code")
    private String empCode;
    private String email;
    private String mobile;
    private String images;
    @JsonIgnore
    private String password;
    private Integer companyId;
    private Long location_id;
    private boolean enabled;
    private Integer grade_id;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "bankifsccode")
    private String bankIFSCCode;
    @Column(name = "bankacno")
    private String bankACNo;
    @Column(name = "bank_type")
    private String bankType;
    @Column(name = "device_id")
    private String deviceId;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private String shiftInd;
    private String department;
    private Long reportManager;
    private Long hod;
    private Long financeAuth;
    private String financeAccessArea;

	@Column(columnDefinition = "JSON")
    private String embedding;

    private boolean faceAttendance;
    private boolean normalAttendance;
    private boolean faceUploaded;

    private String appvBy;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "amd_users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Transient
    private String grade;

    @Transient
    private String role;

    @Transient
    private String location;

    @Transient
    private String companyName;

    @Transient
    private String nameReportManager;

    @Transient
    private String nameHod;

    @Transient
    private String nameFinanceAuth;

    @Transient
    private String rawPassword;

    @Transient
	private String privilegeType;

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
	private List<TravelDataHeader> commuteData;

	@Transient
	private List<AttendanceData> attendanceData;

	@Transient
	private double[] embeddingData;

	@Transient
	private boolean checkEmbedding;

}
