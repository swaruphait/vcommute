package com.tecsoft.vcommute.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tecsoft.vcommute.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Set<Role> findByName(String name);

    boolean existsByName(String name);

    @Query(value = "SELECT count(*) FROM adm_user_reg a left join amd_users_roles b ON (a.user_id=b.user_id) left join amd_user_typ c ON (b.role_id=c.role_id) where c.name=?1", nativeQuery = true)
    Integer roleCount(String name);
}
