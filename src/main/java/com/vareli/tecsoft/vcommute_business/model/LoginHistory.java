package com.vareli.tecsoft.vcommute_business.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "login_history")
public class LoginHistory {
     @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;
	private String Name;
	private LocalDateTime loginTime;
	private LocalDateTime logoutTime;
	private String sessionId;
	private Integer companyId;
	private Integer superCompanyId;
	private String ipAddress;
}
