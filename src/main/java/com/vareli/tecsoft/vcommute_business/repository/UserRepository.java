package com.vareli.tecsoft.vcommute_business.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.vareli.tecsoft.vcommute_business.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByRole(String role);

    Optional<User> findByUsernameAndEmailAndMobile(String username, String email,String mobile);

    @Query(value = "SELECT count(id) FROM mst_user where super_company_id= ?1 and role!='SUPERADMIN'", nativeQuery = true)
    Integer countNoOfUser(Integer superCompanyId);

    @Query(value = "SELECT * FROM mst_user where role!='SUPERADMIN' and super_company_id= ?1 and company_id= ?2", nativeQuery = true)
    List<User> fetchUserList(Integer superCompanyId, Integer companyId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE mst_user SET device_id = NULL WHERE id = ?1", nativeQuery = true)
    void resetDeviceId(Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE mst_user SET device_id = NULL  WHERE device_id IS NOT NULL AND super_company_id= ?1 AND  company_id= ?2", nativeQuery = true)
    void resetDeviceIdAll(Integer superCompany, Integer companyId);

    @Transactional
    @Modifying
	@Query(value = "UPDATE mst_user SET embedding = NULL WHERE id =?1", nativeQuery = true)
	void resetEmabddingById(Long userId);

    @Query(value = "SELECT distinct(d.apvl_level) FROM mst_user u left join mst_apvl_dtls d on(u.approval_level_id=d.header_id) where u.id= ?1 AND d.status is true order by apvl_level asc", nativeQuery = true)
    List<Integer> fetchApprovalLevel(Long userId);

    @Query(value = "SELECT * FROM mst_user u LEFT JOIN mst_apvl_dtls d ON (u.approval_level_id = d.header_id AND d.apvl_level = 1) WHERE d.user_id = ?1 AND d.status IS TRUE AND enabled is true", nativeQuery = true)
    List<User> fetchAuthorityUnderUserList(Long userId);
    
}
