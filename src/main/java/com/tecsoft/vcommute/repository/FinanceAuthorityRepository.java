package com.tecsoft.vcommute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsoft.vcommute.model.FinanceAuthority;

public interface FinanceAuthorityRepository extends JpaRepository<FinanceAuthority, Integer> {
    List<FinanceAuthority> findAllByStatus(boolean status);
}
