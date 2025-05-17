package com.tecsoft.vcommute.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tecsoft.vcommute.model.User;

public interface UserRepositiry extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);


    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsernameAndEmailAndMobile(String username, String email, String mobile);

    @Query(value = "select * from adm_user_reg where username=?1", nativeQuery = true)
    Optional<User> findByUserName(String username);

    @Query(value = "select * from adm_user_reg where company_id=?1", nativeQuery = true)
    List<User> userByCompanyId(Integer cId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    public User findByUsername(String username);

    @Query(value = "select user_id, email, enabled, location_id, b.loc_nm as location, mobile, name, password, username from adm_user_reg a join mst_location b on(a.location_id=b.loc_id)order by a.name", nativeQuery = true)
    public List<User> getUserBylocation();

    @Query(value = "select count(distinct(username)) from adm_user_reg", nativeQuery = true)
    public Integer totalUser();

    @Query(value = "select count(distinct(username)) from adm_user_reg where company_id = ?1", nativeQuery = true)
    public Integer totalUserByCompany(Integer coid);

    @Query(value = "select * from adm_user_reg where company_id=?1", nativeQuery = true)
    public List<User> findUserByCompanyId(Integer companyId);

    @Query(value = "select * from adm_user_reg order by name asc", nativeQuery = true)
    List<User> findAll();

    @Transactional
    @Modifying
    @Query(value = "UPDATE adm_user_reg SET device_id = NULL WHERE user_id =?1", nativeQuery = true)
    void resetDeviceId(@Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE adm_user_reg SET images = NULL, embedding = NULL, face_uploaded= false, appv_by='O', face_attendance=false WHERE user_id = ?1", nativeQuery = true)
    void resetImage(@Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE adm_user_reg SET embedding = NULL, face_uploaded= false, appv_by='O', face_attendance=false WHERE user_id = ?1", nativeQuery = true)
    void resetEmbadding(@Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE adm_user_reg SET device_id = NULL WHERE device_id IS NOT NULL", nativeQuery = true)
    void resetDeviceIdAll();

    @Query(value = "SELECT * FROM adm_user_reg where (report_manager=?1 OR hod=?2) and enabled is true order by name asc", nativeQuery = true)
    List<User> fetchEmployeeAuthorityWise(Long managerId, Long hodId);

    @Transactional
    @Modifying
	@Query(value = "UPDATE adm_user_reg SET embedding = NULL, face_uploaded= false, appv_by='O', face_attendance=false WHERE user_id =?1", nativeQuery = true)
	void resetEmabddingById(@Param("user_id") Long user_id);

}
