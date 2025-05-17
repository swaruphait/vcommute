package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	List<Role> findAllBySuperCompanyIdAndCompanyId(Integer superCompanyId, Integer companyId);

	boolean existsByNameAndSuperCompanyIdAndCompanyId(String name, Integer companyId, Integer superCompanyId);

	@Query(value = "SELECT count(*) FROM employees_master where role=?1 and company_id=?2", nativeQuery = true)
	Integer roleCount(String name, Integer companyId);

}
