package com.vareli.tecsoft.vcommute_business.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vareli.tecsoft.vcommute_business.model.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long>{
    Optional<LoginHistory> findBySessionId(String sessionId);
}
